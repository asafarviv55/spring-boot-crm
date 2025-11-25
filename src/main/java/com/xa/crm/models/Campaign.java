package com.xa.crm.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal budget;
    private BigDecimal actualCost;
    private BigDecimal expectedRevenue;

    private Integer targetAudience;
    private Integer sent;
    private Integer delivered;
    private Integer opened;
    private Integer clicked;
    private Integer converted;
    private Integer unsubscribed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "campaign_contacts",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Contact> contacts = new ArrayList<>();

    private Instant createdAt;
    private Instant updatedAt;

    public enum CampaignType {
        EMAIL, SOCIAL_MEDIA, ADVERTISING, WEBINAR, TRADE_SHOW, DIRECT_MAIL, OTHER
    }

    public enum CampaignStatus {
        PLANNING, ACTIVE, PAUSED, COMPLETED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) status = CampaignStatus.PLANNING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public BigDecimal getOpenRate() {
        if (delivered == null || delivered == 0 || opened == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(opened).divide(BigDecimal.valueOf(delivered), 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getClickRate() {
        if (opened == null || opened == 0 || clicked == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(clicked).divide(BigDecimal.valueOf(opened), 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getConversionRate() {
        if (clicked == null || clicked == 0 || converted == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(converted).divide(BigDecimal.valueOf(clicked), 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getRoi() {
        if (actualCost == null || actualCost.compareTo(BigDecimal.ZERO) == 0 || expectedRevenue == null) return BigDecimal.ZERO;
        return expectedRevenue.subtract(actualCost).divide(actualCost, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CampaignType getType() { return type; }
    public void setType(CampaignType type) { this.type = type; }
    public CampaignStatus getStatus() { return status; }
    public void setStatus(CampaignStatus status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public BigDecimal getActualCost() { return actualCost; }
    public void setActualCost(BigDecimal actualCost) { this.actualCost = actualCost; }
    public BigDecimal getExpectedRevenue() { return expectedRevenue; }
    public void setExpectedRevenue(BigDecimal expectedRevenue) { this.expectedRevenue = expectedRevenue; }
    public Integer getTargetAudience() { return targetAudience; }
    public void setTargetAudience(Integer targetAudience) { this.targetAudience = targetAudience; }
    public Integer getSent() { return sent; }
    public void setSent(Integer sent) { this.sent = sent; }
    public Integer getDelivered() { return delivered; }
    public void setDelivered(Integer delivered) { this.delivered = delivered; }
    public Integer getOpened() { return opened; }
    public void setOpened(Integer opened) { this.opened = opened; }
    public Integer getClicked() { return clicked; }
    public void setClicked(Integer clicked) { this.clicked = clicked; }
    public Integer getConverted() { return converted; }
    public void setConverted(Integer converted) { this.converted = converted; }
    public Integer getUnsubscribed() { return unsubscribed; }
    public void setUnsubscribed(Integer unsubscribed) { this.unsubscribed = unsubscribed; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
