package com.example.modul7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

// Helper class untuk mengelola database SQLite
class HomeworkHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = "TABLE_NAME"
        private var INSTANCE: HomeworkHelper? = null

        fun getInstance(context: Context): HomeworkHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeworkHelper(context)
            }
    }

    // fungsi menutup dan membuka koneksi ke database
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen) {
            database.close()
        }
    }

    // Fungsi untuk CRUD untuk membaca semua data
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            columns = null,
            selection = null,
            selectionArgs = null,
            groupBy = null,
            having = null,
            orderBy = "_ID ASC",
            limit = null
        )
    }

    // Fungs insert, update delete
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, nullColumnHack = null, values)
    }
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, whereClause = "_ID = ?", whereArgs = arrayOf(id))
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, whereClause = "_ID = '$id'", whereArgs = null)
    }
}
