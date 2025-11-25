package com.xa.crm.services;

import com.xa.crm.models.*;
import com.xa.crm.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteItemRepository quoteItemRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Quote createQuote(String title, Long dealId, Long contactId, Long companyId,
                             LocalDate validUntil, Long createdById) {
        Quote quote = new Quote();
        quote.setTitle(title);
        quote.setQuoteNumber("QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        quote.setValidUntil(validUntil);

        if (dealId != null) {
            Deal deal = dealRepository.findById(dealId).orElse(null);
            quote.setDeal(deal);
            if (deal != null && contactId == null) {
                quote.setContact(deal.getPrimaryContact());
            }
            if (deal != null && companyId == null) {
                quote.setCompany(deal.getCompany());
            }
        }

        if (contactId != null) {
            quote.setContact(contactRepository.findById(contactId).orElse(null));
        }
        if (companyId != null) {
            quote.setCompany(companyRepository.findById(companyId).orElse(null));
        }
        if (createdById != null) {
            quote.setCreatedBy(userRepository.findById(createdById).orElse(null));
        }

        return quoteRepository.save(quote);
    }

    public QuoteItem addLineItem(Long quoteId, Long productId, Integer quantity, BigDecimal unitPrice,
                                 BigDecimal discountPercent, BigDecimal taxRate) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        QuoteItem item = new QuoteItem();
        item.setQuote(quote);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setDiscountPercent(discountPercent);
        item.setTaxRate(taxRate);
        item.setSortOrder(quote.getItems().size() + 1);

        if (productId != null) {
            Product product = productRepository.findById(productId).orElse(null);
            item.setProduct(product);
            if (product != null) {
                item.setDescription(product.getName());
                if (unitPrice == null) item.setUnitPrice(product.getPrice());
                if (taxRate == null) item.setTaxRate(product.getTaxRate());
            }
        }

        QuoteItem savedItem = quoteItemRepository.save(item);
        quote.getItems().add(savedItem);
        quote.calculateTotals();
        quoteRepository.save(quote);

        return savedItem;
    }

    public Quote removeLineItem(Long quoteId, Long itemId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.getItems().removeIf(item -> item.getId().equals(itemId));
        quoteItemRepository.deleteById(itemId);
        quote.calculateTotals();

        return quoteRepository.save(quote);
    }

    public Quote applyDiscount(Long quoteId, BigDecimal discountPercent) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setDiscountPercent(discountPercent);
        quote.calculateTotals();

        return quoteRepository.save(quote);
    }

    public Quote sendQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setStatus(Quote.QuoteStatus.SENT);
        quote.setIssuedAt(LocalDate.now());

        return quoteRepository.save(quote);
    }

    public Quote acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setStatus(Quote.QuoteStatus.ACCEPTED);

        if (quote.getDeal() != null) {
            Deal deal = quote.getDeal();
            deal.setAmount(quote.getTotalAmount());
            dealRepository.save(deal);
        }

        return quoteRepository.save(quote);
    }

    public Quote rejectQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setStatus(Quote.QuoteStatus.REJECTED);
        return quoteRepository.save(quote);
    }

    public List<Quote> getQuotesByDeal(Long dealId) {
        return quoteRepository.findByDealId(dealId);
    }

    public List<Quote> getDraftQuotes(Long userId) {
        return quoteRepository.findDraftsByUser(userId);
    }

    public List<Quote> getExpiredQuotes() {
        return quoteRepository.findExpiredQuotes(LocalDate.now());
    }
}
