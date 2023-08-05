package com.example.feature_custom_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.feature_custom_notification.ui.theme.FeatureCustomNotificationTheme

class MainActivity : ComponentActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListener()
    }

    private fun setClickListener() {
        var politics:Button
        var sports:Button
        var entertainment:Button
        var custom_notifs:Button
        politics = findViewById(R.id.politics)
        sports = findViewById(R.id.sports)
        entertainment = findViewById(R.id.entertainment)
        custom_notifs = findViewById(R.id.custom_notifs)
        politics.setOnClickListener(this@MainActivity)
        sports.setOnClickListener(this@MainActivity)
        entertainment.setOnClickListener(this@MainActivity)
        custom_notifs.setOnClickListener( this@MainActivity)
    }

    override fun onClick(view: View?) {

        when (view?.id) {

            R.id.politics -> {
                //Politics Notification
                createNotification(
                    resources.getString(R.string.channel_politics),
                    resources.getString(R.string.politics_content),
                    resources.getString(R.string.channel_politics),
                    NotificationCompat.PRIORITY_HIGH,
                    100
                )
            }

            R.id.sports -> {
                //Sports Notification
                createNotification(
                    resources.getString(R.string.channel_sports),
                    resources.getString(R.string.sports_content),
                    resources.getString(R.string.channel_sports),
                    NotificationCompat.PRIORITY_DEFAULT,
                    101
                )
            }

            R.id.entertainment -> {
                //Entertainment Notification
                createNotification(
                    resources.getString(R.string.channel_entertainment),
                    resources.getString(R.string.entertainment_content),
                    resources.getString(R.string.channel_entertainment),
                    NotificationCompat.PRIORITY_LOW,
                    102
                )
            }

            R.id.custom_notifs -> {
                //Custom Notification
                createCustomNotification()
            }
        }
    }

    /**
     * Create Notification
     * Param
     * 1. title
     * 2. content
     * 3. channelId
     * 4.priorty
     * 5. notificationId
     */
    fun createNotification(
        title: String,
        content: String,
        channelId: String,
        priorty: Int,
        notificationID: Int
    ) {

        val intent = Intent(applicationContext, NotifyActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val extras = Bundle()
        extras.putString(NotifyActivity.notify_title, title)
        extras.putString(NotifyActivity.notify_content, content)
        intent.putExtras(extras)
        intent.action = Intent.ACTION_VIEW

        //val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val pendingIntent: PendingIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(priorty)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(500, 500, 500))


        with(NotificationManagerCompat.from(applicationContext)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                createNotificationChannel(channel)
            }
            // notificationId is a unique int for each notification that you must define
            notify(notificationID, builder.build())
        }

        playNotificationSound(this@MainActivity)
    }

    private fun createCustomNotification() {

        val notificationID: Int = 100
        // Create an explicit intent for an Activity in your app
        val intent = Intent(applicationContext, CustomNotifyActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.data = Uri.parse("https://www.tutorialsbuzz.com/")

        //val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val pendingIntent: PendingIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                applicationContext,
                notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val channelId = "News"
        val channelName = "News"

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(500, 500, 500))

        val smallContent = RemoteViews(getPackageName(), R.layout.small_layout_notification)
        val bigContent = RemoteViews(getPackageName(), R.layout.large_notification_layout)

        bigContent.setTextViewText(R.id.notification_title, "Notification Custom Text")
        smallContent.setTextViewText(R.id.notification_title, "Notification Custom Text")

        bigContent.setImageViewResource(R.id.notification_image, R.mipmap.ic_launcher)
        smallContent.setImageViewResource(R.id.notification_image, R.mipmap.ic_launcher)

        builder.setContent(smallContent)
        builder.setCustomBigContentView(bigContent)

        with(NotificationManagerCompat.from(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                createNotificationChannel(channel)
                notify(notificationID, builder.build())
            }

            playNotificationSound(this@MainActivity)
        }
    }

    /**
     * Play sound for notification
     */
    fun playNotificationSound(context: Context) {
        try {
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, defaultSoundUri)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
    FeatureCustomNotificationTheme {
        Greeting("Android")
    }
}