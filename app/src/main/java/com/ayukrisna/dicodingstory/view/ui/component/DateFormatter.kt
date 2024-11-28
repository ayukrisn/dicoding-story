package com.ayukrisna.dicodingstory.view.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatTimestamp(timestamp: String): String {
    val parsedDate = ZonedDateTime.parse(timestamp)
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy, hh:mm a", Locale.getDefault())
    return parsedDate.format(formatter)
}