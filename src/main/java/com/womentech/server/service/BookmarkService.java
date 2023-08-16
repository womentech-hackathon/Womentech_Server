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

    public List<Bookmark> findBookmarks(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return bookmarkRepository.findByUserIdOrderById(user.getId());
    }

    public int countBookmarks(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return bookmarkRepository.countByUserId(user.getId());
    }

    @Transactional
    public void addBookmark(String username, int number) {
        Optional<User> user = userRepository.findByUsername(username);
        user.ifPresent(u -> {
            Bookmark bookmark = Bookmark.builder()
                    .user(u)
                    .number(number)
                    .build();
            bookmarkRepository.save(bookmark);
        });
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    public boolean isBookmarked(String username, int number) {
        User user = userRepository.findByUsername(username).orElse(null);

        return bookmarkRepository.existsByUserIdAndNumber(user.getId(), number);
    }
}
