package com.womentech.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.womentech.server.domain.converter.SetDayConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @OneToMany(mappedBy = "task")
    private List<DailyTask> dailyTasks = new ArrayList<>();

    private String name;

    @Convert(converter = SetDayConverter.class)
    private EnumSet<Day> days;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(STRING)
    private CompletionStatus status;

    // == 비즈니스 로직 == //
    public void complete() {
        this.status = COMPLETE;
    }

    public void unComplete() {
        this.status = PROGRESS;
    }
}
