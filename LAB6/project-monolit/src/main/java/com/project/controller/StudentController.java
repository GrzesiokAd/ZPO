package com.project.controller;

import org.springframework.data.domain.Page;
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

import com.project.model.Student;
import com.project.service.StudentService;

import jakarta.validation.Valid;

@Controller
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/studentList")
	public String studentList(@RequestParam(name = "nazwisko", required = false) String nazwisko,
			@RequestParam(name = "nrIndeksu", required = false) String nrIndeksu,
			@PageableDefault(size = 10, sort = "nazwisko", direction = Sort.Direction.ASC) Pageable pageable,
			Model model) {
		Page<Student> studenci;
		if (hasText(nrIndeksu)) {
			studenci = studentService.searchByNrIndeksu(nrIndeksu, pageable);
		} else if (hasText(nazwisko)) {
			studenci = studentService.searchByNazwisko(nazwisko, pageable);
		} else {
			studenci = studentService.getStudenci(pageable);
		}
		model.addAttribute("studenci", studenci);
		model.addAttribute("nazwisko", nazwisko);
		model.addAttribute("nrIndeksu", nrIndeksu);
		return "studentList";
	}

	@GetMapping("/studentEdit")
	public String studentEdit(@RequestParam(name = "studentId", required = false) Integer studentId, Model model) {
		Student student = studentId == null ? new Student() : studentService.getStudent(studentId).orElse(new Student());
		model.addAttribute("student", student);
		return "studentEdit";
	}

	@PostMapping(path = "/studentEdit")
	public String studentEditSave(@ModelAttribute @Valid Student student, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "studentEdit";
		}
		try {
			if (student.getStacjonarny() == null) {
				student.setStacjonarny(false);
			}
			studentService.setStudent(student);
		} catch (RuntimeException e) {
			bindingResult.reject("rest.error", e.getMessage());
			return "studentEdit";
		}
		return "redirect:/studentList";
	}

	@PostMapping(params = "cancel", path = "/studentEdit")
	public String studentEditCancel() {
		return "redirect:/studentList";
	}

	@PostMapping(params = "delete", path = "/studentEdit")
	public String studentEditDelete(@ModelAttribute Student student) {
		studentService.deleteStudent(student.getStudentId());
		return "redirect:/studentList";
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
