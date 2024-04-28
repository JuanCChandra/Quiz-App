package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val restartButton: Button = findViewById(R.id.restartButton)

        val score = intent.getDoubleExtra("score", 0.0)
        val message = "Your score: $score%"
        scoreTextView.text = message

        restartButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}