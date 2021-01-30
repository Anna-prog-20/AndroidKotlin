package com.example.keepnotes.model

import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMMM.yy HH:mm"

enum class NameActivity {
    main,
    note,
    splash
}

fun Date.format(): String =
    SimpleDateFormat(DATE_TIME_FORMAT,Locale.getDefault()).format(this)