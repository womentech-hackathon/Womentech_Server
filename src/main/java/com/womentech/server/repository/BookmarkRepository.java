package com.womentech.server.repository;

import com.womentech.server.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserIdOrderById(Long userId);

    int countByUserId(Long userId);

    boolean existsByUserIdAndNumber(Long userId, int number);
}
