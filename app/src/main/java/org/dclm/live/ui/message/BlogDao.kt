package org.dclm.live.ui.message

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface BlogDao {

    @Insert
    suspend fun insert(blog: Blog)

    @Update
    suspend fun update(blog: Blog)

    @Query("SELECT * FROM blog ORDER BY id ASC")
    fun getAllBlogs(): LiveData<MutableList<Blog>>

    @Query("SELECT COUNT(*) from blog")
   suspend fun countDataBase (): Long
}