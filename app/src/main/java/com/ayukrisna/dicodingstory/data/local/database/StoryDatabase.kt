package com.ayukrisna.dicodingstory.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayukrisna.dicodingstory.data.local.dao.RemoteKeysDao
import com.ayukrisna.dicodingstory.data.local.dao.StoryDao
import com.ayukrisna.dicodingstory.data.local.entity.RemoteKeys
import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}