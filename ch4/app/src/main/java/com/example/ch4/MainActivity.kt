package com.example.ch4

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rg = findViewById<RadioGroup>(R.id.rg1)

        rg.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb1 -> {
                    println("student clicked!")
                    findViewById<MotionLayout>(R.id.motion_layout).transitionToStart()
                }
                R.id.rb2 -> {
                    println("worker clicked!")
                    findViewById<MotionLayout>(R.id.motion_layout).transitionToEnd()
                }
                else -> println("Noooooo!")
            }
        }
    }
}