package se.umu.cs.peer0019.thirty.controller

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import se.umu.cs.peer0019.thirty.R
import se.umu.cs.peer0019.thirty.model.Thirty

/*
todo:
 game logic
   pair dices at the end of each round
   game variables for points
   pick a grading option
 results screen
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
    private lateinit var dices: List<ImageButton>
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
    private val yellowDices = listOf(
        R.drawable.yellow1,
        R.drawable.yellow2,
        R.drawable.yellow3,
        R.drawable.yellow4,
        R.drawable.yellow5,
        R.drawable.yellow6
    )
    private val diceMap = mapOf(
        "dice1" to 0,
        "dice2" to 1,
        "dice3" to 2,
        "dice4" to 3,
        "dice5" to 4,
        "dice6" to 5
    )
    private lateinit var gradingButtons: MutableList<ToggleButton>

    private fun updateDice(btn: ImageButton, dice: Int) {
        val index = diceMap[btn.tag]
        index?.let {
            if (thirty.pickedDices.contains(it)) {
                btn.setImageResource(yellowDices[thirty.dices[index]!!-1])
            } else {
                btn.setImageResource(whiteDices[thirty.dices[index]!!-1])
            }
        }
    }

    private fun updateDices() {
        dices.forEachIndexed { index, imageButton ->
            thirty.dices[index]
                ?.let {
                    updateDice(imageButton, index)
                }
        }
    }

    private fun setTopMessage() {
        var text = ""
        if (thirty.round != null) {
            text = text+"Round \n"+thirty.round.toString()
        }
        if (thirty.currentThrow != null) {
            text = text+"\n"+
                    "Throw \n"+thirty.currentThrow.toString()
        }
        topMessage.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Dices on one row for landscape
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val dg = findViewById<GridLayout>(R.id.dice_grid)
            dg.columnCount = 6
            val gg = findViewById<GridLayout>(R.id.grading_grid)
            gg.columnCount = 5
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
        dices = listOf(
            dice1,
            dice2,
            dice3,
            dice4,
            dice5,
            dice6
        )

        throwButton = findViewById(R.id.throw_button)
        throwButton.setOnClickListener { view: View ->
            thirty.throwDice()
            updateDices()
            setTopMessage()
            if (thirty.stopped) {
                throwButton.isEnabled = false
            }
        }

        fun diceClickListener (btn: View) {
            val index = diceMap[btn.tag]
            index?.let {
                if (thirty.pickedDices.contains(it)) {
                    thirty.pickedDices.remove(it)
                } else {
                    thirty.pickedDices.add(it)
                }
            }

            updateDices()
        }

        gradingButtons = mutableListOf<ToggleButton>(
            findViewById(R.id.low),
            findViewById(R.id.four),
            findViewById(R.id.five),
            findViewById(R.id.six),
            findViewById(R.id.seven),
            findViewById(R.id.eight),
            findViewById(R.id.nine),
            findViewById(R.id.ten),
            findViewById(R.id.eleven),
            findViewById(R.id.twelve)
        )

        dices.forEach {
            it.setOnClickListener { it
                diceClickListener(it)
            }
        }

        fun gradingsClickListener (btn: ToggleButton) {
            gradingButtons.forEach {
                it.isChecked = false
            }
            btn.isChecked = true
        }

        gradingButtons.forEach {
            it.setOnClickListener { it as ToggleButton
                gradingsClickListener(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("thirty", thirty)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getParcelable<Thirty>("thirty")?.let {
            thirty = it
        }

        // Update the UI
        setTopMessage()
        updateDices()
    }
}