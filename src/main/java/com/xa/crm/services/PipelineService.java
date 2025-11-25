package com.xa.crm.services;

import com.xa.crm.models.Pipeline;
import com.xa.crm.models.PipelineStage;
import com.xa.crm.repositories.PipelineRepository;
import com.xa.crm.repositories.PipelineStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PipelineService {

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineStageRepository stageRepository;

    public Pipeline createPipeline(String name, String description, boolean isDefault) {
        if (isDefault) {
            pipelineRepository.findByIsDefaultTrue().ifPresent(p -> {
                p.setDefault(false);
                pipelineRepository.save(p);
            });
        }

        Pipeline pipeline = new Pipeline();
        pipeline.setName(name);
        pipeline.setDescription(description);
        pipeline.setDefault(isDefault);
        return pipelineRepository.save(pipeline);
    }

    public Pipeline createDefaultSalesPipeline() {
        Pipeline pipeline = createPipeline("Sales Pipeline", "Default sales pipeline", true);

        addStage(pipeline.getId(), "Lead", 10, "#3498db", 1);
        addStage(pipeline.getId(), "Qualified", 25, "#9b59b6", 2);
        addStage(pipeline.getId(), "Proposal", 50, "#f39c12", 3);
        addStage(pipeline.getId(), "Negotiation", 75, "#e67e22", 4);
        addStage(pipeline.getId(), "Closed Won", 100, "#27ae60", 5);

        return pipelineRepository.findById(pipeline.getId()).orElse(pipeline);
    }

    public PipelineStage addStage(Long pipelineId, String name, int defaultProbability, String color, int sortOrder) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));

        PipelineStage stage = new PipelineStage();
        stage.setName(name);
        stage.setDefaultProbability(defaultProbability);
        stage.setColor(color);
        stage.setSortOrder(sortOrder);
        stage.setPipeline(pipeline);

        return stageRepository.save(stage);
    }

    public PipelineStage updateStage(Long stageId, String name, Integer defaultProbability, String color) {
        PipelineStage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage not found"));

        if (name != null) stage.setName(name);
        if (defaultProbability != null) stage.setDefaultProbability(defaultProbability);
        if (color != null) stage.setColor(color);

        return stageRepository.save(stage);
    }

    public void reorderStages(Long pipelineId, List<Long> stageIds) {
        for (int i = 0; i < stageIds.size(); i++) {
            PipelineStage stage = stageRepository.findById(stageIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Stage not found"));
            stage.setSortOrder(i + 1);
            stageRepository.save(stage);
        }
    }

    public List<Pipeline> getActivePipelines() {
        return pipelineRepository.findByIsActiveTrue();
    }

    public Pipeline getDefaultPipeline() {
        return pipelineRepository.findByIsDefaultTrue().orElse(null);
    }

    public List<PipelineStage> getPipelineStages(Long pipelineId) {
        return stageRepository.findByPipelineIdOrderBySortOrderAsc(pipelineId);
    }

    public void deactivatePipeline(Long pipelineId) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));
        pipeline.setActive(false);
        pipelineRepository.save(pipeline);
    }
}
