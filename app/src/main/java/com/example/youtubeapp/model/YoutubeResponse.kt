data class YoutubeResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: PageInfo,
    val items: List<VideoItem>
)

data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)

data class VideoItem(
    val kind: String,
    val etag: String,
    val id: VideoId,
    val snippet: VideoSnippet
)

data class VideoId(
    val kind: String,
    val videoId: String
)

data class VideoSnippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

data class Thumbnails(
    val default: ThumbnailDetail,
    val medium: ThumbnailDetail,
    val high: ThumbnailDetail
)

data class ThumbnailDetail(
    val url: String,
    val width: Int,
    val height: Int
)
