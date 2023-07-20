package com.womentech.server.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "goal")
    private List<Practice> practices = new ArrayList<>();

    private String name;

    @Enumerated(EnumType.STRING)
    private GoalCategory category;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean achievementStatus;
}
