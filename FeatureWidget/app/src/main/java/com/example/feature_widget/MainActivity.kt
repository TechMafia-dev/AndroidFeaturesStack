package com.example.feature_widget

import android.R.style.Widget
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.feature_widget.ui.theme.FeatureWidgetTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        set_clicking_functionality(this)


        if (intent != null && intent.action == "widget triggered") {
            val coffee_id = intent.getIntExtra("coffee_id", 0)

            if (coffee_id == 100)
                Toast.makeText(this, "clicked : restretto " + coffee_id, Toast.LENGTH_SHORT).show()
            if (coffee_id == 200)
                Toast.makeText(this, "clicked : espresso " + coffee_id, Toast.LENGTH_SHORT).show()
            if (coffee_id == 300)
                Toast.makeText(this, "clicked : latte " + coffee_id, Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this, "Natural Start", Toast.LENGTH_SHORT).show()
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
    FeatureWidgetTheme {
        Greeting("Android")
    }
}

fun set_clicking_functionality(context: Context){
    val appWidgetManager = AppWidgetManager.getInstance(context)

    CustomWidget.updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(ComponentName(
        context,
        CustomWidget::class.java
    )).get(0))
}