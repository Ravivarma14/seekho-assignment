package com.rvcn.seekhoanimeapplication.utils

import android.net.Uri
import com.rvcn.seekhoanimeapplication.data.remote.model.TrailerDto

object YoutubeUtil {

    fun extractVideoId(trailer: TrailerDto?): String? {

        trailer?.youtubeId?.let { return it }

        trailer?.url?.let { url ->
            return try {
                Uri.parse(url).getQueryParameter("v")
            } catch (e: Exception) {
                null
            }
        }

        trailer?.embedUrl?.let { embed ->
            return embed.substringAfterLast("/")
        }

        return null
    }
}
