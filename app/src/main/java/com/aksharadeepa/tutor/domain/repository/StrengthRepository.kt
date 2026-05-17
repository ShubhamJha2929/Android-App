package com.aksharadeepa.tutor.domain.repository

import com.aksharadeepa.tutor.domain.model.GapArea
import com.aksharadeepa.tutor.domain.model.StrengthData
import kotlinx.coroutines.flow.Flow

interface StrengthRepository {
    fun observeStrengthMap(): Flow<List<StrengthData>>
    fun observeGapAreas(threshold: Float = 60f): Flow<List<GapArea>>
}
