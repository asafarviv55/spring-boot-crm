package com.xa.crm.repositories;

import com.xa.crm.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByCreatedById(Long userId);
    List<Task> findByContactId(Long contactId);
    List<Task> findByCompanyId(Long companyId);
    List<Task> findByDealId(Long dealId);
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByPriority(Task.TaskPriority priority);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId AND t.status NOT IN ('COMPLETED', 'DEFERRED') ORDER BY t.dueDate ASC")
    List<Task> findPendingTasksByUser(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId AND t.status NOT IN ('COMPLETED', 'DEFERRED') AND t.dueDate < :now")
    List<Task> findOverdueTasks(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId AND t.dueDate BETWEEN :start AND :end")
    List<Task> findTasksDueBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Task> findByAssignedToIdAndStatusOrderByDueDateAsc(Long userId, Task.TaskStatus status);
}
