package com.example.news.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.news.util.Constants.Companion.NEWS_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = NEWS_TABLE)
@Parcelize
data class Article(
    val id: String?,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    @Embedded
    val source: Source?,
    @PrimaryKey
    val title: String,
    val url: String,
    val urlToImage: String
) : Parcelable {
    @Parcelize
    data class Source(
        val name: String?
    ) : Parcelable
}

