package com.phonebook.repository;

import com.phonebook.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select u from Contact u " +
            "where LOWER(u.firstName) LIKE CONCAT('%',LOWER(:input),'%') " +
            "or LOWER(u.secondName) LIKE CONCAT('%',LOWER(:input),'%') " +
            "or LOWER(u.phoneNumber) LIKE CONCAT('%',LOWER(:input),'%')")
    Page<Contact> findContactByInput(@Param("input") String input, Pageable pageable);
}
