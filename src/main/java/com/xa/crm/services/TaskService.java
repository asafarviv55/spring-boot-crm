package com.xa.crm.services;

import com.xa.crm.models.*;
import com.xa.crm.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(String title, String description, Task.TaskPriority priority,
                           LocalDateTime dueDate, Long contactId, Long companyId, Long dealId,
                           Long assignedToId, Long createdById) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(dueDate);

        if (contactId != null) {
            task.setContact(contactRepository.findById(contactId).orElse(null));
        }
        if (companyId != null) {
            task.setCompany(companyRepository.findById(companyId).orElse(null));
        }
        if (dealId != null) {
            task.setDeal(dealRepository.findById(dealId).orElse(null));
        }
        if (assignedToId != null) {
            task.setAssignedTo(userRepository.findById(assignedToId).orElse(null));
        }
        if (createdById != null) {
            task.setCreatedBy(userRepository.findById(createdById).orElse(null));
        }

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, Task.TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        if (status == Task.TaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
        }

        return taskRepository.save(task);
    }

    public Task completeTask(Long taskId) {
        return updateTaskStatus(taskId, Task.TaskStatus.COMPLETED);
    }

    public Task startTask(Long taskId) {
        return updateTaskStatus(taskId, Task.TaskStatus.IN_PROGRESS);
    }

    public Task deferTask(Long taskId) {
        return updateTaskStatus(taskId, Task.TaskStatus.DEFERRED);
    }

    public Task updatePriority(Long taskId, Task.TaskPriority priority) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setPriority(priority);
        return taskRepository.save(task);
    }

    public Task rescheduleTask(Long taskId, LocalDateTime newDueDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setDueDate(newDueDate);
        return taskRepository.save(task);
    }

    public Task reassignTask(Long taskId, Long newAssigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User assignee = userRepository.findById(newAssigneeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(assignee);
        return taskRepository.save(task);
    }

    public Task setReminder(Long taskId, int minutesBefore) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setReminderMinutesBefore(minutesBefore);
        task.setReminderSent(false);

        return taskRepository.save(task);
    }

    public List<Task> getPendingTasks(Long userId) {
        return taskRepository.findPendingTasksByUser(userId);
    }

    public List<Task> getOverdueTasks(Long userId) {
        return taskRepository.findOverdueTasks(userId, LocalDateTime.now());
    }

    public List<Task> getTasksDueToday(Long userId) {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime end = start.plusDays(1);
        return taskRepository.findTasksDueBetween(userId, start, end);
    }

    public List<Task> getTasksDueThisWeek(Long userId) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        return taskRepository.findTasksDueBetween(userId, start, end);
    }

    public List<Task> getTasksByContact(Long contactId) {
        return taskRepository.findByContactId(contactId);
    }

    public List<Task> getTasksByDeal(Long dealId) {
        return taskRepository.findByDealId(dealId);
    }
}
