package com.xa.crm.repositories;

import com.xa.crm.models.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Optional<Quote> findByQuoteNumber(String quoteNumber);
    List<Quote> findByDealId(Long dealId);
    List<Quote> findByContactId(Long contactId);
    List<Quote> findByCompanyId(Long companyId);
    List<Quote> findByStatus(Quote.QuoteStatus status);
    List<Quote> findByCreatedById(Long userId);

    @Query("SELECT q FROM Quote q WHERE q.status = 'SENT' AND q.validUntil < :date")
    List<Quote> findExpiredQuotes(@Param("date") LocalDate date);

    @Query("SELECT q FROM Quote q WHERE q.status = 'DRAFT' AND q.createdBy.id = :userId")
    List<Quote> findDraftsByUser(@Param("userId") Long userId);
}
