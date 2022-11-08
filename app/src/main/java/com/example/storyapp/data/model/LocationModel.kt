package com.example.storyapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?
): Parcelable
