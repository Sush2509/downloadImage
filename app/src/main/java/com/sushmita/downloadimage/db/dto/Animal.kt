package com.sushmita.downloadimage.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animalTable")
data class Animal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "animal_id")
    var id: Int?,

    @ColumnInfo(name = "animal_image_path")
    val image_path: String?

)