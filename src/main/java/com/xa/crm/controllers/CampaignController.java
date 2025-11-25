package com.xa.crm.controllers;

import com.xa.crm.models.Campaign;
import com.xa.crm.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping
    public ResponseEntity<Campaign> createCampaign(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Campaign.CampaignType type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) BigDecimal budget,
            @RequestParam(required = false) Long ownerId) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        return ResponseEntity.ok(campaignService.createCampaign(name, description, type, start, end, budget, ownerId));
    }

    @PostMapping("/{id}/launch")
    public ResponseEntity<Campaign> launch(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.launchCampaign(id));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<Campaign> pause(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.pauseCampaign(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Campaign> complete(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.completeCampaign(id));
    }

    @PostMapping("/{id}/add-contacts")
    public ResponseEntity<Campaign> addContacts(@PathVariable Long id, @RequestBody List<Long> contactIds) {
        return ResponseEntity.ok(campaignService.addContactsToCampaign(id, contactIds));
    }

    @PutMapping("/{id}/metrics")
    public ResponseEntity<Campaign> updateMetrics(
            @PathVariable Long id,
            @RequestParam(required = false) Integer sent,
            @RequestParam(required = false) Integer delivered,
            @RequestParam(required = false) Integer opened,
            @RequestParam(required = false) Integer clicked,
            @RequestParam(required = false) Integer converted) {
        return ResponseEntity.ok(campaignService.updateMetrics(id, sent, delivered, opened, clicked, converted));
    }

    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Campaign> recordUnsubscribe(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.recordUnsubscribe(id));
    }

    @PutMapping("/{id}/costs")
    public ResponseEntity<Campaign> updateCosts(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal actualCost,
            @RequestParam(required = false) BigDecimal expectedRevenue) {
        return ResponseEntity.ok(campaignService.updateCosts(id, actualCost, expectedRevenue));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Campaign>> getActive() {
        return ResponseEntity.ok(campaignService.getActiveCampaigns());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Campaign>> getByType(@PathVariable Campaign.CampaignType type) {
        return ResponseEntity.ok(campaignService.getCampaignsByType(type));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Campaign>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(campaignService.getCampaignsByOwner(ownerId));
    }

    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Object>> getPerformance(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignPerformance(id));
    }
}
