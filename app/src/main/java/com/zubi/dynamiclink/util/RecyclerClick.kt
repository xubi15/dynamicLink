package com.zubi.dynamiclink.util

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.zubi.dynamiclink.interfaces.ClickListener

class RecyclerClick(
    val context: Context,
    val recyclerView: RecyclerView,
    val clickListener: ClickListener
) : RecyclerView.OnItemTouchListener {
    private var gestureDetector: GestureDetector? = null

    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null) {
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                }
                super.onLongPress(e)
            }
        })
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && gestureDetector!!.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }
}


