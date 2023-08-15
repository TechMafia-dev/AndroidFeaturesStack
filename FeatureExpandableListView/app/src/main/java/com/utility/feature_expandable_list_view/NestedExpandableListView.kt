package com.utility.feature_expandable_list_view

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListView

class NestedExpandableListView(var con: Context, attributeSet: AttributeSet): ExpandableListView(con, attributeSet)
{
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val int = Int.MAX_VALUE.shr(2)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(int, MeasureSpec.AT_MOST))
    }

}