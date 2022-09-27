package com.example.ch5

import android.os.Bundle
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

                insertClass(ClassInfo(1, "c-lang", "2020-10-01", "309", 1))
            }
        }

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                append("Student List\n")
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            binding.textView.text = str
        }

        binding.queryButton.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            val name = binding.editTextTextPersonName2.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    val name = binding.editTextTextPersonName2.text.toString()
                    getStudentByName(name)
                }
            }
        }

        binding.addButton.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            val name = binding.editTextTextPersonName2.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    insertStudent(Student(id.toInt(), name))
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    deleteStudent(Student(id.toInt(), ""))
                }
            }
        }

        binding.enrollmentButton.setOnClickListener {
            val id = binding.editTextTextPersonName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                with(myDao) {
                    insertEnrollment(Enrollment(id.toInt(), 1, "A"))
                }
            }
        }
    }
}