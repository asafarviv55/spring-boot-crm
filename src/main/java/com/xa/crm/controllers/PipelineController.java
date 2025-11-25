package com.xa.crm.controllers;

import com.xa.crm.models.Pipeline;
import com.xa.crm.models.PipelineStage;
import com.xa.crm.services.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @PostMapping
    public ResponseEntity<Pipeline> createPipeline(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "false") boolean isDefault) {
        return ResponseEntity.ok(pipelineService.createPipeline(name, description, isDefault));
    }

    @PostMapping("/default-sales")
    public ResponseEntity<Pipeline> createDefaultSalesPipeline() {
        return ResponseEntity.ok(pipelineService.createDefaultSalesPipeline());
    }

    @PostMapping("/{pipelineId}/stages")
    public ResponseEntity<PipelineStage> addStage(
            @PathVariable Long pipelineId,
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int defaultProbability,
            @RequestParam(required = false) String color,
            @RequestParam int sortOrder) {
        return ResponseEntity.ok(pipelineService.addStage(pipelineId, name, defaultProbability, color, sortOrder));
    }

    @PutMapping("/stages/{stageId}")
    public ResponseEntity<PipelineStage> updateStage(
            @PathVariable Long stageId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer defaultProbability,
            @RequestParam(required = false) String color) {
        return ResponseEntity.ok(pipelineService.updateStage(stageId, name, defaultProbability, color));
    }

    @PutMapping("/{pipelineId}/reorder-stages")
    public ResponseEntity<Void> reorderStages(
            @PathVariable Long pipelineId,
            @RequestBody List<Long> stageIds) {
        pipelineService.reorderStages(pipelineId, stageIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Pipeline>> getActivePipelines() {
        return ResponseEntity.ok(pipelineService.getActivePipelines());
    }

    @GetMapping("/default")
    public ResponseEntity<Pipeline> getDefaultPipeline() {
        return ResponseEntity.ok(pipelineService.getDefaultPipeline());
    }

    @GetMapping("/{pipelineId}/stages")
    public ResponseEntity<List<PipelineStage>> getStages(@PathVariable Long pipelineId) {
        return ResponseEntity.ok(pipelineService.getPipelineStages(pipelineId));
    }

    @DeleteMapping("/{pipelineId}")
    public ResponseEntity<Void> deactivatePipeline(@PathVariable Long pipelineId) {
        pipelineService.deactivatePipeline(pipelineId);
        return ResponseEntity.ok().build();
    }
}
