package com.example.ch2

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<MyView>(R.id.view2)
        val rectButton = findViewById<RadioButton>(R.id.rectangleButton)
        val circleButton = findViewById<RadioButton>(R.id.circleButton)

        rectButton.setOnClickListener {
            view.drawing_mode = "rect"
        }

        circleButton.setOnClickListener {
            view.drawing_mode = "circle"
        }
    }
}