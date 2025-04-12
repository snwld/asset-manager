package com.inspien.asset_manager.controller;

import com.inspien.asset_manager.entity.Asset;
import com.inspien.asset_manager.entity.AssetStatus;
import com.inspien.asset_manager.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetRepository assetRepository;

    // Create (이미 있는 부분)
    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        return assetRepository.save(asset);
    }

    // ✅ 1. GET - 전체 자산 목록
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // ✅ 1. GET - 특정 자산 조회
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        return assetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 2. PUT - 자산 수정
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset updatedAsset) {
        return assetRepository.findById(id).map(asset -> {
            asset.setName(updatedAsset.getName());
            asset.setSerialNumber(updatedAsset.getSerialNumber());
            asset.setStatus(updatedAsset.getStatus());
            asset.setCpu(updatedAsset.getCpu());
            asset.setMemory(updatedAsset.getMemory());
            asset.setHdd(updatedAsset.getHdd());
            asset.setMemo(updatedAsset.getMemo());
            return ResponseEntity.ok(assetRepository.save(asset));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ 3. DELETE - 자산 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAsset(@PathVariable Long id) {
        return assetRepository.findById(id).map(asset -> {
            assetRepository.delete(asset);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. PATCH - 자산 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<Asset> updateAssetStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        String newStatus = updates.get("status");
        if (newStatus == null) {
            return ResponseEntity.badRequest().build(); // status 없으면 400 에러
        }

        try {
            asset.setStatus(AssetStatus.valueOf(newStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 enum 값
        }

        Asset updatedAsset = assetRepository.save(asset);
        return ResponseEntity.ok(updatedAsset);
    }


}
