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
public class TaskTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testTaskWithDays() {
        // Task 생성
        Task task = new Task();
        task.setName("Test Practice");

        // Day EnumSet 생성
        EnumSet<Day> days = EnumSet.of(Day.월, Day.화, Day.수);
        task.setDays(days);

        // Task 저장
        entityManager.persist(task);
        entityManager.flush();
        entityManager.clear();

        // 저장된 Task 조회
        Task savedTask = entityManager.find(Task.class, task.getId());

        // 조회한 Task의 days 확인
        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals("Test Practice", savedTask.getName());
        Assertions.assertEquals(days, savedTask.getDays());
    }
}