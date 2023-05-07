package com.todayrecord.todayrecord.util.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.todayrecord.todayrecord.util.toPx

class RecordsItemDecoration(
    private val top: Int,
    private val bottom: Int
    ): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val isFirst = parent.getChildAdapterPosition(view) == 0
        val isLast = parent.getChildAdapterPosition(view) == state.itemCount -1

        when {
            isFirst && isLast -> return
            isFirst -> outRect.bottom = this@RecordsItemDecoration.bottom.toPx(parent.context)
            isLast -> outRect.top = this@RecordsItemDecoration.top.toPx(parent.context)
            else -> {
                outRect.top = this@RecordsItemDecoration.top.toPx(parent.context)
                outRect.bottom = this@RecordsItemDecoration.bottom.toPx(parent.context)
            }
        }
    }
}