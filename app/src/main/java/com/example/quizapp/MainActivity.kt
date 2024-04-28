package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        internal var cheatCount = 0
    }

    private lateinit var questionTextView: TextView
    private lateinit var questionNumberTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button

    private val questions = listOf(
        Question("Water boils at 100 degrees Celcius.", "True"),
        Question("The human body has four lungs.", "False"),
        Question("Apples are a type of fruit.", "True"),
        Question("The Earth orbits around the moon.", "False"),
        Question("Dolphins are mammals.", "True"),
        Question("All mammals lay eggs.", "False"),
        Question("Jupiter is the fifth planet from the sun.", "True"),
        Question("The moon shines its own light.", "False")
    )
    private var currentQuestionIndex = 0

    private var userAnswers = mutableListOf<String?>(null, null, null, null, null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionTextView = findViewById(R.id.questionTextView)
        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        cheatButton = findViewById(R.id.cheatButton)

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex", 0)
            userAnswers = savedInstanceState.getStringArrayList("userAnswers") as MutableList<String?>
            cheatCount = savedInstanceState.getInt("cheatCount", 0)
            updateQuestion()
            updateQuestionNumberText()
        } else {
            updateQuestion()
            updateQuestionNumberText()
        }

        trueButton.setOnClickListener {
            checkAnswer("True")
        }

        falseButton.setOnClickListener {
            checkAnswer("False")
        }

        nextButton.setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                updateQuestion()
                updateQuestionNumberText()
            } else {
                if (currentQuestionIndex == questions.size - 1) {
                    calculateScore()
                }
            }
        }

        prevButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                updateQuestion()
                updateQuestionNumberText()
            }
        }

        cheatButton.setOnClickListener {
            if (cheatCount < 2) {
                val intent = Intent(this, CheatActivity::class.java)
                val answer = questions[currentQuestionIndex].answer
                intent.putExtra("answer", answer)
                intent.putExtra("questionNumber", currentQuestionIndex + 1)
                intent.putExtra("totalQuestions", questions.size)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Maximum cheat usage reached: You have already used cheat 2 times.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentQuestionIndex", currentQuestionIndex)
        outState.putStringArrayList("userAnswers", ArrayList(userAnswers))
        outState.putInt("cheatCount", cheatCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentQuestionIndex = savedInstanceState.getInt("currentQuestionIndex")
        userAnswers = savedInstanceState.getStringArrayList("userAnswers") as MutableList<String?>
        cheatCount = savedInstanceState.getInt("cheatCount")
        updateQuestion()
        updateQuestionNumberText()
    }

    private fun updateQuestionNumberText() {
        val questionNumberText = "Question ${currentQuestionIndex + 1} of ${questions.size}"
        questionNumberTextView.text = questionNumberText
    }

    private fun updateQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        questionTextView.text = currentQuestion.question

        trueButton.setBackgroundColor(Color.BLUE)
        falseButton.setBackgroundColor(Color.BLUE)
        // Memeriksa apakah pengguna sudah menjawab pertanyaan sebelumnya, jika ya, mengubah warna tombol sesuai jawaban
        userAnswers[currentQuestionIndex]?.let {
            if (it == "True") {
                trueButton.setBackgroundColor(Color.GREEN)
            } else {
                falseButton.setBackgroundColor(Color.GREEN)
            }
        }

        if (currentQuestionIndex == questions.size - 1) {
            nextButton.text = getString(R.string.submit_button_text)
        } else {
            nextButton.text = getString(R.string.button_next_text)
        }

        prevButton.visibility = if (currentQuestionIndex == 0) View.GONE else View.VISIBLE
    }

    private fun checkAnswer(userAnswer: String) {
        // Memperbarui jawaban pengguna di list
        userAnswers[currentQuestionIndex] = userAnswer

        if (userAnswer == "True") {
            trueButton.setBackgroundColor(Color.GREEN)
            falseButton.setBackgroundColor(Color.BLUE)
        } else {
            trueButton.setBackgroundColor(Color.BLUE)
            falseButton.setBackgroundColor(Color.GREEN)
        }
    }

    private fun calculateScore() {

        var correctAnswers = 0
        for (i in questions.indices) {
            val userAnswer = userAnswers[i]
            val correctAnswer = questions[i].answer
            if (userAnswer != null && userAnswer == correctAnswer) {
                correctAnswers++
            }
        }

        val scorePercentage = (correctAnswers.toDouble() / questions.size.toDouble()) * 100

        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", scorePercentage)
        startActivity(intent)
        cheatCount = 0
    }
}

data class Question(val question: String, val answer: String)