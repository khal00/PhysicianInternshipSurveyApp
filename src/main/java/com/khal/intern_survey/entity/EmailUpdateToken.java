package com.khal.intern_survey.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "email_update_token")
public class EmailUpdateToken {
	
	private static final int EXPIRATION = 60 * 72;
	 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
     
    @Column(name = "token")
    private String token;
   
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
     
    @Column(name = "expiry_date")
    private Date expiryDate;
    
    @Column(name = "new_email")
    private String newEmail;
    
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

	public EmailUpdateToken(String token, User user, String newEmail) {
		super();
		this.token = token;
		this.user = user;
		this.newEmail = newEmail;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}
    
}
