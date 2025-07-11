package com.leets.chikahae.domain.parent.service;

import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParentService {


    private final ParentRepository parentRepository;

    public Parent saveOrFind(String kakaoId, String email, String name) {
        return parentRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Parent parent = Parent.builder()
                            .kakaoId(kakaoId)
                            .email(email)
                            .name(name)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .isDelete("N")
                            .build();
                    return parentRepository.save(parent);
                });
    }



}//class
