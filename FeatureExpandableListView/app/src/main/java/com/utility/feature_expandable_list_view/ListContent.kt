package com.utility.feature_expandable_list_view

import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader

internal object ListContent {
    data class Row(val index: String, val content: String){
        val level:Int = computeLevel()
        var viewID:Int = 0
        var selected:Boolean = false
        var visible:Boolean = visible()
        var childVisible:Boolean = false
        var hasChild:Boolean = false

        private fun computeLevel(): Int {
            return index.split(".").size // (number of ".") + 1
        }

        private fun visible(): Boolean {
            if (level == 1)
                return true
            return false
        }

    }
    data class NestedFormatList(val _content: String){
        var content: String?
        var child: MutableList<NestedFormatList>?
        init {
            this.content = _content
            this.child = mutableListOf<NestedFormatList>()
        }
    }

    lateinit var data_row_format: List<Row>
    lateinit var data_nested_format: List<NestedFormatList>

    fun promptFileRead(context: Context) {
        var fileSaver:FileSaver = FileSaver()
        val file = fileSaver.saveFile(context)

        // gets data from the storage
        var fileReader:FileReader = FileReader()
        data_row_format = fileReader.extractData(context, file)
        data_nested_format = fileReader.convertToNested(data_row_format)
    }

    private class FileReader() {
        fun convertToNested(data: List<Row>): List<NestedFormatList> {
            var nested: MutableList<NestedFormatList>? = mutableListOf<NestedFormatList>()
            for (i in 0..data.size - 1) {
                val pos: List<String> = data.get(i).index.split(".")

                var temp: MutableList<NestedFormatList>? = nested
                for (j in 0..pos.size - 2) {
                    temp = temp?.get(pos.get(j).toInt() - 1)?.child!!
                }
                temp?.add(NestedFormatList(data.get(i).content))

            }
            return nested!!
        }

        fun readCsv(inputStream: InputStream): List<Row> {
            val reader = inputStream.bufferedReader()
            val header = reader.readLine()
            return reader.lineSequence()
                .filter { it.isNotBlank() }
                .map {
                    val (index, content) = it.split(',', ignoreCase = false, limit = 2)
                    Row(index.trim().removeSurrounding("\""), content.trim().removeSurrounding("\""))
                }.toList()
        }

        fun extractData(context: Context, file: String): List<Row> {
            var fileInputStream: FileInputStream? = null
            fileInputStream = context.openFileInput(file)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)

