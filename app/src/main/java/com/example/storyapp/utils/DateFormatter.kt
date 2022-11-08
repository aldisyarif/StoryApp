package com.example.storyapp.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {

    fun formatDate(currentDateString: String, targetTieZone: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTieZone))
        return formatter.format(instant) //"02 Feb 2022 | 17:10"
    }
}