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


    //회원탈퇴 (주석: 필요할 수도 있음)
//    @Modifying
//    @Query("DELETE FROM AccountToken t WHERE t.member.id = :memberId")
    void deleteByMemberId(Long memberId);


}//interface
