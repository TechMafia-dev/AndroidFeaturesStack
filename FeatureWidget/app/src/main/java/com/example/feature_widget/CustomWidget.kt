package com.example.feature_widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 */
class CustomWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("Update", "zingalla onUpdate: ")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.custom_widget)
//    views.setTextViewText(R.id.appwidget_text, widgetText)

    // update data-base --> Takes you to the app
    views.setOnClickPendingIntent(R.id.restretto,
        getPendingIntent(context, 100))
    views.setOnClickPendingIntent(R.id.espresso,
        getPendingIntent(context, 200))
    views.setOnClickPendingIntent(R.id.latte,
        getPendingIntent(context, 300))




    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun getPendingIntent(context: Context, value: Int): PendingIntent {
    //1
    val intent = Intent(context, MainActivity::class.java)
    //2
    intent.action = Intent.ACTION_VIEW
    //3
    intent.putExtra("extra", value)
    //4
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    //5
    val pendingIntent = PendingIntent.getActivity(
        context,
        0, intent, PendingIntent.FLAG_IMMUTABLE
    )

    return pendingIntent
}