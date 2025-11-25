package com.xa.crm.services;

import com.xa.crm.models.Campaign;
import com.xa.crm.models.Contact;
import com.xa.crm.models.User;
import com.xa.crm.repositories.CampaignRepository;
import com.xa.crm.repositories.ContactRepository;
import com.xa.crm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Campaign createCampaign(String name, String description, Campaign.CampaignType type,
                                   LocalDate startDate, LocalDate endDate, BigDecimal budget, Long ownerId) {
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setDescription(description);
        campaign.setType(type);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setBudget(budget);

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId).orElse(null);
            campaign.setOwner(owner);
        }

        return campaignRepository.save(campaign);
    }

    public Campaign launchCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setStatus(Campaign.CampaignStatus.ACTIVE);
        return campaignRepository.save(campaign);
    }

    public Campaign pauseCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setStatus(Campaign.CampaignStatus.PAUSED);
        return campaignRepository.save(campaign);
    }

    public Campaign completeCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setStatus(Campaign.CampaignStatus.COMPLETED);
        return campaignRepository.save(campaign);
    }

    public Campaign addContactsToCampaign(Long campaignId, List<Long> contactIds) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        for (Long contactId : contactIds) {
            Contact contact = contactRepository.findById(contactId).orElse(null);
            if (contact != null && !campaign.getContacts().contains(contact)) {
                campaign.getContacts().add(contact);
            }
        }

        campaign.setTargetAudience(campaign.getContacts().size());
        return campaignRepository.save(campaign);
    }

    public Campaign updateMetrics(Long campaignId, Integer sent, Integer delivered,
                                  Integer opened, Integer clicked, Integer converted) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (sent != null) campaign.setSent(sent);
        if (delivered != null) campaign.setDelivered(delivered);
        if (opened != null) campaign.setOpened(opened);
        if (clicked != null) campaign.setClicked(clicked);
        if (converted != null) campaign.setConverted(converted);

        return campaignRepository.save(campaign);
    }

    public Campaign recordUnsubscribe(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setUnsubscribed(campaign.getUnsubscribed() != null ? campaign.getUnsubscribed() + 1 : 1);
        return campaignRepository.save(campaign);
    }

    public Campaign updateCosts(Long campaignId, BigDecimal actualCost, BigDecimal expectedRevenue) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        campaign.setActualCost(actualCost);
        campaign.setExpectedRevenue(expectedRevenue);

        return campaignRepository.save(campaign);
    }

    public List<Campaign> getActiveCampaigns() {
        return campaignRepository.findActiveCampaigns();
    }

    public List<Campaign> getCampaignsByType(Campaign.CampaignType type) {
        return campaignRepository.findByType(type);
    }

    public List<Campaign> getCampaignsByOwner(Long ownerId) {
        return campaignRepository.findByOwnerId(ownerId);
    }

    public Map<String, Object> getCampaignPerformance(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        Map<String, Object> performance = new HashMap<>();
        performance.put("openRate", campaign.getOpenRate());
        performance.put("clickRate", campaign.getClickRate());
        performance.put("conversionRate", campaign.getConversionRate());
        performance.put("roi", campaign.getRoi());
        performance.put("totalSent", campaign.getSent());
        performance.put("totalConverted", campaign.getConverted());

        return performance;
    }
}
