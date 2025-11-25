package com.xa.crm.controllers;

import com.xa.crm.models.Activity;
import com.xa.crm.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/log")
    public ResponseEntity<Activity> logActivity(
            @RequestParam Activity.ActivityType type,
            @RequestParam String subject,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long dealId,
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(activityService.logActivity(type, subject, description, contactId, companyId, dealId, ownerId));
    }

    @PostMapping("/schedule")
    public ResponseEntity<Activity> scheduleActivity(
            @RequestParam Activity.ActivityType type,
            @RequestParam String subject,
            @RequestParam String scheduledAt,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long dealId,
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(activityService.scheduleActivity(type, subject, LocalDateTime.parse(scheduledAt), durationMinutes, contactId, dealId, ownerId));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Activity> completeActivity(
            @PathVariable Long id,
            @RequestParam(required = false) String outcome,
            @RequestParam(required = false) String nextSteps) {
        return ResponseEntity.ok(activityService.completeActivity(id, outcome, nextSteps));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Activity> cancelActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.cancelActivity(id));
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity<Activity> rescheduleActivity(@PathVariable Long id, @RequestParam String newDateTime) {
        return ResponseEntity.ok(activityService.rescheduleActivity(id, LocalDateTime.parse(newDateTime)));
    }

    @GetMapping("/upcoming/{ownerId}")
    public ResponseEntity<List<Activity>> getUpcoming(@PathVariable Long ownerId, @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(activityService.getUpcomingActivities(ownerId, days));
    }

    @GetMapping("/overdue/{ownerId}")
    public ResponseEntity<List<Activity>> getOverdue(@PathVariable Long ownerId) {
        return ResponseEntity.ok(activityService.getOverdueActivities(ownerId));
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<Activity>> getByContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(activityService.getActivitiesByContact(contactId));
    }

    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<Activity>> getByDeal(@PathVariable Long dealId) {
        return ResponseEntity.ok(activityService.getActivitiesByDeal(dealId));
    }
}
