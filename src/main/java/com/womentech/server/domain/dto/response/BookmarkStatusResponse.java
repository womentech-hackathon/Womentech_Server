package com.womentech.server.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BookmarkStatusResponse {
    @JsonProperty("isBookmarked")
    private boolean isBookmarked;
}
