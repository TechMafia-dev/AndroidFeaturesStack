package com.utility.feature_expandable_list_view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Typeface
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

class CustomExpandableListAdapterNew internal constructor(
    private val context: Context,
    private val customData: List<ListContent.NestedFormatList>
) : BaseExpandableListAdapter() {

    var flag_force_single_render = false

    init {
        for (element in customData){
            if (element.child?.isEmpty() == false) {
                for (item in element.child!!){
                    if (item.child?.isEmpty() == false){
                        flag_force_single_render = true
                        break
                    }
                }
            }
        }
    }

    fun hasChild(listPosition: Int, expandedListPosition: Int): Boolean{
        return (this.customData[listPosition].child?.get(expandedListPosition)?.child?.isEmpty() == false)
    }
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.customData[listPosition].child?.get(expandedListPosition)?.content.toString()
    }

    internal fun getChildList(listPosition: Int, expandedListPosition: Int): List<ListContent.NestedFormatList> {
        return this.customData[listPosition].child!!
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
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.listView)
        expandedListTextView.text = expandedListText

        if (hasChild(listPosition, expandedListPosition)) {
            val expandableList = convertView!!.findViewById<ExpandableListView>(R.id.expendableList)
            populateList(convertView, expandableList, getChildList(listPosition, expandedListPosition), expandedListText)

            expandedListTextView.text = ""
            expandedListTextView.layoutParams.height = 0
        }

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        if (this.customData[listPosition].child?.isEmpty()==true)
            return 0
        if (flag_force_single_render)
            return 1

        return this.customData[listPosition].child?.size ?: 0
    }

    override fun getGroup(listPosition: Int): Any {
        return this.customData[listPosition].content.toString()
    }

    override fun getGroupCount(): Int {
        return this.customData.size
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
        if (getChildrenCount(listPosition)<1){ // hide expandable view

            val expandableListView: NestedExpandableListView = convertView!!.findViewById(R.id.expendableList)
            listTitleTextView.setTypeface(null, Typeface.NORMAL)
            expandableListView.layoutParams.height = 0
        }
        return convertView
    }

    private fun populateList(parentView: View, expandableList: ExpandableListView,
                             childList: List<ListContent.NestedFormatList>, expandedListText: String) {
        if (expandableList != null) {
            val listData = data
            var titleList: List<String> = ArrayList(listData.keys)
            var adapter: ExpandableListAdapter = CustomExpandableListAdapterNew(
                context, childList)
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