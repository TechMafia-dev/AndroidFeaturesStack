package com.utility.feature_expandable_list_view

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity


class CustomListView : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_list_view)

        val relativeLayoutContainer: RelativeLayout = findViewById(R.id.custom_parent_view)

        ListContent.promptFileRead(applicationContext)
        val data_new: List<ListContent.Row> = ListContent.data_row_format

        // setup expandable list view
        var customView: ListContent.CustomExpandableList =
            ListContent.CustomExpandableList(applicationContext, relativeLayoutContainer, data_new)
        customView.populateViews()
    }

}