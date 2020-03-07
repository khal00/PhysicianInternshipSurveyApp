package com.khal.intern_survey.aspect;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.AdminPersonalDataService;

@Aspect
@Component
public class AclAspect {
	
	@Autowired
	AdminPersonalDataService adminPersonalDataService;

	@Autowired
	JdbcMutableAclService aclService;

	@AfterReturning(value = "execution(public com.khal.intern_survey.entity.Questionnaire com.khal.intern_survey.service."
			+ "QuestionnaireServiceImpl.saveQuestionnaire(com.khal.intern_survey.entity.Questionnaire))"
			, returning = "questionnaire")
	public void addPermissionsForUserOnNewQuest(Questionnaire questionnaire) {
		
		User user = questionnaire.getUser();
	
		ObjectIdentity oi = new ObjectIdentityImpl(questionnaire);
		Sid sid = new PrincipalSid(user.getEmail());

		MutableAcl acl = null;

		// add read, write, delete permissions if questionnaire was saved for the first time
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
	
	
	@After(value = "execution(public String com.khal.intern_survey.controller.SurveyController"
			+ ".sendQuestionnaire(..)) && args(principal,questionnaire,..)")
	public void removeDeleteAndWritePermissionAndAddReadAndDeletePermissionsForAdminAfterQuestIsSent(Principal principal
			, Questionnaire questionnaire) {

		ObjectIdentity oi = new ObjectIdentityImpl(questionnaire);
		MutableAcl acl = (MutableAcl) aclService.readAclById(oi, null);
		
		// acl.getEntries changes order every time an entry is deleted
		AtomicInteger aceIndex = new AtomicInteger(0);
		for(AccessControlEntry ace : acl.getEntries()) {
			
			if (ace.getPermission() == BasePermission.WRITE || ace.getPermission() == BasePermission.DELETE) {
				acl.deleteAce(aceIndex.get());			
			} else {
				aceIndex.incrementAndGet();
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
	
	
	@AfterReturning(value = "execution(public com.khal.intern_survey.entity.Questionnaire com.khal.intern_survey.service."
			+ "QuestionnaireServiceImpl.delete(java.lang.Long))"
			, returning = "questionnaire")
	public void deleteObjectIdentityIfQuestIsDeleted(Questionnaire questionnaire) {

	    try{
	    	ObjectIdentity oi = new ObjectIdentityImpl(questionnaire);
	        MutableAcl acl = (MutableAcl) aclService.readAclById(oi);
	        aclService.deleteAcl(acl.getObjectIdentity(), true);
	    } catch (NotFoundException e){
	        System.out.println("Could not find ACL");
	    }
	}
	
	@AfterReturning(value = "execution(public com.khal.intern_survey.entity.InternshipUnit com.khal.intern_survey.service."
			+ "InternshipUnitServiceImpl.addUnit(com.khal.intern_survey.entity.InternshipUnit))"
			, returning = "unit")
	public void addPermissionsForAdminsOnAddedUnit(InternshipUnit unit) {
	
		ObjectIdentity oi = new ObjectIdentityImpl(unit);
		
		MedicalChamberEnum medicalChamber = unit.getMedicalChamber();
		List<AdminPersonalData> admins = adminPersonalDataService.findByMedicalChamber(medicalChamber);

		MutableAcl acl = null;

		// add read, write, delete permissions if internship unit was saved for the first time
		try {
		acl = (MutableAcl) aclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			
			acl = aclService.createAcl(oi);
			
			Permission pRead = BasePermission.READ;
			Permission pWrite = BasePermission.WRITE;
			Permission pDelete = BasePermission.DELETE;		
			
			for(AdminPersonalData apd : admins) {
				Sid sid = new PrincipalSid(apd.getUser().getEmail());
				acl.insertAce(acl.getEntries().size(), pRead, sid, true);
				acl.insertAce(acl.getEntries().size(), pWrite, sid, true);
				acl.insertAce(acl.getEntries().size(), pDelete, sid, true);
			}

			aclService.updateAcl(acl);		
		}
	}
	
	@AfterReturning(value = "execution(public com.khal.intern_survey.entity.InternshipUnit com.khal.intern_survey.service."
			+ "InternshipUnitServiceImpl.delete(java.lang.Long))"
			, returning = "unit")
	public void deleteObjectIdentityIfUnitIsDeleted(InternshipUnit unit) {

	    try{
	    	ObjectIdentity oi = new ObjectIdentityImpl(unit);
	        MutableAcl acl = (MutableAcl) aclService.readAclById(oi);
	        aclService.deleteAcl(acl.getObjectIdentity(), true);
	    } catch (NotFoundException e){
	        System.out.println("Could not find ACL");
	    }
	}
}
