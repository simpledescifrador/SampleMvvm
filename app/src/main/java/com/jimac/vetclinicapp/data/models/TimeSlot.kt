package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName
import com.jimac.vetclinicapp.utils.AppUtils

data class TimeSlot(
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String
) {

    override fun toString(): String {
        return "${AppUtils.convertTime(startTime)} - ${AppUtils.convertTime(endTime)} "
    }
}