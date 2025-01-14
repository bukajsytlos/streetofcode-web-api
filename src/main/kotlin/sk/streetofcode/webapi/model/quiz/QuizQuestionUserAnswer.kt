package sk.streetofcode.webapi.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.quiz.QuizQuestionUserAnswerDto
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class QuizQuestionUserAnswer(
    @Id
    @SequenceGenerator(name = "quiz_question_user_answer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_question_user_answer_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    var question: QuizQuestion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    var answer: QuizQuestionAnswer,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val createdAt: OffsetDateTime,

    @Column(nullable = false)
    var tryCount: Int
) {
    constructor(question: QuizQuestion, answer: QuizQuestionAnswer, userId: String, tryCount: Int) :
        this(null, question, answer, userId, OffsetDateTime.now(), tryCount)

    override fun equals(other: Any?) = other is QuizQuestionUserAnswer && QuizQuestionUserAnswerEssential(this) == QuizQuestionUserAnswerEssential(other)
    override fun hashCode() = QuizQuestionUserAnswerEssential(this).hashCode()
    override fun toString() = QuizQuestionUserAnswerEssential(this).toString().replaceFirst("QuizQuestionUserAnswerEssential", "QuizQuestionUserAnswer")
}

private data class QuizQuestionUserAnswerEssential(
    val questionId: Long,
    val answerId: Long,
    val createdAt: OffsetDateTime,
    val userId: String,
    val tryCount: Int
) {
    constructor(quizQuestionUserAnswer: QuizQuestionUserAnswer) : this(
        questionId = quizQuestionUserAnswer.question.id!!,
        answerId = quizQuestionUserAnswer.answer.id!!,
        createdAt = quizQuestionUserAnswer.createdAt,
        userId = quizQuestionUserAnswer.userId,
        tryCount = quizQuestionUserAnswer.tryCount
    )
}

fun QuizQuestionUserAnswer.toQuizQuestionUserAnswerDto(): QuizQuestionUserAnswerDto {
    return QuizQuestionUserAnswerDto(
        id = this.id!!,
        question = this.question.toQuizQuestionDto(),
        answer = this.answer.toQuizQuestionAnswerDto(),
        userId = this.userId,
        tryCount = this.tryCount,
        isCorrect = false
    )
}
