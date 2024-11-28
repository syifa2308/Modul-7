package com.example.modul7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class HomeworkHelper(context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.HomeworkColumns.TABLE_NAME
        @Volatile
        private var INSTANCE: HomeworkHelper? = null

        fun getInstance(context: Context): HomeworkHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeworkHelper(context).also { INSTANCE = it }
            }
        }
    }

    @Throws(SQLException::class)
    fun open() {
        try {
            database = dataBaseHelper.writableDatabase
        } catch (e: SQLException) {
            Log.e("HomeworkHelper", "Error opening database: ${e.message}")
            throw e
        }
    }

    fun close() {
        try {
            dataBaseHelper.close()
            if (database.isOpen) {
                database.close()
            }
        } catch (e: Exception) {
            Log.e("HomeworkHelper", "Error closing database: ${e.message}")
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${DatabaseContract.HomeworkColumns._ID} ASC",
            null
        )
    }


    fun insert(values: ContentValues?): Long {
        return try {
            if (values == null) -1 else database.insert(DATABASE_TABLE, null, values)
        } catch (e: Exception) {
            Log.e("HomeworkHelper", "Error inserting data: ${e.message}")
            -1
        }
    }

    fun update(id: String, values: ContentValues?): Int {
        return try {
            if (values == null) 0 else database.update(
                DATABASE_TABLE,
                values,
                "${DatabaseContract.HomeworkColumns._ID} = ?",
                arrayOf(id)
            )
        } catch (e: Exception) {
            Log.e("HomeworkHelper", "Error updating data: ${e.message}")
            0
        }
    }

    fun deleteById(id: String): Int {
        return try {
            if (id.isEmpty()) 0 else database.delete(
                DATABASE_TABLE,
                "${DatabaseContract.HomeworkColumns._ID} = ?",
                arrayOf(id)
            )
        } catch (e: Exception) {
            Log.e("HomeworkHelper", "Error deleting data: ${e.message}")
            0
        }
    }
}
