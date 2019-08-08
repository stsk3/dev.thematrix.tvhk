package dev.thematrix.tvhk

import java.io.Serializable

data class Movie(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var cardImageUrl: Int = 0,
    var videoUrl: String = "",
    var func: String = "",
    var exo: Boolean = false
) : Serializable {



    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }

    override fun toString(): String {
        return "Movie(id=$id, title='$title', description='$description', cardImageUrl=$cardImageUrl, videoUrl='$videoUrl', func='$func', exo=$exo)"
    }
}
