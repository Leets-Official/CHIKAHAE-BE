package com.leets.chikahae.domain.parent.repository;

import com.leets.chikahae.domain.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByKakaoId(String kakaoId);

    Optional<Parent> findByParentId(Long parentId);

    //회원탈퇴(주석 : 필요할 수도 있음)
//    @Modifying
//    @Query("DELETE FROM Parent p WHERE p.memberId = :memberId")
//    void deleteByMemberId(Long memberId);


}//interface
