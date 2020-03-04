package com.khal.intern_survey.aspect;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Component;

import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.AdminPersonalDataService;
import com.khal.intern_survey.service.QuestionnaireService;

@Aspect
@Component
public class AclAspect {
	
	@Autowired
	AdminPersonalDataService adminPersonalDataService;
	
	@Autowired
	QuestionnaireService questionnaireService;

	@Autowired
	JdbcMutableAclService aclService;

	@AfterReturning(value = "execution(public com.khal.intern_survey.entity.Questionnaire com.khal.intern_survey.service."
			+ "QestionnaireServiceImpl.saveQuestionnaire(com.khal.intern_survey.entity.Questionnaire))"
			, returning = "questionnaire")
	public void addPermissionsToNewQuest(Questionnaire questionnaire) {
		
		User user = questionnaire.getUser();
	
		ObjectIdentity oi = new ObjectIdentityImpl(questionnaire);
		Sid sid = new PrincipalSid(user.getEmail());

		MutableAcl acl = null;

		// add read, write, delete permissions if questionnaire was saved for the first
		try {
		acl = (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			acl = aclService.createAcl(oi);
			
			Permission pRead = BasePermission.READ;
			Permission pWrite = BasePermission.WRITE;
			Permission pDelete = BasePermission.DELETE;		

			acl.insertAce(acl.getEntries().size(), pRead, sid, true);
			acl.insertAce(acl.getEntries().size(), pWrite, sid, true);
			acl.insertAce(acl.getEntries().size(), pDelete, sid, true);
			aclService.updateAcl(acl);		
		}

	}
	
	
	// Remove user write and delete permission, add read and delete permission for admin of appropriate medical chamber
	@After(value = "execution(public String com.khal.intern_survey.controller.SurveyController"
			+ ".sendQuestionnaire(..)) && args(principal,questionnaire,..)")
	public void removeDeleteAndWritePermissionAfterQuestIsSent(Principal principal
			, Questionnaire questionnaire) {

		ObjectIdentity oi = new ObjectIdentityImpl(questionnaire);
		MutableAcl acl = (MutableAcl) aclService.readAclById(oi, null);
		
		// acl.getEntries list changes order every time an entry is deleted
		int aceIndex = 0;
		for(AccessControlEntry ace : acl.getEntries()) {
			
			if (ace.getPermission() == BasePermission.WRITE || ace.getPermission() == BasePermission.DELETE) {
				acl.deleteAce(aceIndex);			
			} else {
				aceIndex++;
			}
		}
		
		MedicalChamberEnum medicalChamber = questionnaire.getMedicalChamber();
		List<AdminPersonalData> admins = adminPersonalDataService.findByMedicalChamber(medicalChamber);
		
		for(AdminPersonalData apd : admins) {
			Sid sid = new PrincipalSid(apd.getUser().getEmail());
			acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
			acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
		}
		
		aclService.updateAcl(acl);
	}
	
}
