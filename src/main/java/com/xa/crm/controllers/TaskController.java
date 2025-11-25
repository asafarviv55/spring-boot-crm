package com.xa.crm.controllers;

import com.xa.crm.models.Task;
import com.xa.crm.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Task.TaskPriority priority,
            @RequestParam(required = false) String dueDate,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long dealId,
            @RequestParam(required = false) Long assignedToId,
            @RequestParam(required = false) Long createdById) {
        LocalDateTime due = dueDate != null ? LocalDateTime.parse(dueDate) : null;
        return ResponseEntity.ok(taskService.createTask(title, description, priority, due, contactId, companyId, dealId, assignedToId, createdById));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestParam Task.TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> complete(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> start(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.startTask(id));
    }

    @PostMapping("/{id}/defer")
    public ResponseEntity<Task> defer(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deferTask(id));
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<Task> updatePriority(@PathVariable Long id, @RequestParam Task.TaskPriority priority) {
        return ResponseEntity.ok(taskService.updatePriority(id, priority));
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity<Task> reschedule(@PathVariable Long id, @RequestParam String newDueDate) {
        return ResponseEntity.ok(taskService.rescheduleTask(id, LocalDateTime.parse(newDueDate)));
    }

    @PostMapping("/{id}/reassign")
    public ResponseEntity<Task> reassign(@PathVariable Long id, @RequestParam Long assigneeId) {
        return ResponseEntity.ok(taskService.reassignTask(id, assigneeId));
    }

    @PostMapping("/{id}/reminder")
    public ResponseEntity<Task> setReminder(@PathVariable Long id, @RequestParam int minutesBefore) {
        return ResponseEntity.ok(taskService.setReminder(id, minutesBefore));
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<Task>> getPending(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getPendingTasks(userId));
    }

    @GetMapping("/overdue/{userId}")
    public ResponseEntity<List<Task>> getOverdue(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getOverdueTasks(userId));
    }

    @GetMapping("/due-today/{userId}")
    public ResponseEntity<List<Task>> getDueToday(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksDueToday(userId));
    }

    @GetMapping("/due-this-week/{userId}")
    public ResponseEntity<List<Task>> getDueThisWeek(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksDueThisWeek(userId));
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<Task>> getByContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(taskService.getTasksByContact(contactId));
    }

    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<Task>> getByDeal(@PathVariable Long dealId) {
        return ResponseEntity.ok(taskService.getTasksByDeal(dealId));
    }
}
