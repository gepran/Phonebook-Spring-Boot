package com.phonebook;

import com.phonebook.model.Contact;
import com.phonebook.repository.ContactRepository;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import org.slf4j.Logger;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PhonebookApplication.class)
public class PhonebookApplicationTests {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private BCryptPasswordEncoder oauthClientPasswordEncoder;

	@Autowired
	private BCryptPasswordEncoder userPasswordEncoder;

	@Test
	public void contextLoads() {
		List<Contact> contactList = contactRepository.findAll();
	}

	@Test
	public void testBCrypt() {
		String passowrd = "secret";

		String pass8 = userPasswordEncoder.encode(passowrd);
		String pass = passwordEncoder.encode(passowrd);

		System.out.println(oauthClientPasswordEncoder.encode("secret"));
		String base64 = new String(Base64.encodeBase64(passowrd.getBytes()));

		System.out.println(pass8);
		System.out.println(pass);
	}
}
