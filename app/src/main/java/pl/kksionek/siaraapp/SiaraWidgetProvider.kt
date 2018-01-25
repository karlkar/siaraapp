package pl.kksionek.siaraapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

class SiaraWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        if (appWidgetIds == null) {
            return
        }
        for (widgetId in appWidgetIds) {
            val views = RemoteViews(context?.packageName, R.layout.siara_widget)

            val intentHappy = Intent(context, PlayService::class.java)
            intentHappy.putExtra(BUNDLE_KEY_MOOD, BUNDLE_VAL_HAPPY)
            val pendingHappyIntent = PendingIntent.getService(
                    context,
                    1,
                    intentHappy,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(
                    R.id.button_good,
                    pendingHappyIntent)

            val intentSad = Intent(context, PlayService::class.java)
            intentSad.putExtra(BUNDLE_KEY_MOOD, BUNDLE_VAL_SAD)
            val pendingSadIntent = PendingIntent.getService(
                    context?.applicationContext,
                    2,
                    intentSad,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            views.setOnClickPendingIntent(
                    R.id.button_bad,
                    pendingSadIntent)

            appWidgetManager?.updateAppWidget(widgetId, views)
        }
    }
}