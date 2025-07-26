package com.leets.chikahae.domain.parent.service;

import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    /**
     * 카카오 ID로 부모 조회, 없으면 저장
     */
    public Parent saveOrFind(String kakaoId, String email, String name, Boolean gender, LocalDate birth) {
        return parentRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Parent parent = Parent.builder()
                            .kakaoId(kakaoId)
                            .email(email)
                            .name(name)
                            .gender(gender)
                            .birth(birth)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .isDelete("N")
                            .build();
                    return parentRepository.saveAndFlush(parent);
                });
    }

    /**
     * 카카오 ID로 부모 조회 (Optional 반환)
     */
    public Optional<Parent> findByKakaoId(String kakaoId) {
        return parentRepository.findByKakaoId(kakaoId);
    }

}//class
