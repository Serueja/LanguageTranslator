package com.example.languagetranslator

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
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

    private var sourceLanguageCode = "en"
    private var sourceLanguageTitle = "English"
    private var targetLanguageCode = "ru"
    private var targetLanguageTitle = "Russian"

    private lateinit var translatorOptions: TranslatorOptions

    private lateinit var translator: Translator

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceLanguageEt = findViewById(R.id.sourceLanguageEt)
        targetLanguageTv = findViewById(R.id.targetLanguageTv)
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn)
        targetLanguageChooseBtn = findViewById(R.id.targetLanguageChooseBtn)
        translateBtn = findViewById(R.id.translateBtn)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadAvailableLanguages()

        sourceLanguageChooseBtn.setOnClickListener {
            sourceLanguageChoose()
        }

        targetLanguageChooseBtn.setOnClickListener {
            targetLanguageChoose()
        }

        translateBtn.setOnClickListener {
            validateDate()
        }
    }

    private var sourceLanguageText = ""
    private fun validateDate(){
        sourceLanguageText = sourceLanguageEt.text.toString().trim()

        Log.d(TAG, "validateData: sourceLanguageText: $sourceLanguageText")

        if (sourceLanguageText.isEmpty()) {

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

            languageArrayList!!.add(modelLanguage)
        }
    }

    private fun sourceLanguageChoose() {

        val popupMenu = PopupMenu(this, sourceLanguageChooseBtn)

        for (i in languageArrayList!!.indices) {

            popupMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)
        }

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val position = menuItem.itemId

            sourceLanguageCode = languageArrayList!![position].languageCode
            sourceLanguageTitle = languageArrayList!![position].languageTitle

            sourceLanguageChooseBtn.text = sourceLanguageTitle
            sourceLanguageEt.hint = "Enter $sourceLanguageTitle"

            Log.d(TAG, "sourceLanguageChoose: sourceLanguageCode: $sourceLanguageCode")
            Log.d(TAG, "sourceLanguageChoose: sourceLanguageTitle: $sourceLanguageTitle")

            false
        }
    }

    private fun targetLanguageChoose(){

        val popupMenu = PopupMenu(this, targetLanguageChooseBtn)

        for (i in languageArrayList!!.indices) {

            popupMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)
        }

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val position = menuItem.itemId

            targetLanguageCode = languageArrayList!![position].languageCode
            targetLanguageTitle = languageArrayList!![position].languageTitle

            targetLanguageChooseBtn.text = targetLanguageTitle

            Log.d(TAG, "targetLanguageChoose: targetLanguageCode: $targetLanguageCode")
            Log.d(TAG, "targetLanguageChoose: targetLanguageTitle: $targetLanguageTitle")

            false
        }
    }

    private fun showToast()
}