package com.womentech.server.repository;

import com.womentech.server.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId ORDER BY b.id DESC")
    List<Bookmark> findByUserIdOrderByIdDesc(@Param("userId") Long userId);
}
