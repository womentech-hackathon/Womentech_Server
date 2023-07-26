package com.womentech.server.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoalCompletedResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TaskCompletedResponse> tasks;
}
