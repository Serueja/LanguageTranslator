package com.example.languagetranslator

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale
import android.content.Intent
import android.database.Cursor
import android.text.TextUtils
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.DataInput


class MainActivity : AppCompatActivity() {

    private lateinit var sourceLanguageEt: EditText
    private lateinit var targetLanguageTv:  TextView
    private lateinit var sourceLanguageChooseBtn: MaterialButton
    private lateinit var targetLanguageChooseBtn: MaterialButton
    private lateinit var translateBtn: MaterialButton
    private lateinit var historyBtn: MaterialButton
    private lateinit var recyclerView: RecyclerView

    // db instance
    private lateinit var db: DBHelper
    lateinit var dbh: DBHelper
    private lateinit var newArray: ArrayList<Datalist>
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

        db = DBHelper(this)

        dbh = DBHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayuser()

        sourceLanguageEt = findViewById(R.id.sourceLanguageEt)
        targetLanguageTv = findViewById(R.id.targetLanguageTv)
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn)
        targetLanguageChooseBtn = findViewById(R.id.targetLanguageChooseBtn)
        translateBtn = findViewById(R.id.translateBtn)
        historyBtn = findViewById(R.id.historyBtn)

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

        // подвязка translateBtn к триггеру обновления БД
        translateBtn.setOnClickListener {
            validateDate()
            val message = sourceLanguageEt.text.toString()
            val langfrom = sourceLanguageChooseBtn.text.toString()
            val langto = targetLanguageChooseBtn.text.toString()
            val savehistory = db.savehistory(message, langfrom, langto)

            if (TextUtils.isEmpty(message)) {
                Toast.makeText(this, "Add Text For Translation", Toast.LENGTH_SHORT).show()
            }
            else{
                if (savehistory==true){
                    Toast.makeText(this, "Text Saved", Toast.LENGTH_SHORT).show()
                }
//                else {
//                    Toast.makeText(this, "Text Saved", Toast.LENGTH_SHORT).show()
//                }
            }
        }


        historyBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, History::class.java)
            startActivity(intent)
        }

//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
     }

    private fun displayuser() {
        var newcursor: Cursor? = dbh!!.gettext()
        newArray = ArrayList<Datalist>
        while (newcursor)
    }

    private var sourceLanguageText = ""
    private fun validateDate(){
        sourceLanguageText = sourceLanguageEt.text.toString().trim()

        Log.d(TAG, "validateData: sourceLanguageText: $sourceLanguageText")

        if (sourceLanguageText.isEmpty()) {
            showToast("Enter text to translate...")
        }
        else {
            startTranslation()
        }
    }

    private fun startTranslation() {
        progressDialog.setMessage("Processing language model...")
        progressDialog.show()

        translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        translator = Translation.getClient(translatorOptions)

        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                Log.d(TAG, "startTranslation: model ready, starting translation")

                progressDialog.setMessage("Translating...")

                translator.translate(sourceLanguageText)
                    .addOnSuccessListener { translatedText ->
                        Log.d(TAG, "startTranslation: translatedText: $translatedText")

                        progressDialog.dismiss()

                        targetLanguageTv.text = translatedText
                    }
                    .addOnFailureListener{e ->

                        progressDialog.dismiss()

                        Log.e(TAG, "startTranslation: ", e)

                        showToast("Failed to translate due to ${e.message}")
                    }
            }
            .addOnFailureListener{e ->
                progressDialog.dismiss()

                Log.e(TAG, "StartTranslation: ", e)

                showToast("Failed due to ${e.message}")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}