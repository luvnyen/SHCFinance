package com.example.shcfinance

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class transaction(
    var id : String,
    var description : String,
    var sum_of_money : Int,
    var type : String,
    var id_category : String,
    var date : String
) : Parcelable
