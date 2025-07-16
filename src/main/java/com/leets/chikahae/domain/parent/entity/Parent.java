package com.leets.chikahae.domain.parent.entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "kakao_id", nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name; // 카톡에서 받은 이름

    @Column(nullable = false)
    private Boolean gender; // true: 남성, false: 여성

    @Column(name = "birth", nullable = false)
    private LocalDate birth; // 생년월일 (예: 1990-01-01)

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_delete", nullable = false)
    private String isDelete;

    public Long getId() {
        return this.parentId;
    }

    public static Parent of(Long parentId,String kakaoId, String email, String name) {
        return Parent.builder()
                .parentId(parentId)
                .kakaoId(kakaoId)
                .email(email)
                .name(name)
                .isDelete("N") // 기본값은 "N"
                .build();

    }



}//class
