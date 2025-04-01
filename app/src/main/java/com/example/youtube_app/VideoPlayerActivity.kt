package com.example.youtube_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.youtube_app.databinding.ActivityVideoPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    companion object {
        private const val EXTRA_VIDEO_ID = "video_id"
        private const val EXTRA_VIDEO_TITLE = "video_title"

        fun createIntent(context: Context, videoId: String, videoTitle: String): Intent {
            return Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_ID, videoId)
                putExtra(EXTRA_VIDEO_TITLE, videoTitle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoId = intent.getStringExtra(EXTRA_VIDEO_ID) ?: return finish()
        val videoTitle = intent.getStringExtra(EXTRA_VIDEO_TITLE) ?: ""

        binding.tvTitle.text = videoTitle

        // Add the player as a lifecycle observer
        lifecycle.addObserver(binding.youtubePlayerView)

        // Set up the player
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }
}
