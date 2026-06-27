package com.project.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.service.ProjektService;
import com.project.service.StudentService;
import com.project.service.ZadanieService;

import jakarta.validation.Valid;

@Controller
public class ProjectController {

	private final ProjektService projektService;
	private final StudentService studentService;
	private final ZadanieService zadanieService;

	public ProjectController(ProjektService projektService, StudentService studentService, ZadanieService zadanieService) {
		this.projektService = projektService;
		this.studentService = studentService;
		this.zadanieService = zadanieService;
	}

	@GetMapping("/projektList")
	public String projektList(@RequestParam(name = "nazwa", required = false) String nazwa,
			@PageableDefault(size = 10, sort = "dataczasModyfikacji", direction = Sort.Direction.DESC) Pageable pageable,
			Model model) {
		Page<Projekt> projekty = hasText(nazwa) ? projektService.searchByNazwa(nazwa, pageable)
				: projektService.getProjekty(pageable);
		model.addAttribute("projekty", projekty);
		model.addAttribute("nazwa", nazwa);
		return "projektList";
	}

	@GetMapping("/projektEdit")
	public String projektEdit(@RequestParam(name = "projektId", required = false) Integer projektId, Model model) {
		Projekt projekt = projektId == null ? new Projekt() : projektService.getProjekt(projektId).orElse(new Projekt());
		addProjectFormAttributes(model, projekt);
		return "projektEdit";
	}

	@PostMapping(path = "/projektEdit")
	public String projektEditSave(@ModelAttribute @Valid Projekt projekt, BindingResult bindingResult,
			@RequestParam(name = "studentIds", required = false) List<Integer> studentIds, Model model) {
		if (bindingResult.hasErrors()) {
			addProjectFormAttributes(model, projekt, studentIds);
			return "projektEdit";
		}
		try {
			prepareProjectRelations(projekt, studentIds);
			projektService.setProjekt(projekt);
		} catch (RuntimeException e) {
			bindingResult.reject("rest.error", e.getMessage());
			addProjectFormAttributes(model, projekt, studentIds);
			return "projektEdit";
		}
		return "redirect:/projektList";
	}

	@PostMapping(params = "cancel", path = "/projektEdit")
	public String projektEditCancel() {
		return "redirect:/projektList";
	}

	@PostMapping(params = "delete", path = "/projektEdit")
	public String projektEditDelete(@ModelAttribute Projekt projekt) {
		projektService.deleteProjekt(projekt.getProjektId());
		return "redirect:/projektList";
	}

	private void prepareProjectRelations(Projekt projekt, List<Integer> studentIds) {
		if (projekt.getProjektId() != null) {
			projektService.getProjekt(projekt.getProjektId()).ifPresent(existing -> {
				projekt.setDataczasUtworzenia(existing.getDataczasUtworzenia());
				projekt.setDataczasModyfikacji(existing.getDataczasModyfikacji());
				projekt.setZadania(existing.getZadania());
			});
		}
		Set<Student> selectedStudents = new HashSet<>();
		if (studentIds != null) {
			for (Integer studentId : studentIds) {
				studentService.getStudent(studentId).ifPresent(selectedStudents::add);
			}
		}
		projekt.setStudenci(selectedStudents);
	}

	private void addProjectFormAttributes(Model model, Projekt projekt) {
		Set<Integer> selectedIds = projekt.getStudenci() == null ? Set.of()
				: projekt.getStudenci().stream().map(Student::getStudentId).collect(Collectors.toSet());
		addProjectFormAttributes(model, projekt, selectedIds.stream().toList());
	}

	private void addProjectFormAttributes(Model model, Projekt projekt, List<Integer> selectedStudentIds) {
		model.addAttribute("projekt", projekt);
		model.addAttribute("studenci", studentService.getStudenci(PageRequest.of(0, 1000, Sort.by("nazwisko", "imie"))).getContent());
		model.addAttribute("selectedStudentIds", selectedStudentIds == null ? List.of() : selectedStudentIds);
		if (projekt.getProjektId() != null) {
			model.addAttribute("zadaniaProjektu",
					zadanieService.getZadaniaProjektu(projekt.getProjektId(), PageRequest.of(0, 1000, Sort.by("kolejnosc"))).getContent());
		} else {
			model.addAttribute("zadaniaProjektu", List.of());
		}
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
