package com.khal.intern_survey.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.Valid;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.dto.InternshipSectionsEnum;
import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.Course;
import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.CourseService;
import com.khal.intern_survey.service.InternshipSectionService;
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
	
	@Autowired
	MessageSource messages;
	
	@Autowired
	File fontFile;
	
	@Autowired
	InternshipSectionService sectionService;
	
	@Autowired
	CourseService courseService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showQuestListForUser")
	public String showAllQuestionnaires(Principal principal, Model theModel) {
		
		
		User user = userService.findByEmail(principal.getName());
		List<Questionnaire> questionnaires = user.getQuestionnaires();
		
		Comparator<Questionnaire> comparator = (quest1, quest2) -> quest1.getId().compareTo(quest2.getId());
		questionnaires.sort(comparator);
		
		theModel.addAttribute("questionnaires", questionnaires);
		
		return "user_questionnaires";
	}
	
	@GetMapping("/newQuestionnaire")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String createNewQuestionnaire(Principal principal) {
		
		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = new Questionnaire();
		
		LocalDateTime currentTime = LocalDateTime.now();
		questionnaire.setCreateTime(currentTime);
		questionnaire.setUser(user);
		questionnaireService.saveQuestionnaire(questionnaire);
		
		long questId = questionnaire.getId();
		
		sectionService.createQuestionnaireSections(questionnaire);
		courseService.createAllCourses(questionnaire);
				
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
			units = internshipUnitService.findByMedicalChamber(medicalChamber);
		}
		
		if (!theModel.containsAttribute("questionnaire")) {
			theModel.addAttribute("questionnaire", questionnaire);
		}
		theModel.addAttribute("units", units);
		
		return "questionnaire_view";
	}
	
	@GetMapping("/deleteQuestionnaire/{id}")
	public String deleteQuestionnaire(Principal principal, @PathVariable ("id") long id) {
		
		
		questionnaireService.delete(id);

	
		return "redirect:/survey/showQuestListForUser";
	}
	
	@PostMapping(value = "/saveQuestionnaire", params = "action=save")
	public String saveQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire
			, Principal principal) {
				
		setMedicalChamberDependingOnSelectedUnit(questionnaire);
		
		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestListForUser";
	}

	// New transaction is required for acl to work
	@PostMapping(value = "/saveQuestionnaire", params = "action=send")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String sendQuestionnaire(Principal principal, @Valid @ModelAttribute ("questionnaire") Questionnaire questionnaire
			, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.questionnaire", bindingResult);
			redirectAttributes.addFlashAttribute("questionnaire", questionnaire);
			return "redirect:/survey/showQuestionnaire/" + questionnaire.getId();
		}
		
		setMedicalChamberDependingOnSelectedUnit(questionnaire);
		questionnaire.setStatus(Status.SENT);
		
		UUID uuid = UUID.randomUUID();
		questionnaire.setVerificationId(uuid.toString());
		
		LocalDate currentDate = LocalDate.now();
		questionnaire.setSendDate(currentDate);
		
		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/sendQuestPDF/" + questionnaire.getId();
	}
	
	@GetMapping(value = "/sendQuestPDF/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] returnQuestPDF(@PathVariable ("id") long id) throws IOException{
		
		Locale locale = LocaleContextHolder.getLocale();
		Questionnaire questionnaire = questionnaireService.findById(id);
		
		String pdfTitle = messages.getMessage("pdf.title", null, locale);
			
		PDDocument document = new PDDocument();
		PDType0Font font = PDType0Font.load(document, fontFile);
		PDDocumentInformation info = document.getDocumentInformation();
		info.setTitle(pdfTitle);
		
		PDPage page = new PDPage();
		document.addPage(page);
		
		PDPageContentStream cs = new PDPageContentStream(document, page);
		cs.beginText();
		cs.setLeading(27);
		cs.setFont(font, 14);
		cs.newLineAtOffset(25, 750);
		cs.showText(messages.getMessage("pdf.verId", null, locale) + questionnaire.getVerificationId().toString());
		cs.newLine();
		cs.showText(messages.getMessage("pdf.title", null, locale));	
		cs.newLine();
		cs.showText(messages.getMessage("pdf.place", null, locale) + questionnaire.getUnit().getName());
		cs.setFont(font, 12);
		cs.newLine();
		cs.showText(messages.getMessage("pdf.coordinator", null, locale) + questionnaire.getCoordinatorName());
		cs.newLine();
		cs.showText(messages.getMessage("pdf.rating", null, locale) + questionnaire.getCoordinator());
		cs.newLine();
		cs.newLine();
		cs.setFont(font, 14);
		cs.showText(messages.getMessage("pdf.sections", null, locale));
		cs.newLine();
		cs.newLine();
				
		AtomicInteger linesCounter = new AtomicInteger(12);
		
		for(InternshipSection section : questionnaire.getSections()) {
			
			// anesthesiology and family medicine sections don't have clinic and ward rating so they take less lines
			boolean sectionIsNotAnesthesiologyOrFamilyMed = section.getName() != InternshipSectionsEnum.ANESTHESIOLOGY_INTENSIVE_CARE
					&& section.getName() != InternshipSectionsEnum.FAMILY_MEDICINE;
			
			int linesRequired;
					
			if(section.isDisabled()) {
				linesRequired = 3;
				if(newPageIsRequired(linesCounter, linesRequired)) {
					cs = getNewPageContentStream(document, cs);
					linesCounter.set(0);	
				}
				
				linesCounter.addAndGet(linesRequired);
				
				cs.setLeading(18);
				cs.setFont(font, 14);
				String sectionTitle = messages.getMessage("pdf."
						+ section.getName(), null, locale);
				
				cs.showText(sectionTitle);
				cs.newLine();
				cs.showText(messages.getMessage("pdf.disabled", null, locale));
			} else {
				linesRequired = sectionIsNotAnesthesiologyOrFamilyMed ? 20 : 15;
				if(newPageIsRequired(linesCounter, linesRequired)) {
					cs = getNewPageContentStream(document, cs);
					linesCounter.set(0);	
				}
				
				linesCounter.addAndGet(linesRequired);
				
				cs.setLeading(18);
				cs.setFont(font, 14);
				String sectionTitle = messages.getMessage("pdf."
						+ section.getName(), null, locale);
				
				cs.showText(sectionTitle);
				cs.newLine();
				cs.setFont(font, 12);
				cs.showText(messages.getMessage("pdf.tutor", null, locale));
				if (section.getTutorName() == null) {
					cs.showText(messages.getMessage("pdf.unknown", null, locale));
				} else {
					cs.showText(section.getTutorName());
				}
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getTutor());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.place", null, locale));
				if (section.getUnitName() == null) {
					cs.showText(messages.getMessage("pdf.unknown", null, locale));
				} else {
					cs.showText(section.getUnitName());
				}
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getUnit());
				cs.newLine();
				if (sectionIsNotAnesthesiologyOrFamilyMed) {
					
					cs.showText(messages.getMessage("pdf.ward", null, locale));
					cs.newLine();
					cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getWard());
					cs.newLine();
					cs.showText(messages.getMessage("pdf.clinic", null, locale));
					cs.newLine();
					cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getClinic());
					cs.newLine();
				}
				cs.showText(messages.getMessage("pdf.number", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getNumberOfProcedures());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.autonomy", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getProceduresAutonomy());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.theoretical", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getTheoreticalKnowledge());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.practical", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getPracticalKnowledge());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.duty", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + section.getMedicalDuty());
			}
			cs.newLine();
			cs.newLine();
		}
		
		for(Course course : questionnaire.getCourses()) {
			int linesRequired;
			
			if(course.isDisabled()) {
				linesRequired = 3;
				if(newPageIsRequired(linesCounter, linesRequired)) {
					cs = getNewPageContentStream(document, cs);
					linesCounter.set(0);	
				}
				
				linesCounter.addAndGet(linesRequired);
				
				cs.setLeading(18);
				cs.setFont(font, 14);
				String courseTitle = messages.getMessage("pdf."
						+ course.getName(), null, locale);
				
				cs.showText(courseTitle);
				cs.newLine();
				cs.showText(messages.getMessage("pdf.disabled", null, locale));
			} else {
				linesRequired = 10;
				if(newPageIsRequired(linesCounter, linesRequired)) {
					cs = getNewPageContentStream(document, cs);
					linesCounter.set(0);	
				}
				
				linesCounter.addAndGet(10);
				
				cs.setLeading(18);
				cs.setFont(font, 14);
				String courseTitle = messages.getMessage("pdf."
						+ course.getName(), null, locale);
				
				cs.showText(courseTitle);
				cs.newLine();
				cs.setFont(font, 12);
				cs.showText(messages.getMessage("pdf.tutor", null, locale));
				if (course.getTutorName() == null) {
					cs.showText(messages.getMessage("pdf.unknown", null, locale));
				} else {
					cs.showText(course.getTutorName());
				}
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + course.getTutor());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.place", null, locale));
				if (course.getUnitName() == null) {
					cs.showText(messages.getMessage("pdf.unknown", null, locale));
				} else {
					cs.showText(course.getUnitName());
				}
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + course.getUnit());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.theoretical", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + course.getTheoreticalKnowledge());
				cs.newLine();
				cs.showText(messages.getMessage("pdf.practical", null, locale));
				cs.newLine();
				cs.showText(messages.getMessage("pdf.rating", null, locale) + course.getPracticalKnowledge());

			}
			cs.newLine();
			cs.newLine();
		}
	
		cs.endText();
		cs.close();		
		
		AccessPermission ap = new AccessPermission();
		ap.setReadOnly();
		StandardProtectionPolicy policy 
			= new StandardProtectionPolicy("pass01", "", ap);
		
		int keyLength = 256;
		policy.setEncryptionKeyLength(keyLength);
		policy.setPermissions(ap);
		document.protect(policy);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream);
		document.close();
			
		return outputStream.toByteArray();
	}
	
	private boolean newPageIsRequired(AtomicInteger counter, int linesRequired) throws IOException {
		if(counter.get() + linesRequired > 40) {
				return true;	
		} else return false;
	}
	
	private PDPageContentStream getNewPageContentStream(PDDocument document, PDPageContentStream cs) throws IOException {
		cs.endText();
		cs.close();
		PDPage newPage = new PDPage();
		document.addPage(newPage);
		cs = new PDPageContentStream(document, newPage);
		cs.beginText();
		cs.setLeading(18);
		cs.newLineAtOffset(25, 750);
		return cs;
	}
	
	@GetMapping("/unitSearch/{id}")
	public String searchUnitByMedicalChamber(Model theModel, @RequestParam (value = "chamberSelected") MedicalChamberEnum chamberSelected
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
			MedicalChamberEnum chamber = unit.getMedicalChamber();
			questionnaire.setMedicalChamber(chamber);
		}
	}
	
}
