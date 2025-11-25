package com.xa.crm.repositories;

import com.xa.crm.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByContactIdOrderByCreatedAtDesc(Long contactId);
    List<Note> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
    List<Note> findByDealIdOrderByCreatedAtDesc(Long dealId);
    List<Note> findByCreatedByIdOrderByCreatedAtDesc(Long userId);
    List<Note> findByContactIdAndIsPinnedTrue(Long contactId);
    List<Note> findByCompanyIdAndIsPinnedTrue(Long companyId);
    List<Note> findByDealIdAndIsPinnedTrue(Long dealId);
}
