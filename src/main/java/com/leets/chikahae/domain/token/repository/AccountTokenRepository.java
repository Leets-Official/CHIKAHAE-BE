package com.leets.chikahae.domain.token.repository;

import com.leets.chikahae.domain.member.entity.Member;
import com.leets.chikahae.domain.token.entity.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {

    Optional<AccountToken> findByMemberAndTokenType(Member member, String tokenType);


//    Optional<AccountToken> findByMemberId(Member member);

//    Optional<AccountToken> findByMember(Member member);

//    Optional<AccountToken> findByMember_KakaoId(String kakaoId); // 이건 member.kakaoId 기준


}//interface
