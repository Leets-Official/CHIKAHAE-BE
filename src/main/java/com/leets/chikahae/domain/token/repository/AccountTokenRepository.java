package com.leets.chikahae.domain.token.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.token.entity.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {

    Optional<AccountToken> findByMemberAndTokenType(Member member, String tokenType);

    //회원탈퇴 (주석: 필요할 수도 있음)
    //    @Modifying
    //    @Query("DELETE FROM AccountToken t WHERE t.member.id = :memberId")
    void deleteByMemberId(Long memberId);

    //로그아웃
    void deleteByToken(String token);

    //토큰 재발급
    Optional<AccountToken> findByToken(String token);

    //카카오 Id로 token 검색
    Optional<AccountToken> findByMemberKakaoId(String kakaoId);
}//interface
