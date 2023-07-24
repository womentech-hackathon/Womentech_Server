package com.womentech.server.domain.dto.response;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Day;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DailyTaskResponse {
    Long id;
    String task_name;
    EnumSet<Day> task_days;
    CompletionStatus status;
}
