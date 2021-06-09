package com.teenwolf3301.to_do_list.util

import android.util.TypedValue
import androidx.appcompat.widget.SearchView
import com.teenwolf3301.to_do_list.R
import com.teenwolf3301.to_do_list.ui.MainActivity

lateinit var APP_ACTIVITY: MainActivity

const val VIEW_UNCOMPLETED = 1000
const val VIEW_COMPLETED = 1001
const val ADD_ITEM_RESULT_OK = 10
const val UPDATE_ITEM_RESULT_OK = 11
const val DELETE_ITEM_RESULT_OK = 12

fun getTextColorTertiary(): Int {
    val textColorTertiary = TypedValue()
    APP_ACTIVITY.theme.resolveAttribute(R.attr.textColorTertiary, textColorTertiary, true)
    return textColorTertiary.data
}

fun getTextColorSecondary(): Int {
    val textColorSecondary = TypedValue()
    APP_ACTIVITY.theme.resolveAttribute(R.attr.textColorSecondary, textColorSecondary, true)
    return textColorSecondary.data
}

fun getTextDefaultSize() = APP_ACTIVITY.resources.getDimension(R.dimen.default_text)

fun getDefaultMargin() = APP_ACTIVITY.resources.getDimension(R.dimen.default_margin)

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })

}