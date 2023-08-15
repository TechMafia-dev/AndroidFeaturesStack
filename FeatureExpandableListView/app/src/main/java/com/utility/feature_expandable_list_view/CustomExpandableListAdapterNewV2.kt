package com.utility.feature_expandable_list_view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Typeface
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import java.util.HashMap
import com.utility.feature_expandable_list_view.ExpandableListData.data

class CustomExpandableListAdapterNewV2 internal constructor(
    private val context: Context,
    private val customData: List<ListContent.NestedFormatList>,
    private val groupTitle: String
) : BaseExpandableListAdapter() {
    init {
        var stringBuilder:StringBuilder = java.lang.StringBuilder()
        for (item in customData)
            stringBuilder.append(item.content + "  ")
        Log.d("child_trial", "getChild: title: $groupTitle list: $stringBuilder")
    }

    fun hasChild(listPosition: Int, expandedListPosition: Int): Boolean{
        val check:Boolean = (this.customData[expandedListPosition].child?.isEmpty() == false)
        return check
    }
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.customData[expandedListPosition].content.toString()
    }

    internal fun getChildList(listPosition: Int, expandedListPosition: Int): List<ListContent.NestedFormatList> {
        return this.customData[expandedListPosition].child!!
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String

        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.listView)
        expandedListTextView.text = expandedListText


        val expandableList = convertView!!.findViewById<ExpandableListView>(R.id.expendableList)
        if (hasChild(listPosition, expandedListPosition)) {
            populateList(convertView, expandableList, getChildList(listPosition, expandedListPosition), expandedListText)

            expandedListTextView.text = ""
            expandedListTextView.layoutParams.height = 0
        }
        else{

            expandableList.layoutParams.height = 0
        }

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        Log.d("counter_tag", "getChildrenCount: $groupTitle | count: ${customData.size}")
        return this.customData.size
    }

    override fun getGroup(listPosition: Int): Any {
        return groupTitle
    }

    override fun getGroupCount(): Int {
        return 1
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    private fun populateList(parentView: View, expandableList: ExpandableListView,
                             childList: List<ListContent.NestedFormatList>, expandedListText: String) {

        var stringBuilder:StringBuilder = java.lang.StringBuilder()
        for (item in childList)
            stringBuilder.append(item.content + "  ")
        Log.d("inflator_tag", "populateList: title: $expandedListText | list: $stringBuilder")

        if (expandableList != null) {
            val listData = data
            var titleList: List<String> = ArrayList(listData.keys)
            var adapter: ExpandableListAdapter = CustomExpandableListAdapterNewV2(
                context, childList, expandedListText)
            expandableList!!.setAdapter(adapter)
        }
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return false
    }

}