package be.bxl.moorluck.exoredditwidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import be.bxl.moorluck.exoredditwidget.R
import be.bxl.moorluck.exoredditwidget.api.MemeApi
import be.bxl.moorluck.exoredditwidget.api.client.RetrofitClient
import be.bxl.moorluck.exoredditwidget.api.dto.Meme
import be.bxl.moorluck.exoredditwidget.ui.SettingActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Implementation of App Widget functionality.
 */
class RedditWidget : AppWidgetProvider() {
    companion object {
        const val PREF_STRING = "subreddit"
        const val SETTING_ACTION = "settingAction"
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, appWidgetIds)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("helloe", "hello")
        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            onUpdate(context, AppWidgetManager.getInstance(context), intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) ?: IntArray(0))
        }
        else {
            super.onReceive(context, intent)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    appWidgetIds: IntArray
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.reddit_widget)

    views.setOnClickPendingIntent(R.id.btn_widget_update, getPendingIntentUpdate(context, appWidgetIds))
    views.setOnClickPendingIntent(R.id.btn_widget_setting, getPendingIntentSetting(context))

    val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE)

    Log.d("pref", sharedPref.getString(RedditWidget.PREF_STRING, "") ?: "")

    val api = RetrofitClient.client.create(MemeApi::class.java)

    api.randomMeme(sharedPref.getString(RedditWidget.PREF_STRING, "") ?: "").enqueue(object : Callback<Meme> {
        override fun onResponse(call: Call<Meme>, response: Response<Meme>) {
            Log.d("res", "res")
            val awt = AppWidgetTarget(context, R.id.img_widget, views, appWidgetId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("url", it.url)
                    Glide.with(context)
                        .asBitmap()
                        .load(it.url)
                        .override(480, 342)
                        .into(awt)
                }
            }
            else {
                Toast.makeText(context, "Can't find something on this subreddit !", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Meme>, t: Throwable) {
            Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show()
        }
    })

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

internal fun getPendingIntentSetting(context: Context): PendingIntent? {
    val intent = Intent(context, SettingActivity::class.java)
    intent.action = RedditWidget.SETTING_ACTION
    return PendingIntent.getActivity(context, 0, intent, 0)
}

internal fun getPendingIntentUpdate(context: Context, appWidgetIds: IntArray): PendingIntent? {
    val intent = Intent(context, RedditWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
    return PendingIntent.getBroadcast(context, 0, intent, 0)
}
