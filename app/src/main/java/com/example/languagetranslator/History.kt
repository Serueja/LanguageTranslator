package com.example.languagetranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class History : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val actionBar = supportActionBar

        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler)

    }
}