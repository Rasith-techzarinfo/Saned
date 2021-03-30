package com.saned.view.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.saned.R
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.ui.activities.dynamicWF.ViewDynamicWFActivity
import com.saned.view.utils.Utils.Companion.getCurrentTime
import org.json.JSONException
import org.json.JSONObject


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "FirebaseMessagingServi"
    var productName: String = ""

    override fun onNewToken(token: String) {
        Log.e("onNewToken", "Refreshed token: $token")

        prefHelper.setFCMToken("" + token)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "remoteMessage from: ${remoteMessage.from}")
        Log.e(TAG, "remoteMessage toString:" + remoteMessage.toString())
        Log.e(TAG, "remoteMessage body:" + remoteMessage.notification?.body)
        Log.e(TAG, "remoteMessage toString:" + remoteMessage.notification.toString())

        productName = "" + remoteMessage.notification?.body


        if (remoteMessage.notification != null) {
            // Check if message contains a data payload.
            if (remoteMessage.data.size > 0) {
                Log.e(
                    TAG,
                    "Data Payload: " + remoteMessage.data.toString()
                )
                try {
                    val json = JSONObject(remoteMessage.data.toString())
                    handleDataMessage(json)
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: " + e.message)
                }
            }
        }

    }

    private fun handleDataMessage(json: JSONObject) {

        Log.e(TAG, "push json: $json")
        //data: {
        //     title: '"Title of the notification"',
        //     body: '" Message,
//             userid:'"' user_id '"',
        //     wkid: '"'workflowid '"',
        //     type: '"1"'
        //}

        try {


            val moduleType = json.getString("type")
            val wkid = json.getString("wkid")
            val userID = json.getString("userid")
            var titleString  = json.getString("title").toString()
            var descriptionString  = json.getString("body")


//            //type 1 > HADetail

            Log.e(TAG, "moduleType: $moduleType")
            Log.e(TAG, "responseUserID: $userID")
            Log.e(TAG, "wkid: $wkid")
            Log.e(TAG, "userType: " + prefHelper.getUserType())
            Log.e(TAG, "userType: " + prefHelper.getUserId())

            //NOTIFICATION LOGICS HERE


            if (prefHelper.getUserId() == userID) {
//
                if(moduleType == "1") {

                    //static formid, name for now
                    val intent = Intent(applicationContext, ViewDynamicWFActivity::class.java)
                    intent.putExtra("formID", "" + "101")
                    intent.putExtra("formName", "HAForm")
                    intent.putExtra("wkid", "" + wkid)
                    showNotificationDatas(titleString, descriptionString, intent)
                }



            }

        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }

    }

    private fun showNotificationDatas(title: String, message: String, intent: Intent) {

        Log.e("NotificationData", "" + title)
        Log.e("NotificationData", "" + message)
        val collapsedView = RemoteViews(packageName, R.layout.custom_notification_normal)
        collapsedView.setTextViewText(
            R.id.time_text,
            getCurrentTime()
        )
        collapsedView.setTextViewText(
            R.id.title_text,
            title
        )
        collapsedView.setTextViewText(
            R.id.description_text, message

        )


        val expandedView = RemoteViews(packageName, R.layout.custom_notification_full)
        expandedView.setTextViewText(
            R.id.time_text,
            getCurrentTime()
        )
        expandedView.setTextViewText(
            R.id.title_text,
            title
        )
        expandedView.setTextViewText(
            R.id.description_text, message

        )


        //cancel notification on click
//        val actionIntent = Intent(this, ActionReceiver::class.java)
//        actionIntent.putExtra("noti_id", 0)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            this, 0, actionIntent,
//            PendingIntent.FLAG_CANCEL_CURRENT
//        )

        val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, "sanedapplication")
            .setSmallIcon(R.drawable.saned_logo)
            .setAutoCancel(true)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()


            val channel = NotificationChannel(
                "sanedapplication",
                "saned",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "saned notification"
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, attributes)
            notificationManager.createNotificationChannel(channel)


        }
        notificationManager.notify(48, notificationBuilder.build())
    }

//    private fun showNotification(title: String?, body: String?) {
//
//
//        Log.e(TAG, "Dikirim dari:" + getCurrentTime())
//
//        val collapsedView = RemoteViews(packageName, R.layout.custom_notification_normal)
//        collapsedView.setTextViewText(
//            R.id.time_text,
//            getCurrentTime()
//        )
//        collapsedView.setTextViewText(
//            R.id.title_text,
//            title
//        )
//        collapsedView.setTextViewText(
//            R.id.description_text, body
//
//        )
//
//
//        val expandedView = RemoteViews(packageName, R.layout.custom_notification_full)
//        expandedView.setTextViewText(
//            R.id.time_text,
//            getCurrentTime()
//        )
//        expandedView.setTextViewText(
//            R.id.title_text,
//            title
//        )
//        expandedView.setTextViewText(
//            R.id.description_text, body
//
//        )
//
//        val intent = Intent(this, NotificationActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val notificationBuilder = NotificationCompat.Builder(this)
//            .setSmallIcon(R.drawable.watremark_cyan)
//            // .setContentTitle(title)
//            // .setContentText(body)
//            .setAutoCancel(true)
//            .setCustomContentView(collapsedView)
//            .setCustomBigContentView(expandedView)
//            .setPriority(NotificationManager.IMPORTANCE_HIGH)
//            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//            .setContentIntent(pendingIntent)
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val attributes = AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .build()
//
//
//            val channel = NotificationChannel(
//                "saned",
//                "saned",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.description = "saned"
//            channel.importance = NotificationManager.IMPORTANCE_HIGH
//            channel.enableLights(true)
//            channel.lightColor = Color.RED
//            channel.enableVibration(true)
//            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, attributes)
//            notificationManager.createNotificationChannel(channel)
//
//
//        }
//        notificationManager.notify(0, notificationBuilder.build())
//
//    }

}


class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val noti_id = intent.getIntExtra("noti_id", -1)
            if (noti_id > 0) {
                val notificationManager = context
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(noti_id)
            }
        }
    }
}