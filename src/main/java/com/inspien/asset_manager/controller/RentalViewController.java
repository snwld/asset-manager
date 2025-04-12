package com.inspien.asset_manager.controller;

import com.inspien.asset_manager.entity.Asset;
import com.inspien.asset_manager.entity.AssetStatus;
import com.inspien.asset_manager.entity.Rental;
import com.inspien.asset_manager.repository.AssetRepository;
import com.inspien.asset_manager.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/rentals")
public class RentalViewController {

    private final AssetRepository assetRepository;
    private final RentalRepository rentalRepository;

    // [GET] 대여 신청 폼 화면
    @GetMapping("/new/{assetId}")
    public String rentalForm(@PathVariable Long assetId, Model model) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        Rental rental = new Rental();
        rental.setAsset(asset); // asset.id 자동 연결되도록

        model.addAttribute("asset", asset);
        model.addAttribute("rental", rental);
        return "rental-form"; // templates/rental-form.html
    }

    // [POST] 대여 신청 처리
    @PostMapping
    public String submitRental(@ModelAttribute Rental rental) {
        Asset asset = assetRepository.findById(rental.getAsset().getId())
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new RuntimeException("Asset is not available for rental");
        }

        // 상태 업데이트 및 rental 저장
        asset.setStatus(AssetStatus.IN_USE);
        assetRepository.save(asset);

        rental.setAsset(asset);
        rental.setCheckoutDate(LocalDateTime.now());
        rentalRepository.save(rental);

        return "redirect:/ui/assets";
    }
}
