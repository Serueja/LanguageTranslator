package com.example.languagetranslator

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DBHelper(context:Context): SQLiteOpenHelper(context, "History", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table History(source TEXT primary key, target TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL( "drop table if exists History")
    }

}

    fun savehistory(source:String, target:String): {
        val p0 = this.writableDAtabase
        val cv = ContentValues
        cv.put ("source", source)
        cv.put ("target", target)
        val result = p0.insert("History", null, cv)
        if (result==-1 .toLong()){
            return false
        }
        return true
    }