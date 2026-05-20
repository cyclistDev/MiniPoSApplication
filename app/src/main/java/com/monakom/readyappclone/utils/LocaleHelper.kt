package com.monakom.readyappclone.utils

import android.content.Context
import android.content.res.Configuration
import com.monakom.readyappclone.utils.constants.AppConstants
import java.util.Locale
import androidx.core.content.edit

object LocaleHelper {

    fun onAttach(context: Context): Context {
        val language = getSavedLanguage(context)
        return setLocale(context, language)
    }

    fun setLocale(context: Context, language: String): Context {
        saveLanguage(context, language)
        return updateResources(context, language)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(
            AppConstants.PREF_LOCALE, Context.MODE_PRIVATE
        )
        return prefs.getString(AppConstants.KEY_LANGUAGE, AppConstants.LANG_EN) ?: AppConstants.LANG_EN
    }

    private fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(
            AppConstants.PREF_LOCALE, Context.MODE_PRIVATE
        )
        prefs.edit { putString(AppConstants.KEY_LANGUAGE, language) }
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}