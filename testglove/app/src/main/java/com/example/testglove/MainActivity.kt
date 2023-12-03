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
        "What steps can I take to create a more fulfilling and meaningful life?",
        "What emotions are you feeling right now?",
        "How did this experience impact your day?",
        "What are some things you learned about yourself today?",
        "What can this situation teach you about life?",
        "How did this make you feel about your abilities?",
        "What are you most grateful for today?",
        "How can you use this experience to grow?",
        "What's one positive thing you can take away from this?",
        "How did your actions align with your values today?",
        "What did you do today that made you proud?",
        "How do you think this experience has changed you?",
        "What are some ways you can improve based on today's events?",
        "What thoughts occupied your mind the most today?",
        "How did today's events affect your mood?",
        "What would you do differently if given another chance?",
        "How can you apply what you learned today in the future?",
        "What was the most challenging part of your day?",
        "What part of the day brought you the most joy?",
        "How did you overcome the challenges you faced today?",
        "What are some things you are looking forward to?",
        "How did you handle stress today?",
        "What strategies can you develop to manage similar situations better?",
        "What did you discover about your likes and dislikes?",
        "How can you make tomorrow better?",
        "What steps can you take to achieve your goals?",
        "How did you support your mental health today?",
        "What are some self-care practices you found effective?",
        "How did your interactions with others affect your day?",
        "What did you learn from others today?",
        "How do you feel about the decisions you made?",
        "What are some habits you'd like to develop?",
        "How did you prioritize your tasks and goals today?",
        "What are some ways you can improve your productivity?",
        "How did you balance your personal and professional life?",
        "What are your thoughts on your current life path?",
        "How do you feel about your relationships with others?",
        "What can you do to strengthen your relationships?",
        "What aspects of your life bring you the most satisfaction?",
        "How do you plan to overcome your current challenges?",
        "What are some fears or anxieties you faced today?",
        "How can you address these fears constructively?",
        "What role did patience play in your day?",
        "How did you demonstrate kindness to yourself or others?",
        "What are some ways you can improve your self-talk?",
        "What are you most curious about right now?",
        "How can you nurture your passions and interests?",
        "What does success look like for you in this context?",
        "How can you be more present in your daily activities?",
        "What are some ways you can express your creativity?",
        "How did you take care of your physical health today?",
        "What are your thoughts on your spiritual or philosophical beliefs?",
        "How can you deepen your understanding of these beliefs?",
        "What are some new things you'd like to try?",
        "How do you plan to achieve a better work-life balance?",
        "What are your coping mechanisms for stress or sadness?",
        "How do you plan to address any unresolved issues?",
        "What are some ways you can show gratitude?",
        "How can you practice mindfulness in your daily routine?",
        "What steps can you take towards personal growth?",
        "How do you feel about your current life pace?",
        "What are your thoughts on your current level of self-awareness?",
        "How can you improve your emotional intelligence?",
        "What are some ways you can practice self-compassion?",
        "How did you contribute to your community or others' lives?",
        "What does happiness mean to you in your current situation?",
        "How do you deal with changes or uncertainties?",
        "What are some ways you can enhance your learning experiences?",
        "How can you make your environment more conducive to your goals?",
        "What are your thoughts on your current lifestyle choices?",
        "How can you improve your decision-making process?",
        "What are some long-term goals you're working towards?",
        "How do you stay motivated and inspired?",
        "What are some challenges you anticipate in the future?",
        "How can you better align your actions with your aspirations?",
        "What are your strategies for maintaining mental clarity?",
        "How do you plan to cultivate more joy in your life?",
        "What are some areas where you need more understanding or knowledge?",
        "How do you manage conflicts or disagreements?",
        "What are some ways you can increase your resilience?",
        "How do you define personal success and fulfillment?",
        "What are your strategies for overcoming procrastination?",
        "How do you nurture your physical and mental well-being?",
        "What are some new skills or hobbies you'd like to explore?",
        "How do you maintain balance between giving and receiving?",
        "What are your thoughts on your current financial situation?",
        "How can you manage your finances more effectively?",
        "What are some ways you can cultivate positive relationships?",
        "How do you plan to prioritize your needs and desires?",
        "What are some ways you can support your community or society?",
        "How do you handle feelings of loneliness or isolation?",
        "What are some ways you can expand your social circle?",
        "How do you stay grounded during stressful times?",
        "What are your thoughts on personal responsibility and accountability?",
        "How can you make more time for things that matter to you?",
        "What are some ways you can simplify your life?",
        "How do you plan to celebrate your achievements and successes?",
        "What are your strategies for maintaining hope and optimism?",
        "How can you be more accepting of yourself and others?",
        "What are some ways you can practice forgiveness?",
        "How do you plan to honor your unique journey and experiences?",
        "What are your key strategies for preparing for this exam?",
        "How do you feel about your current level of preparation?",
        "What topics do you find most challenging and why?",
        "How can you allocate your study time more effectively?",
        "What are some ways you can reduce stress before the exam?",
        "How does this exam align with your academic or career goals?",
        "What study methods have you found most effective?",
        "How do you plan to reward yourself after the exam?",
        "What are your thoughts on the importance of this exam in your life?",
        "How can you improve your focus and concentration while studying?",
        "In what ways has your understanding of the subject grown?",
        "How do you manage exam-related anxiety or nerves?",
        "What support or resources do you need for better exam preparation?",
        "How do you balance your study time with other responsibilities?",
        "What lessons have you learned from past exams?",
        "How can you create a conducive study environment?",
        "What are your expectations for the exam outcome?",
        "How do you handle setbacks or mistakes during your preparation?",
        "What motivates you to do well in this exam?",
        "How do you maintain your mental health during exam periods?",
        "What are some of your favorite family traditions?",
        "How do you feel after spending time with your family?",
        "What role do you play in your family dynamics?",
        "How can you strengthen your communication with family members?",
        "What are your most cherished memories with your family?",
        "How do your family values influence your decisions?",
        "What are some ways you show love and appreciation to your family?",
        "How do you handle disagreements or conflicts within your family?",
        "What aspects of your family life would you like to improve?",
        "How has your relationship with your family evolved over time?",
        "In what ways does your family support your goals and aspirations?",
        "What are your thoughts on maintaining work-life balance for family time?",
        "How do you contribute to your family's well-being?",
        "What are some challenges you face in your family life?",
        "How do you balance individuality with family expectations?",
        "What lessons have you learned from your family members?",
        "How do you navigate changes in family dynamics?",
        "What makes your family unique?",
        "How do you maintain strong bonds with family members who are far away?",
        "How does your family history shape who you are today?",
        "What steps are you taking to improve your physical health?",
        "How do you feel about your current health and fitness level?",
        "What are your health-related goals and how do you plan to achieve them?",
        "How does your daily routine contribute to your overall health?",
        "What are some healthy habits you are trying to develop?",
        "How do you balance nutrition, exercise, and rest in your life?",
        "What are your biggest challenges in maintaining a healthy lifestyle?",
        "How has your health journey affected other areas of your life?",
        "What motivates you to stay healthy and active?",
        "How do you handle setbacks in your health journey?",
        "What are some ways you can prioritize your mental health?",
        "How do you track your progress in terms of health and fitness?",
        "What are your thoughts on the relationship between mental and physical health?",
        "How do you stay informed about health and wellness?",
        "What changes have you noticed since focusing more on your health?",
        "How do you incorporate self-care into your health routine?",
        "What are some effective stress-reduction techniques you use?",
        "How do you balance health-related goals with other responsibilities?",
        "What are some health myths or misconceptions you've encountered?",
        "How do you stay motivated during challenging times in your health journey?",
        "What qualities do you value most in a romantic relationship?",
        "How do you express love and affection in your relationships?",
        "What are your thoughts on maintaining individuality while in a relationship?",
        "How do you handle conflicts or disagreements in your relationships?",
        "What are some ways you can strengthen your romantic relationship?",
        "How has love impacted your life and personal growth?",
        "What are your expectations and boundaries in a relationship?",
        "How do you nurture trust and understanding with your partner?",
        "What are some memorable moments you've shared with your loved one?",
        "How do you maintain a balance between love and other aspects of your life?",
        "What lessons have you learned from your past relationships?",
        "How do you prioritize communication and openness in your relationship?",
        "What are some challenges you've faced in your romantic life?",
        "How do you cultivate a healthy and supportive relationship?",
        "What role does romance play in your overall happiness?",
        "How do you deal with the vulnerability that comes with love?",
        "What are your thoughts on compromise and sacrifice in a relationship?",
        "How do you celebrate special occasions and milestones in your relationship?",
        "What are some ways you and your partner grow together?",
        "How do you handle the changing dynamics in a long-term relationship?",
        "What are your long-term goals and aspirations in life?",
        "How do you feel about the direction your life is taking?",
        "What are some lessons you've learned from your life experiences?",
        "How do you adapt to unexpected changes or challenges in life?",
        "What brings you the most joy and fulfillment in life?",
        "How do you align your daily actions with your life's purpose?",
        "What are some ways you can live a more meaningful and satisfying life?",
        "How do you balance your personal needs with the needs of others?",
        "What are your thoughts on finding happiness and contentment in life?",
        "How do you prioritize your time and resources in life?",
        "What aspects of your life are you currently working on improving?",
        "How do you manage stress and maintain mental well-being?",
        "What are some important values that guide your life decisions?",
        "How do you handle setbacks and failures in life?",
        "What are some ways you can contribute positively to the world?",
        "How do you nurture relationships that are important to you?",
        "What are your strategies for personal growth and development?",
        "How do you find balance between work, family, and personal interests?",
        "What are some experiences that have shaped who you are today?",
        "How do you plan to leave a positive legacy?",
        "What are you most thankful for in your life right now?",
        "How has expressing gratitude changed your perspective on life?",
        "What small things can you be more appreciative of?",
        "How do you practice gratitude in your daily routine?",
        "What are some ways you show gratitude to others?",
        "How does gratitude impact your mood and overall well-being?",
        "What are some challenges you've faced that you're now grateful for?",
        "How do you maintain a grateful mindset during difficult times?",
        "What lessons have you learned from the things you're grateful for?",
        "How do you express gratitude for your own abilities and qualities?",
        "What are some ways you can cultivate a culture of gratitude in your life?",
        "How do you remind yourself to be grateful in moments of dissatisfaction?",
        "What unexpected experiences have brought you gratitude?",
        "How do you balance the desire for more with appreciation for what you have?",
        "In what ways has gratitude improved your relationships?",
        "How can you show gratitude for challenges that have helped you grow?",
        "What aspects of your daily life deserve more gratitude?",
        "How do you think practicing gratitude can change your future outlook?",
        "What are some creative ways to express gratitude?",
        "How has gratitude influenced your decision-making and life choices?"
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