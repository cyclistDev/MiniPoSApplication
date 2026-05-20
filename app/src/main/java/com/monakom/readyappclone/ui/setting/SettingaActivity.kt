package com.monakom.readyappclone.ui.setting

import android.content.Intent
import android.os.Bundle
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.databinding.ActivitySettingBinding
import com.monakom.readyappclone.ui.language.SelectLanguageActivity
import com.monakom.readyappclone.MainApplication
import com.monakom.readyappclone.utils.LocaleHelper
import com.monakom.readyappclone.data.mqtt.MqttManager
import kotlin.jvm.java

class SettingActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.rowLanguages.setOnClickListener {
            startActivity(Intent(this, SelectLanguageActivity::class.java))
        }
        // Show current language name
        val currentLang = LocaleHelper.getSavedLanguage(this)
        binding.tvCurrentLanguage.text = when (currentLang) {
            "km" -> "ខ្មែរ"
            "zh" -> "中文"
            else -> "English"
        }

        binding.tvLogOut.setOnClickListener {
            MqttManager.disconnect()
            SessionManager.clearSession(this)
            startActivity(Intent(this, MainApplication::class.java))
            finishAffinity() // clears all back stack → fresh login
        }
    }
}