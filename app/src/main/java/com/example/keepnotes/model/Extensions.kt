package com.example.keepnotes.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.keepnotes.R
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMMM.yy HH:mm"

fun Date.format(): String =
    SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(this)

fun Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(context, getColorRes())

fun Color.getColorRes(): Int =
    when (this) {
        Color.BLUE -> R.color.color_blue
        Color.VIOLET -> R.color.color_violet
        Color.RED -> R.color.color_red
        Color.PINK -> R.color.color_pink
        Color.ORANGE -> R.color.color_orange
        Color.YELLOW -> R.color.color_yellow
        Color.LIME -> R.color.color_lime
        Color.GREEN -> R.color.color_green
        Color.WHITE -> R.color.color_white
        Color.GRAY -> R.color.color_gray
    }

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
