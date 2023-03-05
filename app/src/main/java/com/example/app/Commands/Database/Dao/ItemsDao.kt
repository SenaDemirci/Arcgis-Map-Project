package com.example.app.Commands.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app.Commands.Database.Entities.Items


@Dao
interface ItemsDao {

    @Query("SELECT * FROM items_table")
    fun getAll(): List<Items>

    @Query("SELECT * FROM items_table WHERE code LIKE :code_info LIMIT 1")
    fun findByCode(code_info: String) :Items

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(item:Items)

    @Delete
    fun deleteItems(item:Items)

    @Query("DELETE FROM items_table")
    fun deleteAll()

    @Query("UPDATE items_table SET code=:code_info, name=:name_info WHERE id LIKE :id_info")
    fun updateItems(code_info: String, name_info:String, id_info: Int)
}