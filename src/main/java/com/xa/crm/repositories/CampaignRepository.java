package com.xa.crm.repositories;

import com.xa.crm.models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(Campaign.CampaignStatus status);
    List<Campaign> findByType(Campaign.CampaignType type);
    List<Campaign> findByOwnerId(Long ownerId);

    @Query("SELECT c FROM Campaign c WHERE c.status = 'ACTIVE'")
    List<Campaign> findActiveCampaigns();

    @Query("SELECT c FROM Campaign c WHERE c.startDate <= :date AND (c.endDate IS NULL OR c.endDate >= :date)")
    List<Campaign> findCurrentCampaigns(@Param("date") LocalDate date);

    @Query("SELECT c FROM Campaign c WHERE c.status = 'ACTIVE' AND c.endDate < :date")
    List<Campaign> findCampaignsToEnd(@Param("date") LocalDate date);
}
