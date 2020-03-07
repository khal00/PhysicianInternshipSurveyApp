package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/chamber")
public class UnitsController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	InternshipUnitService internshipUnitService;
	
	@GetMapping("/manageUnits")
	public String showUnitsPanel(Model theModel, Principal principal) {
		
		User adminUser = userService.findByEmail(principal.getName());
		MedicalChamberEnum medicalChamber = adminUser.getAdminPersonalData().getMedicalChamber();
		List<InternshipUnit> units = internshipUnitService.findByMedicalChamber(medicalChamber);
		theModel.addAttribute("units", units);
		
		return "units_dictionary";		
	}
	
	@GetMapping("/editUnit/{id}")
	public String changeUnitName(@PathVariable ("id") Long id
			, @RequestParam (value = "newUnitName") String newName) {
		
		InternshipUnit unit = internshipUnitService.findById(id);
		internshipUnitService.editUnit(unit, newName);
	
		return "redirect:/chamber/manageUnits";
	}
	
	@PostMapping("/addUnit")
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String addNewInternshipUnit(@RequestParam ("unitName") String unitName, Principal principal) {
		
		User adminUser = userService.findByEmail(principal.getName());
		
		InternshipUnit unit = new InternshipUnit();
		unit.setName(unitName);
		unit.setMedicalChamber(adminUser.getAdminPersonalData().getMedicalChamber());
		internshipUnitService.addUnit(unit);
		
		return "redirect:/chamber/manageUnits";
	}
	
	@GetMapping("/deleteUnit/{id}")
	public String deleteUnit(@PathVariable ("id") Long id) {
		
		internshipUnitService.delete(id);
	
		return "redirect:/chamber/manageUnits";
	}

}
