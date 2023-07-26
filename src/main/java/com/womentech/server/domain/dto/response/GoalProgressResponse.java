package com.womentech.server.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoalProgressResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private List<TaskProgressResponse> tasks;
}
