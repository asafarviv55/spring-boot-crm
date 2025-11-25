package com.xa.crm.services;

import com.xa.crm.models.*;
import com.xa.crm.repositories.*;
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
public class DealService {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineStageRepository stageRepository;

    @Autowired
    private UserRepository userRepository;

    public Deal createDeal(String name, BigDecimal amount, Long companyId, Long contactId,
                           Long pipelineId, LocalDate expectedCloseDate, Long ownerId) {
        Deal deal = new Deal();
        deal.setName(name);
        deal.setAmount(amount);
        deal.setExpectedCloseDate(expectedCloseDate);

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            deal.setCompany(company);
        }

        if (contactId != null) {
            Contact contact = contactRepository.findById(contactId).orElse(null);
            deal.setPrimaryContact(contact);
        }

        Pipeline pipeline;
        if (pipelineId != null) {
            pipeline = pipelineRepository.findById(pipelineId).orElse(null);
        } else {
            pipeline = pipelineRepository.findByIsDefaultTrue().orElse(null);
        }
        deal.setPipeline(pipeline);

        if (pipeline != null) {
            List<PipelineStage> stages = stageRepository.findByPipelineIdOrderBySortOrderAsc(pipeline.getId());
            if (!stages.isEmpty()) {
                deal.setStage(stages.get(0));
                deal.setProbability(stages.get(0).getDefaultProbability());
            }
        }

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId).orElse(null);
            deal.setOwner(owner);
        }

        return dealRepository.save(deal);
    }

    public Deal moveToStage(Long dealId, Long stageId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
        PipelineStage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage not found"));

        deal.setStage(stage);
        if (stage.getDefaultProbability() != null) {
            deal.setProbability(stage.getDefaultProbability());
        }

        return dealRepository.save(deal);
    }

    public Deal winDeal(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        deal.setStatus(Deal.DealStatus.WON);
        deal.setProbability(100);
        deal.setActualCloseDate(LocalDate.now());

        // Convert contact and company to customer
        if (deal.getPrimaryContact() != null) {
            deal.getPrimaryContact().setStatus(Contact.ContactStatus.CUSTOMER);
        }
        if (deal.getCompany() != null) {
            deal.getCompany().setType(Company.CompanyType.CUSTOMER);
        }

        return dealRepository.save(deal);
    }

    public Deal loseDeal(Long dealId, Deal.LostReason reason, String details) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        deal.setStatus(Deal.DealStatus.LOST);
        deal.setProbability(0);
        deal.setActualCloseDate(LocalDate.now());
        deal.setLostReason(reason);
        deal.setLostReasonDetails(details);

        return dealRepository.save(deal);
    }

    public Deal updateDealAmount(Long dealId, BigDecimal amount) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        deal.setAmount(amount);
        return dealRepository.save(deal);
    }

    public Deal updateProbability(Long dealId, int probability) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        deal.setProbability(probability);
        return dealRepository.save(deal);
    }

    public List<Deal> getDealsByOwner(Long ownerId) {
        return dealRepository.findByOwnerId(ownerId);
    }

    public List<Deal> getOpenDealsByOwner(Long ownerId) {
        return dealRepository.findOpenDealsByOwner(ownerId);
    }

    public List<Deal> getDealsByPipeline(Long pipelineId) {
        return dealRepository.findByPipelineId(pipelineId);
    }

    public List<Deal> getDealsByStage(Long stageId) {
        return dealRepository.findByStageId(stageId);
    }

    public List<Deal> getDealsClosingSoon(int daysAhead) {
        return dealRepository.findDealsClosingSoon(LocalDate.now().plusDays(daysAhead));
    }

    public BigDecimal getTotalWonRevenue(LocalDate startDate, LocalDate endDate) {
        BigDecimal total = dealRepository.getTotalWonAmount(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getWeightedPipeline() {
        BigDecimal total = dealRepository.getTotalWeightedPipeline();
        return total != null ? total : BigDecimal.ZERO;
    }

    public Map<String, Long> getLostReasonAnalysis() {
        List<Object[]> stats = dealRepository.getLostReasonStats();
        Map<String, Long> result = new HashMap<>();
        for (Object[] stat : stats) {
            String reason = stat[0] != null ? stat[0].toString() : "Unknown";
            Long count = (Long) stat[1];
            result.put(reason, count);
        }
        return result;
    }
}
