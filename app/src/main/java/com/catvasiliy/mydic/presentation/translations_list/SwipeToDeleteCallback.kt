package com.catvasiliy.mydic.presentation.translations_list

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.catvasiliy.mydic.R

abstract class SwipeToDeleteCallback(context: Context)
    : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.deleteBackground))
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.baseline_delete_36)
    private val iconCover = ColorDrawable(ContextCompat.getColor(context, R.color.surface))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (deleteIcon == null) {
            return
        }

        val itemView = viewHolder.itemView
        val currentRight = itemView.right + dX.toInt()

        if (dX < 0) {
            background.setBounds(currentRight, itemView.top, itemView.right, itemView.bottom)
        } else {
            background.setBounds(0, 0, 0, 0)
        }

        val deleteIconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - deleteIcon.intrinsicWidth
        val deleteIconTop = itemView.top + deleteIconMargin
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + deleteIcon.intrinsicHeight

        if (currentRight in deleteIconLeft..deleteIconRight) {
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            iconCover.setBounds(deleteIconLeft, itemView.top, currentRight, itemView.bottom)
        } else if (currentRight > deleteIconRight) {
            deleteIcon.setBounds(0, 0, 0, 0)
            iconCover.setBounds(0, 0, 0, 0)
        } else {
            iconCover.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        deleteIcon.draw(c)
        iconCover.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}