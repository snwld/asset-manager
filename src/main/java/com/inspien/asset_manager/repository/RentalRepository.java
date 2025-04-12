package com.inspien.asset_manager.repository;

import com.inspien.asset_manager.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    // 사원명으로 대여 기록 조회
    List<Rental> findByUser(String user);

    // 반납되지 않은 대여만
    List<Rental> findByReturnDateIsNull();
}
