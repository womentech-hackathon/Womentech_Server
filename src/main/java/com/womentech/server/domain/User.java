package com.womentech.server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String name;

    private String password;

    @OneToOne(mappedBy = "user", fetch = LAZY)
    private Goal goal;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();
}
