package com.womentech.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "goal", cascade = REMOVE)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "goal", cascade = REMOVE)
    private List<DailyTask> dailyTasks = new ArrayList<>();

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(STRING)
    private CompletionStatus status;

    // == 비즈니스 로직 == //
    public void complete() {
        this.endDate = LocalDate.now();
        this.status = COMPLETE;
    }
}
