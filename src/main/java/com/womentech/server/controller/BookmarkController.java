package com.womentech.server.controller;

import com.womentech.server.domain.Bookmark;
import com.womentech.server.domain.dto.response.BookmarkResponse;
import com.womentech.server.exception.ErrorResponse;
import com.womentech.server.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "education", description = "교육 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/education/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping()
    @Operation(summary = "교육 북마크 조회", description = "사용자의 교육 북마크를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "교육 북마크 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BookmarkResponse.class))))
    public List<BookmarkResponse> getBookmarks(@RequestParam Long userId) {
        List<Bookmark> bookmarks = bookmarkService.findBookmarks(userId);
        List<BookmarkResponse> dto = bookmarks.stream()
                .map(bookmark -> new BookmarkResponse(bookmark.getId(), bookmark.getNumber()))
                .collect(Collectors.toList());
        return dto;
    }

    @PostMapping()
    @Operation(summary = "교육 북마크 추가", description = "사용자의 교육 북마크를 추가합니다.")
    @ApiResponse(responseCode = "OK", description = "교육 북마크 추가를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> addBookmark(@RequestParam Long userId, @RequestParam int number) {
        bookmarkService.addBookmark(userId, number);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "교육 북마크를 추가를 성공했습니다."));
    }

    @DeleteMapping("/{bookmark_id}")
    @Operation(summary = "교육 북마크 삭제", description = "사용자의 교육 북마크를 삭제합니다.")
    @ApiResponse(responseCode = "OK", description = "교육 북마크 삭제를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> deleteBookmark(@PathVariable Long bookmark_id) {
        bookmarkService.deleteBookmark(bookmark_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "교육 북마크를 삭제를 성공했습니다."));
    }
}
