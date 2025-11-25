package com.xa.crm.repositories;

import com.xa.crm.models.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    List<Pipeline> findByIsActiveTrue();
    Optional<Pipeline> findByIsDefaultTrue();
    Optional<Pipeline> findByName(String name);
}
