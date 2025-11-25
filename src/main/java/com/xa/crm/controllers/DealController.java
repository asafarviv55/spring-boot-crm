package com.xa.crm.controllers;

import com.xa.crm.models.Deal;
import com.xa.crm.services.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    @Autowired
    private DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(
            @RequestParam String name,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long pipelineId,
            @RequestParam(required = false) String expectedCloseDate,
            @RequestParam(required = false) Long ownerId) {
        LocalDate closeDate = expectedCloseDate != null ? LocalDate.parse(expectedCloseDate) : null;
        return ResponseEntity.ok(dealService.createDeal(name, amount, companyId, contactId, pipelineId, closeDate, ownerId));
    }

    @PostMapping("/{id}/move-stage")
    public ResponseEntity<Deal> moveToStage(@PathVariable Long id, @RequestParam Long stageId) {
        return ResponseEntity.ok(dealService.moveToStage(id, stageId));
    }

    @PostMapping("/{id}/win")
    public ResponseEntity<Deal> winDeal(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.winDeal(id));
    }

    @PostMapping("/{id}/lose")
    public ResponseEntity<Deal> loseDeal(
            @PathVariable Long id,
            @RequestParam Deal.LostReason reason,
            @RequestParam(required = false) String details) {
        return ResponseEntity.ok(dealService.loseDeal(id, reason, details));
    }

    @PutMapping("/{id}/amount")
    public ResponseEntity<Deal> updateAmount(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(dealService.updateDealAmount(id, amount));
    }

    @PutMapping("/{id}/probability")
    public ResponseEntity<Deal> updateProbability(@PathVariable Long id, @RequestParam int probability) {
        return ResponseEntity.ok(dealService.updateProbability(id, probability));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Deal>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(dealService.getDealsByOwner(ownerId));
    }

    @GetMapping("/owner/{ownerId}/open")
    public ResponseEntity<List<Deal>> getOpenByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(dealService.getOpenDealsByOwner(ownerId));
    }

    @GetMapping("/pipeline/{pipelineId}")
    public ResponseEntity<List<Deal>> getByPipeline(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(dealService.getDealsByPipeline(pipelineId));
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<List<Deal>> getByStage(@PathVariable Long stageId) {
        return ResponseEntity.ok(dealService.getDealsByStage(stageId));
    }

    @GetMapping("/closing-soon")
    public ResponseEntity<List<Deal>> getClosingSoon(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(dealService.getDealsClosingSoon(days));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenue(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        BigDecimal revenue = dealService.getTotalWonRevenue(LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(Map.of("revenue", revenue, "startDate", startDate, "endDate", endDate));
    }

    @GetMapping("/weighted-pipeline")
    public ResponseEntity<Map<String, Object>> getWeightedPipeline() {
        return ResponseEntity.ok(Map.of("weightedPipeline", dealService.getWeightedPipeline()));
    }

    @GetMapping("/lost-reasons")
    public ResponseEntity<Map<String, Long>> getLostReasons() {
        return ResponseEntity.ok(dealService.getLostReasonAnalysis());
    }
}
