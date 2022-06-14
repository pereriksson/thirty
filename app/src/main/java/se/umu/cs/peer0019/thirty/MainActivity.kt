package se.umu.cs.peer0019.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.math.round

/*
todo:
 saveInstanceState
 design
 game logic
   more textviews with round info
   rounds
   disable spinner after first round
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

    private fun setDiceFace(n: Int, color: String = "white") {

    }

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
        // TODO: The app crashes when clicking the dices before first throw
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
        //todo: gradingSetting.isEnabled = false
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
            // todo: the game might say "No" when trying to pick
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

        dices.forEach {
            it.setOnClickListener { it
                diceClickListener(it)
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
    }
}