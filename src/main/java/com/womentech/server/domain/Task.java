package com.womentech.server.domain;

import com.womentech.server.domain.converter.SetDayConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.EnumSet;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private String name;

    @Convert(converter = SetDayConverter.class)
    private EnumSet<Day> days;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(STRING)
    private CompletionStatus status;

    // == 비즈니스 로직 == //
    public void complete() {
        this.endDate = LocalDate.now();
        this.status = COMPLETE;
    }

    public void unComplete() {
        this.endDate = null;
        this.status = PROGRESS;
    }
}
