package com.example.testglove

import android.util.Log
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.pow
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private var editText1: EditText? = null
    private var resultView: TextView? = null
    private var calculateButton: Button? = null
    private var gloveLoader: GloveLoader? = null
    private val hardcodedQuestions = arrayOf(
        "What are the moments in my life that made me feel most alive?",
        "How have my past experiences shaped my values and beliefs?",
        "What are the biggest challenges I've overcome, and what did I learn from them?",
        "In what ways have I grown over the past year?",
        "What am I most grateful for in my life right now?",
        "What are my most significant strengths, and how can I use them more?",
        "What dreams have I put on hold, and why?",
        "What does happiness mean to me, and what makes me happy?",
        "Who are the people who have impacted my life the most, and how?",
        "What are the biggest regrets of my life, and what lessons have I learned from them?",
        "How do I deal with stress and anxiety, and are there healthier ways I can cope?",
        "What are my short-term and long-term goals, and what steps do I need to take to achieve them?",
        "What aspects of my life would I like to improve, and how can I make those changes?",
        "How do I want to be remembered by others?",
        "What are the things that I am most passionate about?",
        "How do I define success, and do I consider myself successful?",
        "What fears are holding me back from achieving my goals?",
        "How do I practice self-care, and how can I improve it?",
        "What are my most deeply held beliefs, and why do I hold them?",
        "What steps can I take to create a more fulfilling and meaningful life?"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText1 = findViewById(R.id.editText1)
        resultView = findViewById(R.id.resultView)
        calculateButton = findViewById(R.id.cosine)
        // Initially disable the calculate button
        calculateButton?.isEnabled = false

        // Load the GloVe model in a background thread
        Thread {
            try {
                val inputStream =
                    assets.open("glove.txt") // Replace with your file name
                gloveLoader = GloveLoader()
                gloveLoader!!.loadGloveModel(inputStream)
                Log.d("MainActivity", "GloVe model loaded successfully.")

                // Enable the calculate button after loading is complete
                runOnUiThread {
                    calculateButton?.isEnabled = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Error loading GloVe model: ${e.printStackTrace()}")
                runOnUiThread {
                    Toast.makeText(this, "Failed to load GloVe model", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
        calculateButton?.setOnClickListener { findSimilarQuestions() }
    }


    private fun findSimilarQuestions() {
        val startOverall = System.nanoTime()
        val journalEntry = editText1!!.text.toString()
        val journalEntryEmbedding = gloveLoader!!.getAverageEmbedding(journalEntry)

        val questionSimilarities = hardcodedQuestions.map { question ->
            val questionEmbedding = gloveLoader!!.getAverageEmbedding(question)
            val similarity = gloveLoader!!.cosineSimilarity(journalEntryEmbedding, questionEmbedding)
            question to similarity
        }

        val topQuestions = questionSimilarities
            .sortedByDescending { it.second }
            .take(5)
            .joinToString("\n") { it.first }




        resultView!!.text = topQuestions
        val words = topQuestions.split(" ")
        val totalWords = words.size
        val endOverall = System.nanoTime()
        val overallLatency = (endOverall - startOverall) / 1_000_000.0 // Convert to milliseconds
        val averageTokenLatency = overallLatency / totalWords // Average per token in milliseconds

// Formatting the output to three decimal places
        val formattedOverallLatency = String.format("%.3f", overallLatency)
        val formattedAverageTokenLatency = String.format("%.3f", averageTokenLatency)

        val resultText = "Top Questions:\n$topQuestions\n\n" +
                "Overall Latency: ${formattedOverallLatency}ms\n" +
                "Average Per-Token Latency: ${formattedAverageTokenLatency}ms"

        resultView!!.text = resultText
    }

    private class GloveLoader {
        private val embeddings: MutableMap<String, FloatArray> = HashMap()

        @Throws(Exception::class)
        fun loadGloveModel(inputStream: InputStream?) {
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String
            var count = 0 // Counter for the number of words read

            while (reader.readLine().also { line = it } != null) {
                val values = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val word = values[0]

                val vector = FloatArray(50) // Assuming 50-dimensional embeddings
                for (i in 1 until values.size) {
                    vector[i - 1] = values[i].toFloat()
                }
                embeddings[word] = vector

                count++
                if (count >= 399999) { // Break after reading 10,000 words
                    break
                }
            }
            reader.close()
        }

        fun getAverageEmbedding(text: String): FloatArray {
            val words = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val avgVector = FloatArray(50)
            var count = 0
            for (word in words) {
                val vec = embeddings[word]
                if (vec != null) {
                    for (i in vec.indices) {
                        avgVector[i] += vec[i]
                    }
                    count++
                }
            }
            if (count > 0) {
                for (i in avgVector.indices) {
                    avgVector[i] /= count.toFloat()
                }
            }
            return avgVector
        }

        fun cosineSimilarity(vectorA: FloatArray, vectorB: FloatArray): Float {
            var dotProduct = 0.0f
            var normA = 0.0f
            var normB = 0.0f
            for (i in vectorA.indices) {
                dotProduct += vectorA[i] * vectorB[i]
                normA += vectorA[i].toDouble().pow(2.0).toFloat()
                normB += vectorB[i].toDouble().pow(2.0).toFloat()
            }
            return (dotProduct / (sqrt(normA.toDouble()) * sqrt(normB.toDouble()))).toFloat()
        }


    } // Add other necessary methods and calculations here
}