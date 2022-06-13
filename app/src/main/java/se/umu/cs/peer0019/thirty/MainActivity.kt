package se.umu.cs.peer0019.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

/*
todo:
 saveInstanceState
 design
 game logic
   more textviews with round info
   rounds
   disable spinner after first round
 */

class MainActivity : AppCompatActivity() {
    private lateinit var gradingSetting: Spinner
    private lateinit var throwButton: Button
    private lateinit var thirty: Thirty
    private lateinit var topMessage: TextView
    private lateinit var dice1: ImageButton
    private lateinit var dice2: ImageButton
    private lateinit var dice3: ImageButton
    private lateinit var dice4: ImageButton
    private lateinit var dice5: ImageButton
    private lateinit var dice6: ImageButton
    private val whiteDices = listOf(
        R.drawable.white1,
        R.drawable.white2,
        R.drawable.white3,
        R.drawable.white4,
        R.drawable.white5,
        R.drawable.white6
    )
    private val redDices = listOf(
        R.drawable.red1,
        R.drawable.red2,
        R.drawable.red3,
        R.drawable.red4,
        R.drawable.red5,
        R.drawable.red6
    )

    private fun setDiceFace(n: Int, color: String = "white") {

    }

    private fun updateDices() {
        // TODO: Do not use !!
        dice1.setImageResource(whiteDices[thirty.dice1!! - 1])
        dice2.setImageResource(whiteDices[thirty.dice2!! - 1])
        dice3.setImageResource(whiteDices[thirty.dice3!! - 1])
        dice4.setImageResource(whiteDices[thirty.dice4!! - 1])
        dice5.setImageResource(whiteDices[thirty.dice5!! - 1])
        dice6.setImageResource(whiteDices[thirty.dice6!! - 1])
    }

    private fun setTopMessage() {
        var text = "Round \n"+thirty.round.toString()
        if (thirty.currentThrow != null) {
            text = text+"\n"+
                    "Throw \n"+thirty.currentThrow.toString()
        }
        topMessage.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gradingSetting = findViewById(R.id.grading_setting)

        ArrayAdapter.createFromResource(
            this,
            R.array.grading_settings,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            gradingSetting.adapter = adapter
        }

        topMessage = findViewById(R.id.top_message)

        thirty = Thirty()
        thirty.startGame()
        setTopMessage()

        dice1 = findViewById(R.id.dice1)
        dice2 = findViewById(R.id.dice2)
        dice3 = findViewById(R.id.dice3)
        dice4 = findViewById(R.id.dice4)
        dice5 = findViewById(R.id.dice5)
        dice6 = findViewById(R.id.dice6)

        throwButton = findViewById(R.id.throw_button)
        throwButton.setOnClickListener { view: View ->
            thirty.throwDice()
            updateDices()
            setTopMessage()
            if (thirty.stopped) {
                throwButton.isEnabled = false
            }
        }

        //todo: gradingSetting.isEnabled = false
    }
}