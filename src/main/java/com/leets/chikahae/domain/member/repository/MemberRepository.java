package com.leets.chikahae.domain.member.repository;

import com.leets.chikahae.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // ✅ 닉네임 중복 확인용
    boolean existsByNickname(String nickname);

    /**
     * 부모의 카카오 ID로 회원 조회
     */
    @Query("SELECT m FROM Member m JOIN Parent p ON m.parentId = p.parentId WHERE p.kakaoId = :kakaoId")
    Optional<Member> findByParentKakaoId(@Param("kakaoId") String kakaoId);

    /**
     * 부모 ID로 첫 번째 자녀 조회 (Optional 반환)
     */
    Optional<Member> findFirstByParentId(Long parentId);
}
