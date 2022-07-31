package be.bxl.moorluck.exoredditwidget.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.bxl.moorluck.exoredditwidget.R
import be.bxl.moorluck.exoredditwidget.widget.RedditWidget

class SettingViewModel(val context: Context) : ViewModel() {

    private val _subreddit = MutableLiveData<String>()
    val subreddit : LiveData<String>
        get() = _subreddit

    private val sharedPref : SharedPreferences by lazy {
        context.getSharedPreferences(context.getString(R.string.pref_name), Context.MODE_PRIVATE)
    }

    init {
        _subreddit.value = sharedPref.getString(RedditWidget.PREF_STRING, "")
    }

    fun changeSubreddit(name: String) {
        sharedPref.edit().putString(RedditWidget.PREF_STRING, name).apply()
        _subreddit.value = name
    }

    fun resetSubreddit() {
        sharedPref.edit().putString(RedditWidget.PREF_STRING, "").apply()
        _subreddit.value = ""
    }
}