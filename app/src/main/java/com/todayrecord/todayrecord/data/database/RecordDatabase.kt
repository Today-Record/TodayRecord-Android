package com.todayrecord.todayrecord.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.todayrecord.todayrecord.data.repository.record.local.RecordDao
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.model.record.StringListTypeConverter

@Database(
    entities = [Record::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListTypeConverter::class)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun record(): RecordDao

    companion object {
        private const val databaseName = "today-record-db"

        fun buildDatabase(context: Context): RecordDatabase {
            return Room.databaseBuilder(context, RecordDatabase::class.java, databaseName)
                .enableMultiInstanceInvalidation()
                .build()
        }
    }
}