package com.womentech.server.domain.scheduler;

import com.womentech.server.domain.Practice;
import com.womentech.server.repository.PracticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailyStatusResetScheduler {
    private final PracticeRepository practiceRepository;

    @Autowired
    public DailyStatusResetScheduler(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    // 매일 자정에 실행될 스케줄링 메서드
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyStatus() {
        // 오늘 날짜 구하기
        LocalDate today = LocalDate.now();

        // 모든 Practice 조회
        List<Practice> practices = practiceRepository.findAll();

        // 각 Practice의 dailyStatus를 0으로 초기화
        for (Practice practice : practices) {
            if (practice.getAchievementStatus() != null && practice.getAchievementStatus() == false) {
                practice.setDailyStatus(false);
            }
        }

        // 변경사항 저장
        practiceRepository.saveAll(practices);
    }
}
