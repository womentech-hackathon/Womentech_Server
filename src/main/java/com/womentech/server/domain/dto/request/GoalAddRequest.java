package com.womentech.server.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoalAddRequest {
    private List<TaskRequest> tasks;
    private String name;
}
