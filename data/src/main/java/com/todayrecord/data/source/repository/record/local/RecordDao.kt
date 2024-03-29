package com.todayrecord.data.source.repository.record.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.todayrecord.data.source.repository.record.model.Record

@Dao
internal interface RecordDao {

    @Query("SELECT * FROM RECORD WHERE isDeleted = :isDeleted ORDER BY date desc")
    fun getRecords(isDeleted: Boolean): PagingSource<Int, Record>

    @Query("SELECT * FROM RECORD WHERE id is NULL OR id = :recordId")
    suspend fun getRecord(recordId: String): Record?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Query("UPDATE RECORD SET isDeleted = :isDeleted WHERE id = :recordId")
    suspend fun setRecordDeleted(recordId: String, isDeleted: Boolean)

    @Query("DELETE FROM RECORD WHERE id is NULL OR id = :recordId")
    suspend fun deleteRecord(recordId: String)

    @Query("DELETE FROM RECORD WHERE isDeleted")
    suspend fun clearBinRecords()

    @Query("DELETE FROM RECORD")
    suspend fun clearAllRecords()
}