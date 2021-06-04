package com.teenwolf3301.to_do_list.ui.list

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.util.getDefaultMargin
import com.teenwolf3301.to_do_list.util.getTextColorTertiary
import com.teenwolf3301.to_do_list.util.getTextDefaultSize

class ListHeader(private val viewType: Int, private val text: String) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = getTextColorTertiary()
        paint.textSize = getTextDefaultSize()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView) {

        parent.children.firstOrNull {
            parent.adapter?.getItemViewType(parent.getChildAdapterPosition(it)) == viewType
        }.apply {
            if (this != null) {
                c.drawText(
                    text,
                    this.left + getDefaultMargin(),
                    this.top - getDefaultMargin(),
                    paint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val first = parent.children.firstOrNull {
            parent.adapter?.getItemViewType(parent.getChildAdapterPosition(it)) == viewType
        }
        if (first != null)
            if (parent.getChildAdapterPosition(view) == parent.getChildAdapterPosition(first))
                outRect.top = 100
    }
}