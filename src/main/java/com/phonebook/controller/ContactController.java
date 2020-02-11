package com.phonebook.controller;

import com.phonebook.model.Contact;
import com.phonebook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> delete(@RequestBody(required = true) Contact contact) {
        contactService.delete(contact.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> edit(@RequestBody(required = true) Contact contact) {
        contactService.update(contact);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> create(@RequestBody(required = true) Contact contact) {
        contactService.create(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Page<Contact> getPaginatedList(
            @RequestParam(required = false, defaultValue = "") String searchExpression,
            @RequestParam(required = false, defaultValue = "firstName") String sortField,
            @RequestParam(required = false, defaultValue = "true") boolean isAscending,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        Sort sort = new Sort(isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);

        if(searchExpression == null || searchExpression.isEmpty()) {
            return contactService.findAll(new PageRequest(pageNumber, pageSize, sort));
        }
        return contactService.findContacts(searchExpression, new PageRequest(pageNumber, pageSize, sort));
    }
}
