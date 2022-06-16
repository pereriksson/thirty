package se.umu.cs.peer0019.thirty.controller

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import se.umu.cs.peer0019.thirty.R
import se.umu.cs.peer0019.thirty.model.Dice
import se.umu.cs.peer0019.thirty.model.Round
import se.umu.cs.peer0019.thirty.model.Thirty

class MainActivity : AppCompatActivity() {
    private lateinit var throwButton: Button
    private lateinit var nextRoundButton: Button
    private lateinit var thirty: Thirty
    private lateinit var topMessage: TextView
    private lateinit var title: TextView
    private lateinit var instructions: TextView
    private lateinit var gradingGrid: GridLayout
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
    private lateinit var categoryButtons: MutableList<ToggleButton>

    /**
     * Updates the dice faces in the UI depending on game
     * mode and the picked state for each dice.
     */
    private fun updateDice(btn: ImageButton, index: Int) {
        index.let {
            val dice = thirty.dices[index]

            dice.value?.let {
                if (thirty.isThrowing && dice.picked) {
                    btn.setImageResource(yellowDices[it - 1])
                } else if (thirty.isGrading && dice.picked) {
                    btn.setImageResource(redDices[it - 1])
                } else {
                    btn.setImageResource(whiteDices[it - 1])
                }
            }
        }
    }

    /**
     * Convenience method to update the faces of all dices in the UI.
     */
    private fun updateDices() {
        dices.forEachIndexed { index, imageButton ->
            updateDice(imageButton, index)
        }
    }

    /**
     * Compile the message shown to the user.
     */
    private fun setTopMessage() {
        val msg = mutableListOf<String>()

        if (thirty.round != null) msg.add("Round: ${thirty.round.toString()}")
        if (thirty.currentThrow != null) msg.add("Throw: ${thirty.currentThrow.toString()}")
        topMessage.text = msg.joinToString(" ")

        instructions.text = ""

        if (thirty.isGrading) {
            title.text = getString(R.string.grading_title)
            instructions.text = getString(R.string.grading_instructions)
        }
        if (thirty.isThrowing) {
            title.text = getString(R.string.throwing_title)
        }
    }

    /**
     * Convenience method to make sure all UI components
     * are coherent with game state.
     */
    private fun updateUI() {
        updateDices()
        setTopMessage()
        if (thirty.isGrading) {
            throwButton.isEnabled = false
            gradingGrid.visibility = View.VISIBLE
            nextRoundButton.isEnabled = true
        }
        if (thirty.isThrowing) {
            throwButton.isEnabled = true
            gradingGrid.visibility = View.GONE
            nextRoundButton.isEnabled = false
        }
    }

    /**
     * Lifecycle method to create a new game, create references
     * to view components and register listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Dices on one row
            val dg = findViewById<GridLayout>(R.id.dice_grid)
            dg.columnCount = 6
            // Categories on two rows
            val gg = findViewById<GridLayout>(R.id.category_grid)
            gg.columnCount = 5
        }

        topMessage = findViewById(R.id.top_message)
        title = findViewById(R.id.title)
        instructions = findViewById(R.id.instructions)
        gradingGrid = findViewById(R.id.category_grid)
        nextRoundButton = findViewById(R.id.next_round_button)

        // Create the game, it will be restored from state if available
        thirty = Thirty(
            mutableListOf<Round>(),
            0,
            false,
            false,
            null,
            10,
            null
        )
        thirty.startGame()
        thirty.dices.add(Dice(null, false))
        thirty.dices.add(Dice(null, false))
        thirty.dices.add(Dice(null, false))
        thirty.dices.add(Dice(null, false))
        thirty.dices.add(Dice(null, false))
        thirty.dices.add(Dice(null, false))
        setTopMessage()

        dices = listOf(
            findViewById(R.id.dice1),
            findViewById(R.id.dice2),
            findViewById(R.id.dice3),
            findViewById(R.id.dice4),
            findViewById(R.id.dice5),
            findViewById(R.id.dice6)
        )

        throwButton = findViewById(R.id.throw_button)
        throwButton.setOnClickListener {
            thirty.throwDice()
            updateUI()
        }

        nextRoundButton.setOnClickListener {
            nextRound(it)
        }

        categoryButtons = mutableListOf(
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

        dices.forEach { dice ->
            dice.setOnClickListener {
                clickDice(it)
            }
        }

        categoryButtons.forEach {
            it.setOnClickListener { it as ToggleButton
                toggleCategory(it)
            }
        }
    }

    /**
     * Prepare the UI for next round.
     */
    fun nextRound (btn: View) {
        // Find our which category the user has selected
        var category = ""
        categoryButtons.forEach {
            if (it.isChecked) {
                // Pick the category
                category = it.tag.toString()
            }
        }

        // The user hasn't selected a category
        if (category == "") return

        if (thirty.round == thirty.totalRounds) {
            // This is last round
            if (!thirty.saveRound(category)) return
            // Send to Scoreboard
            Intent(this, ScoreboardActivity::class.java)
                .apply {
                    this.putExtra("thirty", thirty)
                    startActivity(this)
                }
        } else {
            // Save and move to next round
            if (!thirty.saveRound(category)) return
            thirty.nextRound()
            updateUI()
            resetDices()
            // This dice is now used
            categoryButtons.forEach {
                if (it.isChecked) {
                    it.isEnabled = false
                    it.isChecked = false
                }
            }
        }
    }

    /**
     * Reset the faces of all dices.
     */
    fun resetDices() {
        dices.forEach {
            it.setImageDrawable(null)
        }
    }

    /**
     * Event listener to select a dice as picked/unpicked.
     */
    fun clickDice (btn: View) {
        val index = btn.tag.toString().toInt()
        val dice = thirty.dices[index]
        dice.picked = !dice.picked
        updateDices()
    }

    /**
     * Event listener to select a certain category.
     */
    fun toggleCategory (btn: ToggleButton) {
        categoryButtons.forEach {
            it.isChecked = false
        }
        btn.isChecked = true
    }

    /**
     * Store the activity state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Store the game instance in our state bundle.
        outState.putParcelable("thirty", thirty)
    }

    /**
     * Restore the activity state.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the game, if available
        savedInstanceState.getParcelable<Thirty>("thirty")?.let {
            thirty = it
        }

        // Keep the UI in sync with game
        updateUI()
    }
}