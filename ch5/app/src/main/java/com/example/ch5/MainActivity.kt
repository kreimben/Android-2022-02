package com.example.ch5

import ClassInfo
import Enrollment
import MyDAO
import Student
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ch5.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDao: MyDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDao = MyDatabase.getDatabase(this).getMyDao()

        CoroutineScope(Dispatchers.IO).launch {
            with(myDao) {
                insertStudent(Student(1, "james"))
                insertStudent(Student(2, "john"))
            }
        }

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            binding.textView.text = str
        }

        binding.insert.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    insertStudent(Student(id.toInt(), ""))
                }
            }
        }

        binding.delete.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            val name = binding.editTextTextPersonName2.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    deleteStudent(Student(id.toInt(), name))
                }
            }
        }
    }
}