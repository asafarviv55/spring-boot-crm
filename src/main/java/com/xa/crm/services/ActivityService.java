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
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    public Activity logActivity(Activity.ActivityType type, String subject, String description,
                                Long contactId, Long companyId, Long dealId, Long ownerId) {
        Activity activity = new Activity();
        activity.setType(type);
        activity.setSubject(subject);
        activity.setDescription(description);
        activity.setStatus(Activity.ActivityStatus.COMPLETED);
        activity.setCompletedAt(LocalDateTime.now());

        if (contactId != null) {
            Contact contact = contactRepository.findById(contactId).orElse(null);
            activity.setContact(contact);
            if (contact != null) {
                contact.setLastContactedAt(java.time.Instant.now());
                contactRepository.save(contact);
            }
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            activity.setCompany(company);
        }

        if (dealId != null) {
            Deal deal = dealRepository.findById(dealId).orElse(null);
            activity.setDeal(deal);
        }

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId).orElse(null);
            activity.setOwner(owner);
        }

        return activityRepository.save(activity);
    }

    public Activity scheduleActivity(Activity.ActivityType type, String subject, LocalDateTime scheduledAt,
                                     Integer durationMinutes, Long contactId, Long dealId, Long ownerId) {
        Activity activity = new Activity();
        activity.setType(type);
        activity.setSubject(subject);
        activity.setScheduledAt(scheduledAt);
        activity.setDurationMinutes(durationMinutes);
        activity.setStatus(Activity.ActivityStatus.SCHEDULED);

        if (contactId != null) {
            activity.setContact(contactRepository.findById(contactId).orElse(null));
        }
        if (dealId != null) {
            activity.setDeal(dealRepository.findById(dealId).orElse(null));
        }
        if (ownerId != null) {
            activity.setOwner(userRepository.findById(ownerId).orElse(null));
        }

        return activityRepository.save(activity);
    }

    public Activity completeActivity(Long activityId, String outcome, String nextSteps) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setStatus(Activity.ActivityStatus.COMPLETED);
        activity.setCompletedAt(LocalDateTime.now());
        activity.setOutcome(outcome);
        activity.setNextSteps(nextSteps);

        if (activity.getContact() != null) {
            activity.getContact().setLastContactedAt(java.time.Instant.now());
            contactRepository.save(activity.getContact());
        }

        return activityRepository.save(activity);
    }

    public Activity cancelActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setStatus(Activity.ActivityStatus.CANCELLED);
        return activityRepository.save(activity);
    }

    public Activity rescheduleActivity(Long activityId, LocalDateTime newDateTime) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setScheduledAt(newDateTime);
        return activityRepository.save(activity);
    }

    public List<Activity> getUpcomingActivities(Long ownerId, int daysAhead) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(daysAhead);
        return activityRepository.findUpcomingActivities(ownerId, start, end);
    }

    public List<Activity> getOverdueActivities(Long ownerId) {
        return activityRepository.findOverdueActivities(ownerId, LocalDateTime.now());
    }

    public List<Activity> getActivitiesByContact(Long contactId) {
        return activityRepository.findByContactId(contactId);
    }

    public List<Activity> getActivitiesByDeal(Long dealId) {
        return activityRepository.findByDealId(dealId);
    }
}
