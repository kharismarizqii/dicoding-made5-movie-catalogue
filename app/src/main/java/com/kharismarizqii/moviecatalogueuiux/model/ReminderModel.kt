package com.kharismarizqii.moviecatalogueuiux.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReminderModel(
    var isReminded: Boolean = false
):Parcelable