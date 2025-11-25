package com.xa.crm.repositories;

import com.xa.crm.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByContactId(Long contactId);
    List<Activity> findByCompanyId(Long companyId);
    List<Activity> findByDealId(Long dealId);
    List<Activity> findByOwnerId(Long ownerId);
    List<Activity> findByType(Activity.ActivityType type);
    List<Activity> findByStatus(Activity.ActivityStatus status);

    @Query("SELECT a FROM Activity a WHERE a.owner.id = :ownerId AND a.status = 'SCHEDULED' AND a.scheduledAt BETWEEN :start AND :end ORDER BY a.scheduledAt ASC")
    List<Activity> findUpcomingActivities(@Param("ownerId") Long ownerId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT a FROM Activity a WHERE a.owner.id = :ownerId AND a.status = 'SCHEDULED' AND a.scheduledAt < :now")
    List<Activity> findOverdueActivities(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now);

    List<Activity> findByOwnerIdAndStatusOrderByScheduledAtAsc(Long ownerId, Activity.ActivityStatus status);
}
