package se.umu.cs.peer0019.thirty.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import se.umu.cs.peer0019.thirty.R
import se.umu.cs.peer0019.thirty.model.Dice
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
        thirty.rounds.forEach {
            text += "${it.category}: ${it.score}\n"
        }
        scoreboardText.text = text
    }

    override fun onBackPressed() {
        // Reset the game
        val intent = Intent(this, MainActivity::class.java).apply {
            thirty = Thirty(
                mutableListOf<Round>(),
                0,
                false,
                false,
                null,
                10,
                null,
                mutableListOf<String>(
                    "low",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12"
                ),
                listOf<Dice>(
                    Dice(null, false),
                    Dice(null, false),
                    Dice(null, false),
                    Dice(null, false),
                    Dice(null, false),
                    Dice(null, false)
                )
            )
            thirty.startGame()
            this.putExtra("thirty", thirty)
            startActivity(this)
        }
    }
}