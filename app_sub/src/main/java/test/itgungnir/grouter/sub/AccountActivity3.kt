package test.itgungnir.grouter.sub

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_account3.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import test.itgungnir.grouter.common.AppMainActivity
import test.itgungnir.grouter.common.SubAccountActivity3

@Route(SubAccountActivity3)
class AccountActivity3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account3)

        button.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification() {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID", "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Router.instance.with(this)
            .target(AppMainActivity)
            .addFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .addFlag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .getIntent()

        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                this, 0, it,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("一条新通知")
            .setContentIntent(pendingIntent)
            .setContentText("通知内容通知内容通知内容通知内容通知内容通知内容通知内容通知内容")
            .setTicker("通知内容通知内容通知内容通知内容通知内容通知内容通知内容通知内容")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setChannelId("CHANNEL_ID")
            .setDefaults(Notification.DEFAULT_ALL)
            .build()

        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

        notificationManager.notify(1, notification)
    }
}
