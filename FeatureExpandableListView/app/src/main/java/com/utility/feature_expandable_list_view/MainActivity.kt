package com.utility.feature_expandable_list_view

import android.app.ActionBar
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.utility.feature_expandable_list_view.ExpandableListData.data
class MainActivity : ComponentActivity() {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    private var expandableListViewNew: ExpandableListView? = null
    private var adapter_new: ExpandableListAdapter? = null
    private var adapter_new_v2: ExpandableListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"
        expandableListView = findViewById(R.id.expendableList)
        populateList(expandableListView)

        expandableListViewNew = findViewById(R.id.expendableListCustom)
//        populateListNew(expandableListViewNew)
        populateListNewV2(expandableListViewNew)
    }

    private fun populateListNewV2(expandableList: ExpandableListView?) {
        if (expandableList != null) {
            expandableList.layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT

            ListContent.promptFileRead(applicationContext)
            val data_new: List<ListContent.NestedFormatList> = ListContent.data_nested_format

            val listData = data
            adapter_new_v2 = CustomExpandableListAdapterNewV2(this, data_new[0].child!!, data_new[0].content!!)
            expandableList!!.setAdapter(adapter_new_v2)

            expandableList!!.setOnGroupExpandListener {
                expandableList.layoutParams.height = LayoutParams.MATCH_PARENT
            }
        }
    }
    private fun populateListNew(expandableList: ExpandableListView?) {
        if (expandableList != null) {
            expandableList.layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT

            ListContent.promptFileRead(applicationContext)
            val data_new: List<ListContent.NestedFormatList> = ListContent.data_nested_format

            val listData = data
            titleList = ArrayList(listData.keys)
            adapter_new = CustomExpandableListAdapterNew(this, data_new)
            expandableList!!.setAdapter(adapter_new)
        }
    }

    private fun populateList(expandableList: ExpandableListView?) {
        if (expandableList != null) {
            expandableList.layoutParams.height = ActionBar.LayoutParams.WRAP_CONTENT

            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableList!!.setAdapter(adapter)



            expandableListView!!.setOnGroupExpandListener { groupPosition ->
                Toast.makeText(
                    applicationContext,
                    (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
                Toast.makeText(
                    applicationContext,
                    (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                Toast.makeText(
                    applicationContext,
                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(
                            titleList as
                                    ArrayList<String>
                            )
                        [groupPosition]]!!.get(
                        childPosition
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }
}

