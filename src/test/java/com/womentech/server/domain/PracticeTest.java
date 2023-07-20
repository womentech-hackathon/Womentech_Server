package com.womentech.server.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@SpringBootTest
@Transactional
public class PracticeTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testPracticeWithDays() {
        // Practice 생성
        Practice practice = new Practice();
        practice.setName("Test Practice");

        // Day EnumSet 생성
        EnumSet<Day> days = EnumSet.of(Day.MON, Day.WED, Day.FRI);
        practice.setDays(days);

        // Practice 저장
        entityManager.persist(practice);
        entityManager.flush();
        entityManager.clear();

        // 저장된 Practice 조회
        Practice savedPractice = entityManager.find(Practice.class, practice.getId());

        // 조회한 Practice의 days 확인
        Assertions.assertNotNull(savedPractice);
        Assertions.assertEquals("Test Practice", savedPractice.getName());
        Assertions.assertEquals(days, savedPractice.getDays());
    }
}