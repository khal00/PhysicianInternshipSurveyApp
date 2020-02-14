package com.khal.intern_survey.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.khal.intern_survey.DTO.MedicalChamberEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
	

	@Getter @AllArgsConstructor
	public enum Status{
		DRAFT("Draft"), SENT("Sent"), ACCEPTED("Accepted");
		private String name;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String verificationId;
	
	private Status status = Status.DRAFT;
	
	private MedicalChamberEnum medicalChamber;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="unit_id")
	private InternshipUnit unit;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "questionnaire")
	private List<InternshipSection> sections;
	
	@OneToMany(mappedBy = "questionnaire")
	private List<Course> courses;
	
	private LocalDateTime createTime;
	
	private LocalDate sendDate;
	
	private String Coordinator;
	
	private int coordinatorRating;

	public Questionnaire(Long id, User user, LocalDateTime createTime) {
		this.id = id;
		this.status = Status.DRAFT;
		this.user = user;
		this.createTime = createTime;
	}
}
