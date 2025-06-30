import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fragment.All
import com.example.fragment.Videoplayer_Fragment
import com.example.services.api.model.ItemsItem
import com.example.youtube_app.R
import com.example.youtube_app.databinding.ItemVideoBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class VideoAdapter(private val videos: List<ItemsItem?>, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemVideoBinding = ItemVideoBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)

    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        val snippet = video?.snippet
        val stats = video?.statistics
        val contentDetails = video?.contentDetails

        holder.binding.tvTitle.text = snippet?.title ?: "No Title"
        holder.binding.tvChannelName.text = snippet?.channelTitle ?: "Unknown Channel"
        holder.binding.viewsCount.text = formatViewCount(stats?.viewCount)
        holder.binding.uploadTime.text = formatPublishedTime(snippet?.publishedAt)

        holder.binding.movieDuration.text = formatDuration(contentDetails?.duration)

        Glide.with(holder.itemView.context)
            .load(snippet?.thumbnails?.medium?.url)
            .into(holder.binding.ivThumbnail)

        holder.binding.ivThumbnail.setOnClickListener {
            val fragment = Videoplayer_Fragment().apply {
                arguments = Bundle().apply {
                    putString("videoId", video?.id)
                    putString("title", snippet?.title)
                    putString("channelName", snippet?.channelTitle)
                    putString("viewCount", formatViewCount(stats?.viewCount))
                    putString("uploadTime", formatPublishedTime(snippet?.publishedAt))
                    putString("channelLogo", snippet?.thumbnails?.medium?.url)
                }
            }
            (holder.itemView.context as? AppCompatActivity)?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, fragment)
                ?.addToBackStack(null)
                ?.commit()
            (holder.itemView.context as? AppCompatActivity)?.findViewById<FrameLayout>(R.id.fragmentContainer)?.visibility = View.VISIBLE
        }

        Glide.with(holder.itemView.context)
            .load(snippet?.thumbnails?.medium?.url)
            .into(holder.binding.channelLogo)
    }

    override fun getItemCount(): Int = videos.size

    private fun formatViewCount(views: String?): String {
        if (views.isNullOrEmpty()) return "0 views"
        val count = views.toLongOrNull() ?: return "0 views"
        return when {
            count >= 1_000_000_000 -> String.format("%.1fB views", count / 1_000_000_000.0)
            count >= 1_000_000 -> String.format("%.1fM views", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK views", count / 1_000.0)
            else -> "$count views"
        }
    }

    private fun formatPublishedTime(publishedAt: String?): String {
        if (publishedAt.isNullOrEmpty()) return ""
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            val time = sdf.parse(publishedAt)?.time ?: return ""
            val now = System.currentTimeMillis()
            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
        } catch (e: ParseException) {
            ""
        }
    }

    private fun formatDuration(duration: String?): String {
        if (duration.isNullOrEmpty()) return ""

        val regex = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
        val matchResult = regex.find(duration) ?: return ""

        val hours = matchResult.groups[1]?.value?.toIntOrNull() ?: 0
        val minutes = matchResult.groups[2]?.value?.toIntOrNull() ?: 0
        val seconds = matchResult.groups[3]?.value?.toIntOrNull() ?: 0

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

}
