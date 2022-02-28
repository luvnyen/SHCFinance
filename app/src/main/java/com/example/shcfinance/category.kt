package com.example.shcfinance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class category(
    var id : String,
    var name : String
) : Parcelable
