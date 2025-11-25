package com.xa.crm.services;

import com.xa.crm.models.Company;
import com.xa.crm.models.Contact;
import com.xa.crm.models.User;
import com.xa.crm.repositories.CompanyRepository;
import com.xa.crm.repositories.ContactRepository;
import com.xa.crm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public Contact createContact(String firstName, String lastName, String email, String phone,
                                 String jobTitle, Long companyId, Contact.LeadSource leadSource, Long ownerId) {
        Contact contact = new Contact();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setJobTitle(jobTitle);
        contact.setLeadSource(leadSource);

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            contact.setCompany(company);
        }

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId).orElse(null);
            contact.setOwner(owner);
        }

        return contactRepository.save(contact);
    }

    public Contact updateContactStatus(Long id, Contact.ContactStatus status) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setStatus(status);
        return contactRepository.save(contact);
    }

    public Contact qualifyContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setStatus(Contact.ContactStatus.QUALIFIED);
        return contactRepository.save(contact);
    }

    public Contact convertToCustomer(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setStatus(Contact.ContactStatus.CUSTOMER);
        return contactRepository.save(contact);
    }

    public Contact recordContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setLastContactedAt(Instant.now());
        if (contact.getStatus() == Contact.ContactStatus.NEW) {
            contact.setStatus(Contact.ContactStatus.CONTACTED);
        }
        return contactRepository.save(contact);
    }

    public Contact assignOwner(Long contactId, Long ownerId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        contact.setOwner(owner);
        return contactRepository.save(contact);
    }

    public Contact assignToCompany(Long contactId, Long companyId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        contact.setCompany(company);
        return contactRepository.save(contact);
    }

    public List<Contact> getContactsByCompany(Long companyId) {
        return contactRepository.findByCompanyId(companyId);
    }

    public List<Contact> getContactsByOwner(Long ownerId) {
        return contactRepository.findByOwnerId(ownerId);
    }

    public List<Contact> getContactsByStatus(Contact.ContactStatus status) {
        return contactRepository.findByStatus(status);
    }

    public List<Contact> searchContacts(String query) {
        return contactRepository.searchContacts(query);
    }

    public List<Contact> getStaleContacts(int daysSinceLastContact) {
        Instant cutoff = Instant.now().minus(daysSinceLastContact, ChronoUnit.DAYS);
        return contactRepository.findContactsNotContactedSince(cutoff);
    }
}
