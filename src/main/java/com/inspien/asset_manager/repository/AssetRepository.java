package com.inspien.asset_manager.repository;

import com.inspien.asset_manager.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // 상태별 필터링용
    List<Asset> findByStatus(com.inspien.asset_manager.entity.AssetStatus status);

    // 시리얼 넘버 중복 확인용
    boolean existsBySerialNumber(String serialNumber);
}
