package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView

class CheatActivity : AppCompatActivity() {

    private lateinit var questionNumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        questionNumberTextView = findViewById(R.id.questionNumberTextView)
        val questionNumber = intent.getIntExtra("questionNumber", 1)
        val totalQuestions = intent.getIntExtra("totalQuestions", 1)
        val questionNumberText = "Question $questionNumber of $totalQuestions"
        questionNumberTextView.text = questionNumberText

        val showAnswerButton: Button = findViewById(R.id.showAnswerButton)

        showAnswerButton.setOnClickListener {
            // Mengambil jawaban dari intent yang dikirimkan dari MainActivity
            val answer = intent.getStringExtra("answer")
            // Menampilkan jawaban menggunakan Toast
            Toast.makeText(this, "Answer: $answer", Toast.LENGTH_SHORT).show()
            MainActivity.cheatCount++
            finish()
        }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Kembali ke MainActivity
        }
    }
}