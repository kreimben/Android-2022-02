package com.example.ch7ex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import java.util.concurrent.TimeUnit

// The repository pattern is a strategy for abstracting data access.
// ViewModel delegates the data-fetching process to the repository.

class MainActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startWorker).setOnClickListener { startWorker() }
        findViewById<Button>(R.id.stopWorker).setOnClickListener { stopWorker() }
        val recycler = findViewById<RecyclerView>(R.id.resultsView)
        recycler.setHasFixedSize(true)

        myViewModel =
            ViewModelProvider(this, MyViewModel.Factory(this)).get(MyViewModel::class.java)

        myViewModel.repos.observe(this) { repos ->
            val adapter = MyAdapter(this, repos)
            println("repos: ${repos}")
            recycler.adapter = adapter
        }

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(MyWorker.name)
            .observe(this) { workInfo ->
                if (workInfo.isNotEmpty()) {
                    when (workInfo[0].state) {
                        WorkInfo.State.ENQUEUED -> println("Worker enqueued!")
                        WorkInfo.State.RUNNING -> println("Worker running!")
                        WorkInfo.State.SUCCEEDED -> println("Worker succeeded!")  // only for one time worker
                        WorkInfo.State.CANCELLED -> println("Worker cancelled!")
                        else -> println(workInfo[0].state)
                    }
                }
            }
    }

    private fun startWorker() {
        //val oneTimeRequest = OneTimeWorkRequest.Builder<MyWorker>()
        //        .build()

        println("Starting worker")

        val constraints = Constraints.Builder().apply {
            setRequiredNetworkType(NetworkType.UNMETERED) // un-metered network such as WiFi
            setRequiresBatteryNotLow(true)
            //setRequiresCharging(true)
            // setRequiresDeviceIdle(true) // android 6.0(M) or higher
        }.build()

        val username: String = findViewById<EditText>(R.id.username).text.toString()

        //val repeatingRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
        val repeatingRequest = PeriodicWorkRequestBuilder<MyWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(workDataOf("username" to username))
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            MyWorker.name,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )

        println("workermanager: ${WorkManager.getInstance(this)}")
    }

    private fun stopWorker() {
        // to stop the MyWorker
        println("Stopping worker")
        WorkManager.getInstance(this).cancelUniqueWork(MyWorker.name)
    }
}