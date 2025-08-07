package com.leets.chikahae.domain.parent.entity;
import com.leets.chikahae.domain.member.entity.Gender;
import jakarta.persistence.*;
import jakarta.persistence.Id;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender parentGender;

    @Column(length = 50)
    private String parentPhoneNumber;

    @Column(name = "birth", nullable = false)
    private LocalDate parentBirth; // 생년월일 (예: 1990-01-01)

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

    public static Parent of(
            String kakaoId,
            String email,
            String name,
            Gender parentGender,
            String parentPhoneNumber,
            LocalDate parentBirth
    ) {
        return Parent.builder()
                .kakaoId(kakaoId)
                .email(email)
                .name(name)
                .parentGender(parentGender)
                .parentPhoneNumber(parentPhoneNumber)
                .parentBirth(parentBirth)
                .isDelete("N")
                .build();
    }



}//class
