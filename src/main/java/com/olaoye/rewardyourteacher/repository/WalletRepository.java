package com.olaoye.rewardyourteacher.repository;

import com.olaoye.rewardyourteacher.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

