package com.khal.intern_survey.config;


import java.io.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.khal.intern_survey.rest.CourseRating;
import com.khal.intern_survey.rest.SectionRating;
import com.khal.intern_survey.rest.UnitRating;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    return slr;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("lang");
	    return lci;
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	public LayoutDialect layoutDialect() {
	    return new LayoutDialect();
	}
	
	@Bean
	public File fontFile() {
		return new File("src/main/resources/static/font/AbhayaLibre-Regular.ttf");
	}
	
	@Bean
	UnitRating unitRating() {
		UnitRating unitRating = new UnitRating();
		return unitRating;
	}
	
	@Bean
	CourseRating coursesRating() {
		CourseRating coursesRating = new CourseRating();
		return coursesRating;
	}
	
	@Bean
	SectionRating sectionRating() {
		SectionRating coursesRating = new SectionRating();
		return coursesRating;
	}

}
