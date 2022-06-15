package se.umu.cs.peer0019.thirty.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import se.umu.cs.peer0019.thirty.R
import se.umu.cs.peer0019.thirty.model.Round
import se.umu.cs.peer0019.thirty.model.Thirty

class ScoreboardActivity : AppCompatActivity() {
    private lateinit var thirty: Thirty
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        val scoreboardText = findViewById<TextView>(R.id.scoreboard_text)
        intent.getParcelableExtra<Thirty>("thirty")
            ?.apply {
                thirty = this
            }
        val score = findViewById<TextView>(R.id.score)
        score.text = thirty.score.toString()

        var text = "Score per round:\n"
        thirty.rounds?.forEach {
            text = text + "${it.category}: ${it.score}\n" // todo
        }
        scoreboardText.text = text
    }
}