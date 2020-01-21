package com.khal.intern_survey.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	InternshipUnitService internshipUnitService;
	
	@GetMapping("/showQuestList")
	public String showAllQuestionnaires(Principal principal, Model theModel) {
		
		User user = userService.findByEmail(principal.getName());
		List<Questionnaire> questionnaires = user.getQuestionnaires();
		
		Comparator<Questionnaire> comparator = (quest1, quest2) -> quest1.getCreateTime().compareTo(quest2.getCreateTime());
		questionnaires.sort(comparator);
		
		theModel.addAttribute("questionnaires", questionnaires);
		
		return "questionnaires_list";
	}
	
	@GetMapping("/newQuestionnaire")
	public String createNewQuestionnaire(Principal principal) {
		
		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = new Questionnaire();
		LocalDateTime currentTime = LocalDateTime.now();
		questionnaire.setCreateTime(currentTime);
		questionnaire.setUser(user);
		questionnaireService.saveQuestionnaire(questionnaire);
		
		long questId = questionnaire.getId();
				
		return "redirect:/survey/showQuestionnaire/" + questId;
	}
	
	@GetMapping("/showQuestionnaire/{id}")
	public String showQuestionnaire(Model theModel, @PathVariable ("id") long id) {
	
		Questionnaire questionnaire = questionnaireService.findById(id);

		// set list of units. In case user chose medical chamber before filter the units accordingly
		List<InternshipUnit> units;
		
		MedicalChamberEnum medicalChamber = questionnaire.getMedicalChamber();
		
		if (medicalChamber == null) {
			units = internshipUnitService.findAll();
		} else {
			units = internshipUnitService.findByMedicalChamber(medicalChamber.toString());
		}
		
		if (!theModel.containsAttribute("questionnaire")) {
			theModel.addAttribute("questionnaire", questionnaire);
		}
		theModel.addAttribute("units", units);
		
		return "questionnaire_view";
	}
	
	@GetMapping("/deleteQuestionnaire/{id}")
	public String deleteQuestionnaire(@PathVariable ("id") long id) {

		questionnaireService.delete(id);
		
		return "redirect:/survey/showQuestList";
	}
	
	@PostMapping(value = "/saveQuestionnaire", params = "action=save")
	public String saveQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire
			, Principal principal) {
		
		setMedicalChamberDependingOnSelectedUnit(questionnaire);

		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestList";
	}

	@PostMapping(value = "/saveQuestionnaire", params = "action=send")
	public String sendQuestionnaire(@Valid @ModelAttribute ("questionnaire") Questionnaire questionnaire
			, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.questionnaire", bindingResult);
			redirectAttributes.addFlashAttribute("questionnaire", questionnaire);
			return "redirect:/survey/showQuestionnaire/" + questionnaire.getId();
		}
		
		setMedicalChamberDependingOnSelectedUnit(questionnaire);
		questionnaire.setStatus(Status.SENT);
		questionnaireService.saveQuestionnaire(questionnaire);
		

		try (PDDocument document = new PDDocument()) {
			
			PDPage page = new PDPage();
			document.addPage(page);
			
			PDFont pdFont = PDType1Font.TIMES_ROMAN;
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)){

				contentStream.beginText();
				contentStream.setFont(pdFont, 14);
				contentStream.newLineAtOffset(50, 100);
				contentStream.showText(questionnaire.getUnit().getName());
				contentStream.endText();
				contentStream.close();
				document.save("newsurvey.pdf");
				document.close();
				
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		



		
		return "redirect:/survey/showQuestionnaire/" + questionnaire.getId();
	}
	
	// Re-send new units list if user changed medical chamber
	@GetMapping("/unitSearch/{id}")
	public String searchUnitByMedicalChamber(Model theModel, @RequestParam (value = "chamberSelected") String chamberSelected
			, @PathVariable ("id") long id) {
		
		List<InternshipUnit> units = internshipUnitService.findByMedicalChamber(chamberSelected);

		Questionnaire questionnaire = questionnaireService.findById(id);
		
		theModel.addAttribute("questionnaire", questionnaire);
		theModel.addAttribute("units", units);
		
		return "questionnaire_view :: units_list";
	}
	
	public void setMedicalChamberDependingOnSelectedUnit(Questionnaire questionnaire) {
		if (questionnaire.getMedicalChamber() == null && questionnaire.getUnit() != null) {
			
			InternshipUnit unit = questionnaire.getUnit();
			MedicalChamberEnum chamber = MedicalChamberEnum.valueOf(unit.getMedicalChamber());
			questionnaire.setMedicalChamber(chamber);
		}
	}
	
	
	
}
