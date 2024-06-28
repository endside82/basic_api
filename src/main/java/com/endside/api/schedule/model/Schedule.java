package com.endside.api.schedule.model;

import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.constants.StartEndType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity(name = "schedule")
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;                    // 스케쥴 고유번호
    @Column(name = "stuff_id")
    private long stuffId;               // 물건 고유번호
    @Column(name = "receipt_id")
    private Long receiptId;             // 접수 고유번호
    @Column(name = "date")
    private LocalDate date;             // 스케쥴 날짜
    @Column(name = "schedule_type")
    private ScheduleType scheduleType;  // 스케쥴 종류
    @Column(name = "start_end_type")
    private StartEndType startEndType;  // 시작종료구분
    @CreationTimestamp
    private LocalDateTime createdAt;    // 생성시각
    @UpdateTimestamp
    private LocalDateTime updatedAt;    // 변경시각

    @Builder
    public Schedule(long stuffId, Long receiptId, LocalDate date, ScheduleType scheduleType, StartEndType startEndType) {
        this.stuffId = stuffId;
        this.receiptId = receiptId;
        this.date = date;
        this.scheduleType = scheduleType;
        this.startEndType = startEndType;
    }

    public void shiftDay(long day) {
        this.date = this.date.plusDays(day);
    }

    public void changeStartEndType(StartEndType startEndType) {
        this.startEndType = startEndType;
    }

    public void updateNow(){
        this.updatedAt = LocalDateTime.now();
    }
}
