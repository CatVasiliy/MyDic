package com.catvasiliy.mydic.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "example",
    foreignKeys = [
        ForeignKey(
            entity = CachedTranslation::class,
            parentColumns = ["id"],
            childColumns = ["translation_id"],
            onDelete = CASCADE
        )
    ]
)
data class CachedExample(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "translation_id")
    val translationId: Long,

    val exampleText: String
)
