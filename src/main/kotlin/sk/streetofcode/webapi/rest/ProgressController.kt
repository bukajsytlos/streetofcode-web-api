package sk.streetofcode.webapi.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.streetofcode.webapi.api.ProgressService
import sk.streetofcode.webapi.api.dto.progress.CourseProgressOverviewDto
import sk.streetofcode.webapi.api.dto.progress.UserProgressMetadataDto
import sk.streetofcode.webapi.api.request.ResetProgressDto
import sk.streetofcode.webapi.configuration.annotation.IsAuthenticated
import sk.streetofcode.webapi.service.AuthenticationService

@RestController
@RequestMapping("progress")
class ProgressController(private val authenticationService: AuthenticationService, private val progressService: ProgressService) {

    @PostMapping("/update/{lectureId}")
    @IsAuthenticated
    fun updateProgress(@PathVariable("lectureId") lectureId: Long): ResponseEntity<CourseProgressOverviewDto> {
        return ResponseEntity.ok(progressService.updateProgress(authenticationService.getUserId(), lectureId))
    }

    @PostMapping("/reset")
    @IsAuthenticated
    fun resetProgress(@RequestBody resetProgress: ResetProgressDto): ResponseEntity<CourseProgressOverviewDto> {
        return ResponseEntity.ok(progressService.resetProgress(authenticationService.getUserId(), resetProgress))
    }

    @GetMapping("/overview/{courseId}")
    @IsAuthenticated
    fun getProgressOverview(@PathVariable("courseId") courseId: Long): ResponseEntity<CourseProgressOverviewDto> {
        return ResponseEntity.ok(progressService.getProgressOverview(authenticationService.getUserId(), courseId))
    }

    @GetMapping("/metadata/{courseId}")
    @IsAuthenticated
    fun getUserProgressMetadata(@PathVariable("courseId") courseId: Long): ResponseEntity<UserProgressMetadataDto> {
        return ResponseEntity.ok(progressService.getUserProgressMetadata(authenticationService.getUserId(), courseId))
    }
}
