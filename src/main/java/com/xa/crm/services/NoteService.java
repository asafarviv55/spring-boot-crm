package com.xa.crm.services;

import com.xa.crm.models.*;
import com.xa.crm.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;

    public Note createNote(String content, Long contactId, Long companyId, Long dealId, Long createdById) {
        Note note = new Note();
        note.setContent(content);

        if (contactId != null) {
            note.setContact(contactRepository.findById(contactId).orElse(null));
        }
        if (companyId != null) {
            note.setCompany(companyRepository.findById(companyId).orElse(null));
        }
        if (dealId != null) {
            note.setDeal(dealRepository.findById(dealId).orElse(null));
        }
        if (createdById != null) {
            note.setCreatedBy(userRepository.findById(createdById).orElse(null));
        }

        return noteRepository.save(note);
    }

    public Note updateNote(Long noteId, String content) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setContent(content);
        return noteRepository.save(note);
    }

    public Note pinNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setPinned(true);
        return noteRepository.save(note);
    }

    public Note unpinNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setPinned(false);
        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public List<Note> getNotesByContact(Long contactId) {
        return noteRepository.findByContactIdOrderByCreatedAtDesc(contactId);
    }

    public List<Note> getNotesByCompany(Long companyId) {
        return noteRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);
    }

    public List<Note> getNotesByDeal(Long dealId) {
        return noteRepository.findByDealIdOrderByCreatedAtDesc(dealId);
    }

    public List<Note> getPinnedNotesByContact(Long contactId) {
        return noteRepository.findByContactIdAndIsPinnedTrue(contactId);
    }

    public List<Note> getPinnedNotesByCompany(Long companyId) {
        return noteRepository.findByCompanyIdAndIsPinnedTrue(companyId);
    }

    public List<Note> getPinnedNotesByDeal(Long dealId) {
        return noteRepository.findByDealIdAndIsPinnedTrue(dealId);
    }
}
