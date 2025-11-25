package com.xa.crm.controllers;

import com.xa.crm.models.Quote;
import com.xa.crm.models.QuoteItem;
import com.xa.crm.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @PostMapping
    public ResponseEntity<Quote> createQuote(
            @RequestParam String title,
            @RequestParam(required = false) Long dealId,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String validUntil,
            @RequestParam(required = false) Long createdById) {
        LocalDate validDate = validUntil != null ? LocalDate.parse(validUntil) : null;
        return ResponseEntity.ok(quoteService.createQuote(title, dealId, contactId, companyId, validDate, createdById));
    }

    @PostMapping("/{quoteId}/items")
    public ResponseEntity<QuoteItem> addLineItem(
            @PathVariable Long quoteId,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) BigDecimal unitPrice,
            @RequestParam(required = false) BigDecimal discountPercent,
            @RequestParam(required = false) BigDecimal taxRate) {
        return ResponseEntity.ok(quoteService.addLineItem(quoteId, productId, quantity, unitPrice, discountPercent, taxRate));
    }

    @DeleteMapping("/{quoteId}/items/{itemId}")
    public ResponseEntity<Quote> removeLineItem(@PathVariable Long quoteId, @PathVariable Long itemId) {
        return ResponseEntity.ok(quoteService.removeLineItem(quoteId, itemId));
    }

    @PostMapping("/{quoteId}/discount")
    public ResponseEntity<Quote> applyDiscount(@PathVariable Long quoteId, @RequestParam BigDecimal discountPercent) {
        return ResponseEntity.ok(quoteService.applyDiscount(quoteId, discountPercent));
    }

    @PostMapping("/{quoteId}/send")
    public ResponseEntity<Quote> sendQuote(@PathVariable Long quoteId) {
        return ResponseEntity.ok(quoteService.sendQuote(quoteId));
    }

    @PostMapping("/{quoteId}/accept")
    public ResponseEntity<Quote> acceptQuote(@PathVariable Long quoteId) {
        return ResponseEntity.ok(quoteService.acceptQuote(quoteId));
    }

    @PostMapping("/{quoteId}/reject")
    public ResponseEntity<Quote> rejectQuote(@PathVariable Long quoteId) {
        return ResponseEntity.ok(quoteService.rejectQuote(quoteId));
    }

    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<Quote>> getByDeal(@PathVariable Long dealId) {
        return ResponseEntity.ok(quoteService.getQuotesByDeal(dealId));
    }

    @GetMapping("/drafts/{userId}")
    public ResponseEntity<List<Quote>> getDrafts(@PathVariable Long userId) {
        return ResponseEntity.ok(quoteService.getDraftQuotes(userId));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Quote>> getExpired() {
        return ResponseEntity.ok(quoteService.getExpiredQuotes());
    }
}
