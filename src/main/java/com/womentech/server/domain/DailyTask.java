package com.womentech.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyTask {
    @Id
    @GeneratedValue
    @Column(name = "daily_task_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDate date;

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
