package com.khal.intern_survey.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.khal.intern_survey.DTO.MedicalChamberEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
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
	
	private Status status = Status.DRAFT;
	
	private MedicalChamberEnum medicalChamber;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="unit_id")
	private InternshipUnit unit;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private LocalDateTime createTime;
	
}
