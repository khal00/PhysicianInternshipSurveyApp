package com.khal.intern_survey.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.khal.intern_survey.entity.Role;
import com.khal.intern_survey.entity.User;

@DataJpaTest
class UserRepositoryTest {
	
	static long startTime;
	static long endTime;
	
	User newUser;
	
	@Autowired
	UserRepository userRepository;
	
	@BeforeAll
	static void markStartTime() {
		startTime = System.currentTimeMillis();
		System.out.println("Start time: " + startTime);
	}
	
	@AfterAll
	static void calculateTime() {
		System.out.println("Elapsed time: " + (endTime - startTime));
	}
	
	@BeforeEach
	void createNewUserForEachTest() {
		newUser = new User(1L, "shi@m.com", "secretPassword", null, Arrays.asList(new Role(1, "ROLE_USER")), true, null);
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(userRepository).isNotNull();
	}
	
	@Test
	void findsUserByIdShouldReturnRightUser() {
		Optional<User> user = userRepository.findById(1L);
		assertEquals("xi@m.com", user.get().getEmail());
		endTime = System.currentTimeMillis();
	}
	
	@Test
	void findsUserByEmailShouldReturnRightUser() {
		User user = userRepository.findByEmail("min@m.com");
		assertEquals("min@m.com", user.getEmail());
		endTime = System.currentTimeMillis();
	}
	
	@Test
	void savedAndRetreivedFromDBUserIsNotNull() {
		userRepository.save(newUser);
		User user = userRepository.findByEmail("shi@m.com");
		assertNotNull(user);
	}
	
	@Sql("createUser.sql")
	@Test
	void createUserFromQueryThenDeleteShouldReturnNull() {
		User tempUser = userRepository.findByEmail("quin@m.com");
		userRepository.delete(tempUser);
		tempUser = userRepository.findByEmail("quin@m.com");
		assertNull(tempUser);
	}
	
	

}
