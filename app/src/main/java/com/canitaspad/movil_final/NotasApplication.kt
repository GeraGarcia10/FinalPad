package com.canitaspad.movil_final

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.canitaspad.movil_final.data.AppContainer
import com.canitaspad.movil_final.data.AppDataContainer

class NotasApplication : Application() {
    val container: AppContainer by lazy { AppDataContainer(this) }

    override fun onCreate() {
        super.onCreate()

        // Configuración del canal de notificaciones
        val channelId = "alarm_id"
        val channelName = "alarm_name"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        } else {
            // Manejo para versiones anteriores a Android O
                    }
    }
}
