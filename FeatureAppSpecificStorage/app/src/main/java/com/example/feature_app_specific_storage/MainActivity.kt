package com.example.feature_app_specific_storage

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature_app_specific_storage.ui.theme.FeatureAppSpecificStorageTheme
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    lateinit var file_save:Button
    lateinit var file_display:Button
    lateinit var fileName:TextView
    lateinit var fileData:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // writes data to file; given the filename
        file_save = findViewById(R.id.file_save)
        fileName = findViewById(R.id.fileName)
        fileData = findViewById(R.id.fileData)
        file_save.setOnClickListener {
            val file = fileName.text.toString()
            val data = fileData.text.toString()

            val fileOutputStream : FileOutputStream

            try{
                fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
                fileOutputStream.write(data.toByteArray())
            }
            catch (e: FileNotFoundException){
                e.printStackTrace()
            }
            catch (e: Exception){
                e.printStackTrace()
            }

            showToast("Saved to File...")
            //fileData.setText(this.getFilesDir().getAbsolutePath())
        }

        // gets data for a given filename
        file_display = findViewById(R.id.file_display)
        file_display.setOnClickListener {

            val file = fileName.text.toString()

            if (file.toString()!=null && file.trim()!=""){
                var fileInputStream: FileInputStream? = null
                fileInputStream = openFileInput(file)
                var inputStreamReader:InputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader:BufferedReader = BufferedReader(inputStreamReader)

                val stringBuilder:StringBuilder = StringBuilder()
                var text:String? = null

                while({text = bufferedReader.readLine(); text}()!=null) {
                    stringBuilder.append(text)
                }

                fileData.setText(stringBuilder.toString())
            }
            else{
                showToast("Name of the file can't be blank")
            }
        }

    }
    //function extension for Toast.makeText(...)
    fun Context.showToast(text: CharSequence, duration: Int= Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FeatureAppSpecificStorageTheme {
        Greeting("Android")
    }
}