package com.leets.chikahae.domain.token.repository;

import com.leets.chikahae.domain.token.entity.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTokenRepository extends JpaRepository<AccountToken, Long> {


}//interface
