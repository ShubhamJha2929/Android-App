package com.aksharadeepa.tutor.data.local

import com.aksharadeepa.tutor.data.local.entity.ChapterEntity
import com.aksharadeepa.tutor.data.local.entity.QuestionEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectEntity
import com.aksharadeepa.tutor.data.local.entity.SubjectScoreEntity

object DatabaseSeeder {

    const val SUBJECT_SCIENCE = 1L
    const val SUBJECT_MATH = 2L
    const val SUBJECT_SOCIAL = 3L

    val subjects = listOf(
        SubjectEntity(SUBJECT_SCIENCE, "Science", "#2E7D32"),
        SubjectEntity(SUBJECT_MATH, "Math", "#1565C0"),
        SubjectEntity(SUBJECT_SOCIAL, "Social Studies", "#6A1B9A")
    )

    private val scienceChapters = listOf(
        "Chemical Reactions and Equations",
        "Acids, Bases and Salts",
        "Metals and Non-metals",
        "Carbon and its Compounds",
        "Life Processes",
        "Control and Coordination",
        "How do Organisms Reproduce?",
        "Heredity and Evolution"
    )

    private val mathChapters = listOf(
        "Real Numbers",
        "Polynomials",
        "Pair of Linear Equations",
        "Quadratic Equations",
        "Arithmetic Progressions",
        "Triangles",
        "Coordinate Geometry",
        "Statistics"
    )

    private val socialChapters = listOf(
        "The Rise of Nationalism in Europe",
        "Nationalism in India",
        "Resources and Development",
        "Agriculture",
        "Manufacturing Industries",
        "Lifelines of National Economy",
        "Power Sharing",
        "Federalism"
    )

    fun buildChapters(): List<ChapterEntity> {
        var chapterId = 1L
        val chapters = mutableListOf<ChapterEntity>()
        fun add(subjectId: Long, titles: List<String>) {
            titles.forEachIndexed { index, title ->
                chapters += ChapterEntity(
                    id = chapterId++,
                    subjectId = subjectId,
                    title = title,
                    orderIndex = index
                )
            }
        }
        add(SUBJECT_SCIENCE, scienceChapters)
        add(SUBJECT_MATH, mathChapters)
        add(SUBJECT_SOCIAL, socialChapters)
        return chapters
    }

    fun buildInitialScores(): List<SubjectScoreEntity> = subjects.map {
        SubjectScoreEntity(subjectId = it.id, masteryScore = 0f)
    }

    fun buildQuestions(chapters: List<ChapterEntity>): List<QuestionEntity> {
        val templates = listOf(
            Triple("What is the key concept in %s?", listOf("Definition A", "Definition B", "Definition C", "Definition D"), 0),
            Triple("Which statement about %s is correct?", listOf("Statement 1", "Statement 2", "Statement 3", "Statement 4"), 1),
            Triple("In %s, which factor matters most?", listOf("Factor A", "Factor B", "Factor C", "Factor D"), 2),
            Triple("Solve / identify the answer related to %s:", listOf("Answer 1", "Answer 2", "Answer 3", "Answer 4"), 3),
            Triple("Best practice while studying %s is:", listOf("Read notes daily", "Skip revision", "Ignore examples", "Avoid practice"), 0)
        )
        val questions = mutableListOf<QuestionEntity>()
        chapters.forEach { chapter ->
            val shortTitle = chapter.title.take(40)
            templates.forEach { (pattern, options, correct) ->
                questions += QuestionEntity(
                    chapterId = chapter.id,
                    questionText = pattern.format(shortTitle),
                    optionA = options[0],
                    optionB = options[1],
                    optionC = options[2],
                    optionD = options[3],
                    correctOptionIndex = correct
                )
            }
        }
        return questions
    }
}
