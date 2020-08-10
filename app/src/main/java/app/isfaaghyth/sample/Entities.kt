package app.isfaaghyth.sample

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    val popularArtists: ArtsyResponse? = null
)

data class ArtsyResponse(
    @SerializedName("popular_artists")
    val popularArtists: PopularArtists? = null
)

data class PopularArtists(
    @SerializedName("artists")
    val artists: List<Artist> = emptyList()
)

data class Artist(
    @SerializedName("name")
    val name: String
)