package com.example.youtubeapp.adapter

import VideoItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtube_app.VideoPlayerActivity
import com.example.youtube_app.databinding.ItemVideoBinding

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    private var videos = mutableListOf<VideoItem>()

    fun setVideos(newVideos: List<VideoItem>) {
        videos.clear()
        videos.addAll(newVideos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount() = videos.size

    class VideoViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoItem) {
            binding.apply {
                tvTitle.text = video.snippet.title
                tvChannelName.text = video.snippet.channelTitle
                Glide.with(root.context)
                    .load(video.snippet.thumbnails.high.url)
                    .into(ivThumbnail)

                root.setOnClickListener {
                    val context = root.context
                    val intent = VideoPlayerActivity.createIntent(
                        context,
                        video.id.videoId,
                        video.snippet.title
                    )
                    context.startActivity(intent)
                }
            }
        }
    }
}
