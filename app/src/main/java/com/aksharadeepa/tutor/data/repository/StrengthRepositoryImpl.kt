package com.aksharadeepa.tutor.data.repository

import com.aksharadeepa.tutor.data.local.dao.SubjectDao
import com.aksharadeepa.tutor.data.local.dao.SubjectScoreDao
import com.aksharadeepa.tutor.domain.model.GapArea
import com.aksharadeepa.tutor.domain.model.StrengthData
import com.aksharadeepa.tutor.domain.repository.StrengthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class StrengthRepositoryImpl(
    private val subjectScoreDao: SubjectScoreDao,
    private val subjectDao: SubjectDao
) : StrengthRepository {

    override fun observeStrengthMap(): Flow<List<StrengthData>> =
        combine(
            subjectScoreDao.observeScores(),
            subjectDao.observeSubjectsWithChapters()
        ) { scores, subjects ->
            subjects.map { subjectWithChapters ->
                val score = scores.find { it.subjectId == subjectWithChapters.subject.id }
                StrengthData(
                    subjectName = subjectWithChapters.subject.name,
                    masteryScore = score?.masteryScore ?: 0f,
                    colorHex = subjectWithChapters.subject.colorHex
                )
            }
        }

    override fun observeGapAreas(threshold: Float): Flow<List<GapArea>> =
        observeStrengthMap().map { strengths ->
            strengths
                .filter { it.masteryScore < threshold }
                .map { data ->
                    GapArea(
                        subjectName = data.subjectName,
                        masteryScore = data.masteryScore,
                        recommendation = gapRecommendation(data.subjectName, data.masteryScore)
                    )
                }
        }

    private fun gapRecommendation(subject: String, score: Float): String =
        when {
            score < 30f -> "Start with basics in $subject — complete 2 chapters and take daily quizzes."
            score < 60f -> "Focus on weak chapters in $subject. Review wrong answers after each quiz."
            else -> "Keep practicing $subject to maintain momentum."
        }
}
