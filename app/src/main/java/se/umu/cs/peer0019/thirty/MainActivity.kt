package se.umu.cs.peer0019.thirty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

// TODO: saveInstanceState

class MainActivity : AppCompatActivity() {
    private lateinit var gradingSetting: Spinner

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
    }
}