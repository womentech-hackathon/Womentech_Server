package com.womentech.server.repository;

import com.womentech.server.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserIdOrderById(Long userId);

    void deleteByUserIdAndNumber(Long userId, int number);

    int countByUserId(Long userId);
}
