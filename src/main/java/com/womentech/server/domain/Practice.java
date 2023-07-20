package com.womentech.server.domain;

import com.womentech.server.domain.converter.SetDayConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.EnumSet;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Practice {
    @Id
    @GeneratedValue
    @Column(name = "practice_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private String name;

    @Convert(converter = SetDayConverter.class)
    private EnumSet<Day> days;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean dailyStatus;

    private Boolean achievementStatus;
}
