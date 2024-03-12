package com.catvasiliy.mydic.presentation.util

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.core.view.children

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun View.showIf(condition: View.() -> Boolean) {
    if (condition()) {
        show()
    } else {
        hide()
    }
}

inline fun View.visibleIf(condition: View.() -> Boolean) {
    if (condition()) {
        show()
    } else {
        invisible()
    }
}

fun View.hideAndShowOther(otherView: View) {
    this.hide()
    otherView.show()
}

fun RadioGroup.checkWithTag(@IdRes radioButtonId: Int) {
    tag = radioButtonId
    val radioButton = children.find { it.id == radioButtonId } as RadioButton
    radioButton.isChecked = true
}

fun Spinner.setSelectionWithTag(position: Int) {
    tag = position
    setSelection(position)
}