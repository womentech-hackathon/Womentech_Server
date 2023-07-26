package com.womentech.server.service;

import com.womentech.server.domain.Bookmark;
import com.womentech.server.domain.User;
import com.womentech.server.repository.BookmarkRepository;
import com.womentech.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<Bookmark> findBookmarks(Long userId) {
        return bookmarkRepository.findByUserIdOrderByIdDesc(userId);
    }

    @Transactional
    public void addBookmark(Long userId, int number) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> {
            Bookmark bookmark = Bookmark.builder()
                    .user(u)
                    .number(number)
                    .build();
            bookmarkRepository.save(bookmark);
        });
    }

    @Transactional
    public void deleteBookmark(Long userId, int number) {
        bookmarkRepository.deleteByUserIdAndNumber(userId, number);
    }
}
