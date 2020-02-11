package com.phonebook;

import com.phonebook.model.Contact;
import com.phonebook.repository.ContactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactServiceTest {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testContactSearch() {
        //given
        Contact testContact = new Contact("Fname", "SecondName", "556689");
        Contact testContact2 = new Contact("Zhora", "Ivanov", "123456");
        Contact testContact3 = new Contact("Leva", "Petrovich", "304151");
        Contact testContact4 = new Contact("James", "Bond", "449930");
        Contact testContact5 = new Contact("Nona", "Kuxianidze", "375212");
        Contact testContact6 = new Contact("Ani", "Nona", "375213");
        Contact testContact7 = new Contact("Nona", "Nona", "375214");
        Contact testContact8 = new Contact("Nona", "Nona", "375215");
        entityManager.persist(testContact);
        entityManager.persist(testContact2);
        entityManager.persist(testContact3);
        entityManager.persist(testContact4);
        entityManager.persist(testContact5);
        entityManager.persist(testContact6);
        entityManager.persist(testContact7);
        entityManager.persist(testContact8);
        entityManager.flush();


        String search = "lev";
        //contactRepository.findContactByInput(search);

        System.out.println("try");
        /*List<Contact> result1 = contactRepository.findContactsByFirstNameOrSecondNameOrPhoneNumber(Optional.ofNullable(search), Optional.ofNullable(search), Optional.ofNullable(search));

        search = "Nona";
        result1 = contactRepository.findContactsByFirstNameOrSecondNameOrPhoneNumber(Optional.ofNullable(search), Optional.ofNullable(search), Optional.ofNullable(search));

        search = null;
        result1 = contactRepository.findContactsByFirstNameOrSecondNameOrPhoneNumber(Optional.ofNullable(search), Optional.ofNullable(search), Optional.ofNullable(search));*/

    }
}
