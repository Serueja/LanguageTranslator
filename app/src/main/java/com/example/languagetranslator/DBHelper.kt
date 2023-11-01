package com.example.languagetranslator

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class DBHelper(context:Context): SQLiteOpenHelper(context, "History", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table History(message TEXT, langfrom TEXT, langto TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists History")
    }

    fun savehistory(message : String, langfrom : String, langto: String): Boolean {
        val p0 = this.writableDatabase
        val cv = ContentValues()

        cv.put("message", message)
        cv.put("langfrom", langfrom)
        cv.put("langto", langto)

        val result = p0.insert("History", null, cv)

        if (result==-1.toLong()) {
            return false
        }
        return true
    }

    fun gettext(): Cursor? {
        val p0 = this.writableDatabase
        val cursor = p0.rawQuery("select * from History", null)
        return cursor
    }
}