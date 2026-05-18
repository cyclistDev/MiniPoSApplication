package com.monakom.readyappclone.ui.language

import android.os.Bundle
import android.view.View
import com.monakom.readyappclone.base.BaseActivity
import com.monakom.readyappclone.databinding.ActivitySelectLanguageBinding
import com.monakom.readyappclone.utils.LocaleHelper

class SelectLanguageActivity : BaseActivity() {

    private lateinit var binding: ActivitySelectLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateCheckmarks(LocaleHelper.getSavedLanguage(this))
        binding.btnBack.setOnClickListener { finish() }
        binding.rowEnglish.setOnClickListener { selectLanguage("English") }
        binding.rowKhmer.setOnClickListener   { selectLanguage("Khmer") }
        binding.rowChinese.setOnClickListener { selectLanguage("Chinese") }
    }

    private fun updateCheckmarks(language: String) {
        binding.ivCheckEnglish.visibility = View.GONE
        binding.ivCheckKhmer.visibility = View.GONE
        binding.ivCheckChinese.visibility = View.GONE

        when (language) {
            "en" -> binding.ivCheckEnglish.visibility = View.VISIBLE
            "km" -> binding.ivCheckKhmer.visibility = View.VISIBLE
            "zh" -> binding.ivCheckChinese.visibility = View.VISIBLE
        }
    }
    private fun selectLanguage
                (language: String) {
        binding.ivCheckEnglish.visibility = View.GONE
        binding.ivCheckKhmer.visibility   = View.GONE
        binding.ivCheckChinese.visibility = View.GONE

        when (language) {
            "English" -> {
                binding.ivCheckEnglish.visibility = View.VISIBLE
                applyLanguage("en")
            }
            "Khmer" -> {
                binding.ivCheckKhmer.visibility = View.VISIBLE
                applyLanguage("km")
            }
            "Chinese" -> {
                binding.ivCheckChinese.visibility = View.VISIBLE
                applyLanguage("zh")
            }
        }
    }
}