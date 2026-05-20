package com.monakom.readyappclone.ui.home

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat

class HomeTabManager(
    private val container: ViewGroup,
    private val context: Context,
    private val onTabSelected: (String?) -> Unit
) {

    private var selectedIndex = 0

    fun buildTabs(types: List<String>) {

        container.removeAllViews()

        val allTypes = listOf("All") + types

        allTypes.forEachIndexed { index, type ->

            val tab = TextView(context).apply {
                text = type
                textSize = 13f
                setPadding(40, 16, 40, 16)

                setOnClickListener {
                    selectedIndex = index
                    onTabSelected(if (type == "All") null else type)
                    updateUI()
                }
            }

            container.addView(tab)
        }
        updateUI()
    }

    private fun updateUI() {
        for (i in 0 until container.childCount) {

            val child = container.getChildAt(i) as TextView

            val isSelected = i == selectedIndex

            child.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isSelected) android.R.color.white
                    else android.R.color.darker_gray
                )
            )
        }
    }
}