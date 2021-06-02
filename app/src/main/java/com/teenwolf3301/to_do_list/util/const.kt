package com.teenwolf3301.to_do_list.util

import android.R.attr
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.ui.MainActivity

lateinit var APP_ACTIVITY: MainActivity

const val VIEW_COMPLETED = 0
const val VIEW_UNCOMPLETED = 1

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

fun getTextDefaultSize() = APP_ACTIVITY.resources.getDimension(R.dimen.default_text)

fun getDefaultMargin() = APP_ACTIVITY.resources.getDimension(R.dimen.default_margin)

fun RecyclerView.removeItemDecorations() {
    while (this.itemDecorationCount > 0)
        this.removeItemDecorationAt(0)
}