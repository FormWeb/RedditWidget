package be.bxl.moorluck.exoredditwidget.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import be.bxl.moorluck.exoredditwidget.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding

    private val viewModel: SettingViewModel by viewModels { SettingViewModelFactory(this@SettingActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddSetting.setOnClickListener {
            viewModel.changeSubreddit(binding.etSubredditSetting.text.toString())
        }

        binding.btnResetSetting.setOnClickListener {
            viewModel.resetSubreddit()
        }

        viewModel.subreddit.observe(this) {
            binding.tvSubredditSetting.text = it
        }

    }
}