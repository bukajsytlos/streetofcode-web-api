package sk.streetofcode.webapi.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.streetofcode.webapi.api.PostCommentService
import sk.streetofcode.webapi.api.dto.PostCommentDto
import sk.streetofcode.webapi.api.request.PostCommentAddRequest
import sk.streetofcode.webapi.api.request.PostCommentEditRequest
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("post/{postId}/comment")
class PostCommentController(
    private val postCommentService: PostCommentService,
    private val authenticationService: AuthenticationService
) {

    @GetMapping
    fun getAll(@PathVariable("postId") postId: String): ResponseEntity<List<PostCommentDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(postCommentService.getAll(postId))
    }

    @PostMapping
    fun add(
        @PathVariable("postId") postId: String,
        @RequestBody postCommentAddRequest: PostCommentAddRequest
    ): ResponseEntity<PostCommentDto> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                postCommentService.add(
                    authenticationService.getNullableUserId(),
                    postId,
                    postCommentAddRequest
                )
            )
    }

    @PutMapping("{commentId}")
    @IsAuthenticated
    fun edit(
        @PathVariable("postId") postId: String,
        @PathVariable("commentId") commentId: Long,
        @RequestBody postCommentEditRequest: PostCommentEditRequest,
    ): ResponseEntity<PostCommentDto> {
        return ResponseEntity.status(HttpStatus.OK).body(
            postCommentService.edit(
                authenticationService.getNullableUserId(),
                postId,
                commentId,
                postCommentEditRequest
            )
        )
    }

    @DeleteMapping("{commentId}")
    @IsAuthenticated
    fun delete(
        @PathVariable("postId") postId: String,
        @PathVariable("commentId") commentId: Long
    ): ResponseEntity<Void> {
        postCommentService.delete(
            authenticationService.getNullableUserId(),
            postId,
            commentId
        )
        return ResponseEntity.ok().build()
    }
}
