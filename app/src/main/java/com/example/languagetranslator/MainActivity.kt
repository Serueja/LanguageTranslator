package com.example.languagetranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.mlkit.nl.translate.TranslateLanguage
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var sourceLanguageEt: EditText
    private lateinit var targetLanguageTv:  TextView
    private lateinit var sourceLanguageChooseBtn: MaterialButton
    private lateinit var targetLanguageChooseBtn: MaterialButton
    private lateinit var translateBtn: MaterialButton

    companion object {
        // for printing logs
        private const val TAG = "MAIN_TAG"
    }

    private var languageArrayList: ArrayList<ModelLanguage>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceLanguageEt = findViewById(R.id.sourceLanguageEt)
        targetLanguageTv = findViewById(R.id.targetLanguageTv)
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn)
        targetLanguageChooseBtn = findViewById(R.id.targetLanguageChooseBtn)
        translateBtn = findViewById(R.id.translateBtn)

        loadAvailableLanguages()

        sourceLanguageChooseBtn.setOnClickListener {

        }

        targetLanguageChooseBtn.setOnClickListener {

        }

        translateBtn.setOnClickListener {

        }
    }

    private fun loadAvailableLanguages() {

        languageArrayList = ArrayList()

        val languageCodeList = TranslateLanguage.getAllLanguages()

        for (languageCode in languageCodeList) {

            val languageTitle = Locale(languageCode).displayLanguage
            Log.d(TAG, "loadAvailableLanguages: languageCode: $languageCode")
            Log.d(TAG, "loadAvailableLanguages: languageTitle: $languageTitle")

            val modelLanguage = ModelLanguage(languageCode, languageTitle)
        }
    }
}