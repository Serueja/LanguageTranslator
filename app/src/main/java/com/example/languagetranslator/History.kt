package com.example.languagetranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}