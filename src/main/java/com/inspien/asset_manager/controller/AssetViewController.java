package com.inspien.asset_manager.controller;

import com.inspien.asset_manager.entity.Asset;
import com.inspien.asset_manager.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AssetViewController {

    private final AssetRepository assetRepository;

    // 자산 목록
    @GetMapping("/ui/assets")
    public String listAssets(Model model) {
        model.addAttribute("assets", assetRepository.findAll());
        return "asset-list";
    }

    // 자산 등록 폼
    @GetMapping("/ui/assets/new")
    public String newAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        return "asset-form";
    }

    // 자산 저장
    @PostMapping("/ui/assets")
    public String saveAsset(@ModelAttribute Asset asset) {
        assetRepository.save(asset);
        return "redirect:/ui/assets";
    }
}
