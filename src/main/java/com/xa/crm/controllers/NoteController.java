package com.xa.crm.controllers;

import com.xa.crm.models.Note;
import com.xa.crm.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> createNote(
            @RequestParam String content,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long dealId,
            @RequestParam(required = false) Long createdById) {
        return ResponseEntity.ok(noteService.createNote(content, contactId, companyId, dealId, createdById));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestParam String content) {
        return ResponseEntity.ok(noteService.updateNote(id, content));
    }

    @PostMapping("/{id}/pin")
    public ResponseEntity<Note> pinNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.pinNote(id));
    }

    @PostMapping("/{id}/unpin")
    public ResponseEntity<Note> unpinNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.unpinNote(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<Note>> getByContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(noteService.getNotesByContact(contactId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Note>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(noteService.getNotesByCompany(companyId));
    }

    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<Note>> getByDeal(@PathVariable Long dealId) {
        return ResponseEntity.ok(noteService.getNotesByDeal(dealId));
    }

    @GetMapping("/contact/{contactId}/pinned")
    public ResponseEntity<List<Note>> getPinnedByContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(noteService.getPinnedNotesByContact(contactId));
    }

    @GetMapping("/company/{companyId}/pinned")
    public ResponseEntity<List<Note>> getPinnedByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(noteService.getPinnedNotesByCompany(companyId));
    }

    @GetMapping("/deal/{dealId}/pinned")
    public ResponseEntity<List<Note>> getPinnedByDeal(@PathVariable Long dealId) {
        return ResponseEntity.ok(noteService.getPinnedNotesByDeal(dealId));
    }
}
