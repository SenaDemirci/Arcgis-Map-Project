package com.example.app.Commands.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_table")
data class Items(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name = "code") val itemCode:String?,
    @ColumnInfo(name = "name") val itemName:String?,
    @ColumnInfo(name = "point") val pointLoc:String?,
)
