package com.example.news.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM news_table")
    fun getArticles(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)


}