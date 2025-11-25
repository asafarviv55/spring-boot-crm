package com.xa.crm.repositories;

import com.xa.crm.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmail(String email);
    List<Contact> findByCompanyId(Long companyId);
    List<Contact> findByOwnerId(Long ownerId);
    List<Contact> findByStatus(Contact.ContactStatus status);
    List<Contact> findByLeadSource(Contact.LeadSource leadSource);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Contact> searchContacts(@Param("query") String query);

    @Query("SELECT c FROM Contact c WHERE c.lastContactedAt < :date OR c.lastContactedAt IS NULL")
    List<Contact> findContactsNotContactedSince(@Param("date") Instant date);

    List<Contact> findByCompanyIdAndStatus(Long companyId, Contact.ContactStatus status);
}
