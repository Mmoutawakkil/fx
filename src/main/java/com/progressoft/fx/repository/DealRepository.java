package com.progressoft.fx.repository;

import com.progressoft.fx.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    boolean existsByDealUniqueId(String dealUniqueId);
}
