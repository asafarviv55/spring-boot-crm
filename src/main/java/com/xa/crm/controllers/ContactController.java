package com.xa.crm.controllers;

import com.xa.crm.models.Contact;
import com.xa.crm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<Contact> createContact(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Contact.LeadSource leadSource,
            @RequestParam(required = false) Long ownerId) {
        return ResponseEntity.ok(contactService.createContact(firstName, lastName, email, phone, jobTitle, companyId, leadSource, ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Contact> updateStatus(@PathVariable Long id, @RequestParam Contact.ContactStatus status) {
        return ResponseEntity.ok(contactService.updateContactStatus(id, status));
    }

    @PostMapping("/{id}/qualify")
    public ResponseEntity<Contact> qualify(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.qualifyContact(id));
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<Contact> convertToCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.convertToCustomer(id));
    }

    @PostMapping("/{id}/record-contact")
    public ResponseEntity<Contact> recordContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.recordContact(id));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Contact> assignOwner(@PathVariable Long id, @RequestParam Long ownerId) {
        return ResponseEntity.ok(contactService.assignOwner(id, ownerId));
    }

    @PostMapping("/{id}/assign-company")
    public ResponseEntity<Contact> assignToCompany(@PathVariable Long id, @RequestParam Long companyId) {
        return ResponseEntity.ok(contactService.assignToCompany(id, companyId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Contact>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(contactService.getContactsByCompany(companyId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Contact>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(contactService.getContactsByOwner(ownerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Contact>> getByStatus(@PathVariable Contact.ContactStatus status) {
        return ResponseEntity.ok(contactService.getContactsByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Contact>> search(@RequestParam String query) {
        return ResponseEntity.ok(contactService.searchContacts(query));
    }

    @GetMapping("/stale")
    public ResponseEntity<List<Contact>> getStaleContacts(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(contactService.getStaleContacts(days));
    }
}
