package com.aksharadeepa.tutor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val colorHex: String
)
