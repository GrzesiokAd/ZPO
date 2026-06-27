package com.project.controller;

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
import com.project.model.Zadanie;
import com.project.service.ProjektService;
import com.project.service.ZadanieService;

import jakarta.validation.Valid;

@Controller
public class ZadanieController {

	private final ZadanieService zadanieService;
	private final ProjektService projektService;

	public ZadanieController(ZadanieService zadanieService, ProjektService projektService) {
		this.zadanieService = zadanieService;
		this.projektService = projektService;
	}

	@GetMapping("/zadanieList")
	public String zadanieList(@RequestParam(name = "projektId", required = false) Integer projektId,
			@PageableDefault(size = 10, sort = "kolejnosc", direction = Sort.Direction.ASC) Pageable pageable,
			Model model) {
		Page<Zadanie> zadania = projektId == null ? zadanieService.getZadania(pageable)
				: zadanieService.getZadaniaProjektu(projektId, pageable);
		model.addAttribute("zadania", zadania);
		model.addAttribute("projekty", projektService.getProjekty(PageRequest.of(0, 1000, Sort.by("nazwa"))).getContent());
		model.addAttribute("projektId", projektId);
		return "zadanieList";
	}

	@GetMapping("/zadanieEdit")
	public String zadanieEdit(@RequestParam(name = "zadanieId", required = false) Integer zadanieId,
			@RequestParam(name = "projektId", required = false) Integer projektId, Model model) {
		Zadanie zadanie = zadanieId == null ? new Zadanie() : zadanieService.getZadanie(zadanieId).orElse(new Zadanie());
		if (zadanie.getProjekt() == null && projektId != null) {
			projektService.getProjekt(projektId).ifPresent(zadanie::setProjekt);
		}
		addTaskFormAttributes(model, zadanie);
		return "zadanieEdit";
	}

	@PostMapping(path = "/zadanieEdit")
	public String zadanieEditSave(@ModelAttribute @Valid Zadanie zadanie, BindingResult bindingResult,
			@RequestParam(name = "projektId", required = false) Integer projektId, Model model) {
		if (bindingResult.hasErrors()) {
			setProject(zadanie, projektId);
			addTaskFormAttributes(model, zadanie);
			return "zadanieEdit";
		}
		try {
			setProject(zadanie, projektId);
			if (zadanie.getZadanieId() != null) {
				zadanieService.getZadanie(zadanie.getZadanieId())
						.ifPresent(existing -> zadanie.setDataczasDodania(existing.getDataczasDodania()));
			}
			zadanieService.setZadanie(zadanie);
		} catch (RuntimeException e) {
			bindingResult.reject("rest.error", e.getMessage());
			addTaskFormAttributes(model, zadanie);
			return "zadanieEdit";
		}
		return "redirect:/zadanieList";
	}

	@PostMapping(params = "cancel", path = "/zadanieEdit")
	public String zadanieEditCancel() {
		return "redirect:/zadanieList";
	}

	@PostMapping(params = "delete", path = "/zadanieEdit")
	public String zadanieEditDelete(@ModelAttribute Zadanie zadanie) {
		zadanieService.deleteZadanie(zadanie.getZadanieId());
		return "redirect:/zadanieList";
	}

	private void setProject(Zadanie zadanie, Integer projektId) {
		if (projektId == null) {
			zadanie.setProjekt(null);
		} else {
			Projekt projekt = projektService.getProjekt(projektId).orElse(null);
			zadanie.setProjekt(projekt);
		}
	}

	private void addTaskFormAttributes(Model model, Zadanie zadanie) {
		model.addAttribute("zadanie", zadanie);
		model.addAttribute("projekty", projektService.getProjekty(PageRequest.of(0, 1000, Sort.by("nazwa"))).getContent());
	}
}
