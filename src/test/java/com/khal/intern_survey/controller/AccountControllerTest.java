package com.khal.intern_survey.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.BindingResult;

import com.khal.intern_survey.dto.EmailDTO;
import com.khal.intern_survey.entity.Role;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.EmailService;
import com.khal.intern_survey.service.UserService;

@WebMvcTest(AccountController.class)
@WithMockUser
class AccountControllerTest {
	
	private User user;
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@MockBean
	private EmailService emailService;
	
	@BeforeEach
	void initialize() {
		user = new User(1L, "myemail@mail.com", "secretPassword", null, Arrays.asList(new Role(1, "ROLE_USER")), true, null);
	}
	
	@Test
	@WithMockUser(roles = "MEDICALCHAMBERADMIN")
	public void shouldReturnUserPanelForOilAdmin() throws Exception {
		mockMvc.perform(get("/user/showUserPanel"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Manage units")));
	}
	
	@Test
	@WithAnonymousUser
	void shouldReturnUserPanel() throws Exception {
		mockMvc.perform(get("/user/showUserPanel"))
		.andExpect(status().is3xxRedirection());
	}
	
	@Test
	void shouldReturnAccountSettingPageContainingUserEmailAddress() throws Exception {
		when(userService.findByEmail(anyString())).thenReturn(user);
		mockMvc.perform(get("/user/accountSettings"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("myemail@mail.com")));
		
	}
	
	@Test
	void testEmailUpdate_EmailDtoHasNoErrors() throws Exception {
		EmailDTO emailDto = new EmailDTO("updated@mail.com");
		mockMvc.perform(post("/user/updateEmail")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("emailDTO", emailDto)
				).andExpect(ResultMatcher.matchAll(
						redirectedUrl("/user/accountSettings"),
						flash().attributeExists("infomessage")
						));		
	}
	
	@Test
	void testEmailUpdate_EmailDtoHasErrors() throws Exception {
		EmailDTO emailDto = new EmailDTO("");
		mockMvc.perform(post("/user/updateEmail")
				.with(csrf())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("emailDTO", emailDto)
				).andExpect(ResultMatcher.matchAll(
						flash().attributeExists("org.springframework.validation.BindingResult.emailDTO"),
						redirectedUrl("/user/accountSettings")
						));
	}



}
