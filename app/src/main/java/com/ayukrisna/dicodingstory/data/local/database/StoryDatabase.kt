package com.ayukrisna.dicodingstory.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayukrisna.dicodingstory.data.local.dao.StoryDao
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

}