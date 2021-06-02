package com.teenwolf3301.to_do_list.ui.list

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.teenwolf3301.to_do_list.util.getDefaultMargin
import com.teenwolf3301.to_do_list.util.getTextColorTertiary
import com.teenwolf3301.to_do_list.util.getTextDefaultSize


class ListHeader(private val position: Int?, private val text: String) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint()


    override fun onDrawOver(c: Canvas, parent: RecyclerView) {
        paint.color = getTextColorTertiary()
        paint.textSize = getTextDefaultSize()

        parent.children.forEach {
            if (position == parent.getChildAdapterPosition(it)) {
                c.drawText(
                    text,
                    it.left + getDefaultMargin(),
                    it.top - getDefaultMargin(),
                    paint
                )
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (position == itemPosition) {
            outRect.set(0, 100, 0, 0)
        }
    }
}