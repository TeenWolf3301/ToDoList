package com.teenwolf3301.to_do_list.util

import android.R.attr
import android.util.TypedValue
import com.teenwolf3301.to_do_list.ui.MainActivity

lateinit var APP_ACTIVITY: MainActivity

fun getTextColorTertiary(): Int {
    val textColorTertiary = TypedValue()
    APP_ACTIVITY.theme.resolveAttribute(attr.textColorTertiary, textColorTertiary, true)
    return textColorTertiary.data
}

fun getTextColorSecondary(): Int {
    val textColorSecondary = TypedValue()
    APP_ACTIVITY.theme.resolveAttribute(attr.textColorSecondary, textColorSecondary, true)
    return textColorSecondary.data
}