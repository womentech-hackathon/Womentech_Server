package com.womentech.server.domain.dto.response;

import com.womentech.server.domain.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.EnumSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskCompletedResponse {
    private String name;
    private EnumSet<Day> days;
    private LocalDate startDate;
    private LocalDate endDate;
}
