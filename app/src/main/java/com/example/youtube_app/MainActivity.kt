package com.example.youtube_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeapp.adapter.VideoAdapter
import com.example.youtubeapp.api.YoutubeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var searchBar: EditText
    private lateinit var rvVideos: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private var searchJob: Job? = null

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeApi::class.java)
//        https://www.googleapis.com/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBar = findViewById(R.id.search_bar)
        rvVideos = findViewById(R.id.rvVideos)

        videoAdapter = VideoAdapter()
        rvVideos.adapter = videoAdapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    s?.toString()?.let { query ->
                        if (query.isNotEmpty()) {
                            searchVideos(query)
                        }
                    }
                }
            }
        })

        searchVideos("")
    }

    private fun searchVideos(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = api.getMostPopularVideos(query = query)
                if (response.isSuccessful) {
                    response.body()?.items?.let { videos ->
                        videoAdapter.setVideos(videos)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
