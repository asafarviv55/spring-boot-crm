package com.xa.crm.repositories;

import com.xa.crm.models.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByOwnerId(Long ownerId);
    List<Deal> findByCompanyId(Long companyId);
    List<Deal> findByPrimaryContactId(Long contactId);
    List<Deal> findByPipelineId(Long pipelineId);
    List<Deal> findByStageId(Long stageId);
    List<Deal> findByStatus(Deal.DealStatus status);

    @Query("SELECT d FROM Deal d WHERE d.status = 'OPEN' AND d.expectedCloseDate <= :date")
    List<Deal> findDealsClosingSoon(@Param("date") LocalDate date);

    @Query("SELECT d FROM Deal d WHERE d.status = 'OPEN' AND d.owner.id = :ownerId")
    List<Deal> findOpenDealsByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT SUM(d.amount) FROM Deal d WHERE d.status = 'WON' AND d.actualCloseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalWonAmount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(d.amount * d.probability / 100) FROM Deal d WHERE d.status = 'OPEN'")
    BigDecimal getTotalWeightedPipeline();

    @Query("SELECT d.lostReason, COUNT(d) FROM Deal d WHERE d.status = 'LOST' GROUP BY d.lostReason")
    List<Object[]> getLostReasonStats();
}
