package com.phonebook.service;

import com.phonebook.model.Contact;
import com.phonebook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    public Page<Contact> findContacts(String searhText, Pageable pageable) {
        return contactRepository.findContactByInput(searhText, pageable);
    }

    public Contact update(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact create(Contact contact){
        return contactRepository.save(contact);
    }

    public void delete(Long id) {
        contactRepository.delete(id);
    }

}
