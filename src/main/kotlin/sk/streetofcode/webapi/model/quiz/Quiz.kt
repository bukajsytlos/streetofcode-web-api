package sk.streetofcode.webapi.model.quiz

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import sk.streetofcode.webapi.api.dto.quiz.QuizDto
import sk.streetofcode.webapi.model.Lecture
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Quiz(
    @Id
    @SequenceGenerator(name = "quiz_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_id_seq")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    var lecture: Lecture,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var subtitle: String?,

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var createdAt: OffsetDateTime,

    @Column(nullable = true)
    var finishedMessage: String?,

    @OneToMany(
        mappedBy = "quiz",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @OrderBy("question_order")
    var questions: MutableSet<QuizQuestion>
) {
    constructor(lecture: Lecture, title: String, subtitle: String?, finishedMessage: String?) :
        this(null, lecture, title, subtitle, OffsetDateTime.now(), finishedMessage, mutableSetOf())

    override fun equals(other: Any?) = other is Quiz && QuizEssential(this) == QuizEssential(other)
    override fun hashCode() = QuizEssential(this).hashCode()
    override fun toString() = QuizEssential(this).toString().replaceFirst("QuizEssential", "Quiz")
}

private data class QuizEssential(
    val lectureId: Long,
    val title: String,
    val subtitle: String?,
    val createdAt: OffsetDateTime,
    val finishedMessage: String?
) {
    constructor(quiz: Quiz) : this(
        lectureId = quiz.lecture.id!!,
        title = quiz.title,
        subtitle = quiz.subtitle,
        createdAt = quiz.createdAt,
        finishedMessage = quiz.finishedMessage
    )
}

fun Quiz.toQuizDto(): QuizDto {
    return QuizDto(
        id = id!!,
        lectureId = lecture.id!!,
        title = title,
        subtitle = subtitle,
        createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS),
        finishedMessage = finishedMessage,
        questionIds = questions.map { it.id!! }.toSet()
    )
}
