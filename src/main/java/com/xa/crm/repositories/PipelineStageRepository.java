package com.xa.crm.repositories;

import com.xa.crm.models.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PipelineStageRepository extends JpaRepository<PipelineStage, Long> {
    List<PipelineStage> findByPipelineIdOrderBySortOrderAsc(Long pipelineId);
    List<PipelineStage> findByPipelineId(Long pipelineId);
}
