package com.womentech.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "goal")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "goal")
    private List<DailyTask> dailyTasks = new ArrayList<>();

    private String name;

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
