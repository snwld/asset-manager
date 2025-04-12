package com.inspien.asset_manager.controller;

import com.inspien.asset_manager.entity.Asset;
import com.inspien.asset_manager.entity.AssetStatus;
import com.inspien.asset_manager.entity.Rental;
import com.inspien.asset_manager.repository.AssetRepository;
import com.inspien.asset_manager.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalRepository rentalRepository;
    private final AssetRepository assetRepository;

    // 대여 등록
    @PostMapping
    public Rental createRental(@RequestParam Long assetId, @RequestParam String user, @RequestParam String dueDate) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new RuntimeException("Asset is not available for rental");
        }

        Rental rental = Rental.builder()
                .asset(asset)
                .user(user)
                .checkoutDate(LocalDateTime.now())
                .dueDate(LocalDateTime.parse(dueDate))
                .build();

        // 자산 상태 변경
        asset.setStatus(AssetStatus.IN_USE);
        assetRepository.save(asset);

        return rentalRepository.save(rental);
    }

    // 반납 처리
    @PostMapping("/{rentalId}/return")
    public Rental returnRental(@PathVariable Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setReturnDate(LocalDateTime.now());
        rentalRepository.save(rental);

        Asset asset = rental.getAsset();
        asset.setStatus(AssetStatus.AVAILABLE);
        assetRepository.save(asset);

        return rental;
    }

    // 대여중인 장비 목록
    @GetMapping("/in-use")
    public List<Rental> getInUseRentals() {
        return rentalRepository.findByReturnDateIsNull();
    }

    // 대여 연장
    @PutMapping("/{rentalId}/extend")
    public Rental extendRental(@PathVariable Long rentalId, @RequestParam String newDueDate) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rental.setDueDate(LocalDateTime.parse(newDueDate));
        return rentalRepository.save(rental);
    }

    // 특정 사원 대여 이력
    @GetMapping("/user/{user}")
    public List<Rental> getRentalsByUser(@PathVariable String user) {
        return rentalRepository.findByUser(user);
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

}
