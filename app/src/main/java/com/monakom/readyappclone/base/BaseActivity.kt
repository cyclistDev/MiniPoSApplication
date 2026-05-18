package com.monakom.readyappclone.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.monakom.readyappclone.utils.LocaleHelper

/**
 * BaseActivity — all activities extend this so locale is always applied.
 * Instead of extending AppCompatActivity directly, extend BaseActivity.
 *
 * Change: class MainActivity : AppCompatActivity()
 *      to: class MainActivity : BaseActivity()
 */
open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    fun applyLanguage(languageCode: String) {
        LocaleHelper.setLocale(this, languageCode)
        recreate()
    }

}