            val file_data = readCsv(fileInputStream)
            return file_data
        }
    }

    private class FileSaver() {
        fun saveFileToStorage(context: Context, file: String, data: String) {

            val fileOutputStream: FileOutputStream

            try {
                fileOutputStream = context.openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun saveFile(context: Context): String {
            //data
            val data = "Index,Content\n" +
                    "1,Madhya Pradesh\n" +
                    "1.1,Indore\n" +
                    "1.1.1,MHOW\n" +
                    "1.1.1.1,Hariphatak\n" +
                    "1.1.1.2,Dreamland\n" +
                    "1.1.1.3,Luniyapura\n" +
                    "1.1.1.4,Kodaria\n" +
                    "1.1.1.5,janpad\n" +
                    "1.1.2,rau\n" +
                    "1.1.2.1,mamaji dhaba\n" +
                    "1.1.2.2,vikas\n" +
                    "1.1.2.3,kanupriya nagar\n" +
                    "1.1.3,palasia\n" +
                    "1.1.3.1,TI\n" +
                    "1.1.3.2,Greater Kailash\n" +
                    "1.1.4,vijai nagar\n" +
                    "1.1.4.1,Phoenix\n" +
                    "1.1.4.2,Satyasai\n" +
                    "1.1.4.3,MR10\n" +
                    "1.1.4.4,Lasudiya\n" +
                    "1.2,Bhopal\n" +
                    "1.2.1,Lalganj\n" +
                    "1.2.2,Raja bhoj\n" +
                    "1.2.3,ISBT\n" +
                    "1.2.4,Central Railway Station\n" +
                    "1.2.4.1,platform1\n" +
                    "1.2.4.2,platform2\n" +
                    "1.2.5,Habibganj\n" +
                    "1.2.5.1,Services\n" +
                    "1.2.5.2,IT\n" +
                    "1.2.5.3,Police\n" +
                    "1.3,Khandwa\n" +
                    "1.4,Ujjain\n" +
                    "2,Gujrat\n" +
                    "2.1,Ahmedabad\n" +
                    "2.1.1,GIFT\n" +
                    "2.1.2,Sabarmati\n" +
                    "2.1.3,Kalupur\n" +
                    "2.2,Surat\n" +
                    "2.2.1,ISKON\n" +
                    "2.2.2,Gopi Talav\n" +
                    "2.2.3,Ambika Niketan\n" +
                    "2.2.4,Maheshwari Bhavan\n" +
                    "2.3,Somnath\n" +
                    "2.4,Baroda\n" +
                    "2.4.1,Golden Chokdi\n" +
                    "2.4.2,Poicha\n" +
                    "2.4.3,Sokhda\n" +
                    "3,Rajasthan\n" +
                    "3.1,Sikar\n" +
                    "3.2,Ajmer\n" +
                    "3.3,Jodhpur\n" +
                    "3.4,Kumbhalgarh\n" +
                    "4,Karnataka\n" +
                    "5,Tamil Nadu\n"

            // writes data to file; given the filename
            val file = "test"

            saveFileToStorage(context, file, data)
            return file
        }
    }

    class CustomExpandableList (context: Context, parentView: RelativeLayout, data: List<ListContent.Row>) {
        private val applicationContext: Context
        private var parentView: RelativeLayout          // current view : parent of the newly added children
        private val dataBase: List<ListContent.Row>

        init {
            applicationContext = context
            this.parentView = parentView
            dataBase = data
        }

        public fun populateViews() {
            for (i in dataBase.indices) {
                val row = dataBase[i]
                val height = viewRenderer(row, 100, 0)
                Log.d("dynamic_view", "populateViews: " + height)
            }

            checkChilds()
            setClickListners()
            resetPositions()
        }

        private fun checkChilds() {
            for (i in dataBase.indices) {
                if ((i + 1) < dataBase.size)
                    if (dataBase[i + 1].level > dataBase[i].level)
                        dataBase[i].hasChild = true

                if (dataBase[i].hasChild != true)
                    parentView.findViewById<View>(dataBase[i].viewID)
                        .findViewById<ImageView>(R.id.expander_arrow).visibility = View.INVISIBLE
            }
        }

        private fun resetPositions() {
            var vert_pos = 100
            val separator = 10
            for (i in dataBase.indices) {
                if (dataBase[i].visible == false)
                    continue

                val view = parentView.findViewById<View>(dataBase[i].viewID)
                var layoutParams = view.layoutParams
                val param = view.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(view.marginLeft, vert_pos, 50, view.marginBottom)
                view.layoutParams =
                    param // Tested!! - You need this line for the params to be applied

                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                vert_pos += view.measuredHeight + separator
            }
            parentView.layoutParams.height = vert_pos
        }

        private fun setClickListners() {
            for (i in 0..dataBase.size - 1) {
                val item = parentView.findViewById<View>(dataBase[i].viewID)
                if (dataBase[i].hasChild)
                    buttonEffect(item)

                item.setOnClickListener() {
                    val childVisible = !dataBase[i].childVisible
                    if (childVisible) {
                        item.findViewById<ImageView>(R.id.expander_arrow)
                            .setImageResource(R.drawable.arrow_up)
                    } else {
                        item.findViewById<ImageView>(R.id.expander_arrow)
                            .setImageResource(R.drawable.arrow_down)
                    }
                    toggleChildVisibility(childVisible, i)
                    resetPositions()
                }
            }
        }

        private fun buttonEffect(button: View) {
            button.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                        v.invalidate()
                    }

                    MotionEvent.ACTION_UP -> {
                        v.background.clearColorFilter()
                        v.invalidate()
                    }

                    MotionEvent.ACTION_HOVER_EXIT -> {
                        v.background.clearColorFilter()
                        v.invalidate()
                    }
                }
                false
            }
        }

        private fun toggleChildVisibility(childVisible: Boolean, item_index: Int) {
            dataBase[item_index].childVisible = childVisible
            for (i in (item_index + 1) until dataBase.size) {
                val case_expand = (childVisible)
                val case_collapse = !childVisible

                if (dataBase[i].level <= dataBase[item_index].level)
                    break;

                if (case_collapse) {
                    dataBase[i].visible = false
                    parentView.findViewById<View>(dataBase[i].viewID).isInvisible = true
                }
                if (case_expand) {
                    if (dataBase[i].level == dataBase[item_index].level + 1) {
                        dataBase[i].visible = true
                        parentView.findViewById<View>(dataBase[i].viewID).isInvisible = false
                    }
                }
            }
        }

        private fun viewRenderer(item: ListContent.Row, marginLeft: Int, marginTop: Int): Int {
            // Inflate the dynamic view layout
            val inflater = LayoutInflater.from(applicationContext)
            val dynamicView = inflater.inflate(R.layout.list_item_custom, parentView, false)
            val label: TextView = dynamicView.findViewById(R.id.listView)
            label.text = item.content

            // Set resource id
            val id = View.generateViewId()
            dynamicView.id = id
            item.viewID = id

            // Set layout parameters for the new Button
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
            )
            // Set margins (left, top, right, bottom) in pixels
//            val dpToPixels = resources.getDimensionPixelSize(R.dimen.button_margin)
//        layoutParams.setMargins(10*dpToPixels, 20*dpToPixels, 0, 0)
            layoutParams.setMargins(50 * item.level, marginTop, 0, 0)

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE)
            dynamicView.layoutParams = layoutParams

            // sizing
            dynamicView.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT

            //Background
            dynamicView.setBackgroundColor(R.color.teal_200)

            //visibility
            dynamicView.isInvisible = !item.visible

            // Add the new Button to the RelativeLayout
            parentView.addView(dynamicView)

            return dynamicView.layoutParams.height
        }
    }

}