package com.womentech.server.controller;

import com.womentech.server.domain.Bookmark;
import com.womentech.server.domain.dto.response.BookmarkCountResponse;
import com.womentech.server.domain.dto.response.BookmarkResponse;
import com.womentech.server.domain.dto.response.BookmarkStatusResponse;
import com.womentech.server.exception.dto.DataResponse;
import com.womentech.server.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "education bookmark", description = "교육 찜 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/education/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping()
    @Operation(summary = "찜한 교육 조회", description = "찜한 교육을 조회합니다.")
    public DataResponse<Object> getBookmarks(Authentication authentication) {
        String username = authentication.getName();
        List<Bookmark> bookmarks = bookmarkService.findBookmarks(username);
        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
                .map(bookmark -> new BookmarkResponse(
                        bookmark.getId(),
                        bookmark.getNumber()))
                .collect(Collectors.toList());

        return DataResponse.of(bookmarkResponses);
    }

    @GetMapping("/count")
    @Operation(summary = "찜한 교육 개수 조회", description = "찜한 교육 개수를 조회합니다.")
    public DataResponse<Object> countBookmarks(Authentication authentication) {
        String username = authentication.getName();

        return DataResponse.of(new BookmarkCountResponse(bookmarkService.countBookmarks(username)));
    }

    @PostMapping()
    @Operation(summary = "찜한 교육 추가", description = "찜한 교육을 추가합니다.")
    public DataResponse<Object> addBookmark(@Parameter int number, Authentication authentication) {
        String username = authentication.getName();

        bookmarkService.addBookmark(username, number);

        return DataResponse.empty();
    }

    @DeleteMapping("/{bookmark_id}")
    @Operation(summary = "찜한 교육 삭제", description = "찜한 교육을 삭제합니다.")
    public DataResponse<Object> deleteBookmark(@PathVariable("bookmark_id") Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);

        return DataResponse.empty();
    }

    @GetMapping("/isBookmarked")
    @Operation(summary = "찜한 교육 여부 조회", description = "찜한 교육인지 여부를 조회합니다.")
    public DataResponse<Object> isBookmarked(@Parameter int number, Authentication authentication) {
        String username = authentication.getName();

        return DataResponse.of(new BookmarkStatusResponse(bookmarkService.isBookmarked(username, number)));
    }
}
