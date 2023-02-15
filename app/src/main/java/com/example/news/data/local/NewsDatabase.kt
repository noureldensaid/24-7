package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.models.Article
import com.example.news.data.local.dao.NewsDao


@Database(entities = [Article::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

}