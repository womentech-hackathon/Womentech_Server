package com.womentech.server.domain.dto.response;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.EnumSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DailyTaskResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private EnumSet<Day> days;
    private CompletionStatus status;
}
