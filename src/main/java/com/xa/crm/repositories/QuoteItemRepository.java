package com.xa.crm.repositories;

import com.xa.crm.models.QuoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuoteItemRepository extends JpaRepository<QuoteItem, Long> {
    List<QuoteItem> findByQuoteIdOrderBySortOrderAsc(Long quoteId);
    List<QuoteItem> findByProductId(Long productId);
    void deleteByQuoteId(Long quoteId);
}
