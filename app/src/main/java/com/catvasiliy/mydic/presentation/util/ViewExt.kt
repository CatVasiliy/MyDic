package com.catvasiliy.mydic.presentation.util

import android.view.View

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