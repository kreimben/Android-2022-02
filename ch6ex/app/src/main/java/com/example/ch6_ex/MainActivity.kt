package com.example.ch6_ex

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

data class Owner(val login: String)
data class Repo(val name: String, val owner: Owner, val url: String)
data class Contributor(val login: String, val contributions: Int)

interface RestApi {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<Repo>
    /*
    [
        {
            "id": 74595421,
            "node_id": "MDEwOlJlcG9zaXRvcnk3NDU5NTQyMQ==",
            "name": "2ndProject",
            "full_name": "jyheo/2ndProject",
            "private": false,
            "owner": {
                "login": "jyheo",
                "id": 4907532,
                 ... 생략 ...
            },
            "html_url": "https://github.com/jyheo/2ndProject",
            "description": null,
            "fork": true,
            "url": "https://api.github.com/repos/jyheo/2ndProject",
            ... 생략 ...
        },
        {
            ... 생략 ...
        },
        ... 생략 ...
    ]
    */

    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun contributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<Contributor>
}

class MyViewModel : ViewModel() {
    private val baseURL = "https://api.github.com/"
    private lateinit var api: RestApi
    var username = ""
    var cache: MutableMap<String, String> = mutableMapOf()

    val response = MutableLiveData<String>()
    val query_results = MutableLiveData<String>()

    init {
        retrofitInit()
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                val repos = api.listRepos(username)
                response.value = StringBuilder().apply {
                    repos.forEach {
                        append(it.name)
                        append(" - ")
                        append(it.owner.login)
                        append("\n")
                    }
                }.toString()

                // Add to cache.
                cache[username] = response.value.toString()

                // Display that we got the message from internet!
                query_results.value = "INTERNET ${username} (${Date()})"
            } catch (e: Exception) {
                if (cache.getOrDefault(username, defaultValue = null) == null) {
                    // If data is not exists in cache,
                    response.value = "Failed to connect to the server"
                    query_results.value = "NOT FOUND ${username} (${Date()})"
                } else {
                    response.value = cache.get(username)
                    query_results.value = "CACHE ${username} (${Date()})"
                }
            }
        }
    }

    private fun retrofitInit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create())

            .build()

        api = retrofit.create(RestApi::class.java)
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.response.observe(this) {
            findViewById<TextView>(R.id.resultsView).text = it
        }
        myViewModel.query_results.observe(this) {
            findViewById<TextView>(R.id.queryResultsView).text = it
        }

        val te = findViewById<EditText>(R.id.username_textedit)
        findViewById<Button>(R.id.query_button).setOnClickListener {
            myViewModel.username = te.text.toString()
            myViewModel.refreshData()
        }

        val qr = findViewById<TextView>(R.id.queryResultsView)
        qr.setTextColor(Color.parseColor("#FF0000"))
    }
}