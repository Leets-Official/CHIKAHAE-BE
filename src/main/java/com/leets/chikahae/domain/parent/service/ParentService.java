package com.leets.chikahae.domain.parent.service;

import com.leets.chikahae.domain.parent.entity.Parent;
import com.leets.chikahae.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParentService {


    private final ParentRepository parentRepository;

    public Optional<Parent> findByKakaoId(String kakaoId) {
        return parentRepository.findByKakaoId(kakaoId);
    }


}//class
