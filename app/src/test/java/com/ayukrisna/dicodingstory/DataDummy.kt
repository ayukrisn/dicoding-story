package com.ayukrisna.dicodingstory

import com.ayukrisna.dicodingstory.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "Story Name $i",
                description = "This is a description for story $i.",
                photoUrl = "https://example.com/photo_$i.jpg",
                createdAt = "2024-01-01T12:00:00Z",
                lat = -6.2 + i * 0.01,
                lon = 106.8 + i * 0.01
            )
            items.add(story)
        }
        return items
    }
}
