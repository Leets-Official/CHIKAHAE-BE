package com.leets.chikahae.domain.mission.entity;

import com.leets.chikahae.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberMissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column
    private LocalDateTime completedAt;

    public enum Status {
        IN_PROGRESS,
        COMPLETED,
        REWARDED
    }

    /** 완료 처리용 도메인 메서드 **/
    public void markRewarded() {
        this.status = Status.REWARDED;
        this.completedAt = LocalDateTime.now();
    }


}
