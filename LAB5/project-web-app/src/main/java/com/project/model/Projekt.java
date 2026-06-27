package com.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Projekt {

	private Integer projektId;

	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	private String nazwa;

	@Size(max = 1000, message = "Opis może mieć maksymalnie {max} znaków!")
	private String opis;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime dataczasUtworzenia;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime dataczasModyfikacji;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataOddania;

	@JsonIgnoreProperties({ "projekt" })
	private List<Zadanie> zadania = new ArrayList<>();

	@JsonIgnoreProperties({ "projekty" })
	private Set<Student> studenci = new HashSet<>();

	public Projekt() {
	}

	public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataczasUtworzenia,
			LocalDateTime dataczasModyfikacji, LocalDate dataOddania, List<Zadanie> zadania, Set<Student> studenci) {
		this.projektId = projektId;
		this.nazwa = nazwa;
		this.opis = opis;
		this.dataczasUtworzenia = dataczasUtworzenia;
		this.dataczasModyfikacji = dataczasModyfikacji;
		this.dataOddania = dataOddania;
		this.zadania = zadania == null ? new ArrayList<>() : zadania;
		this.studenci = studenci == null ? new HashSet<>() : studenci;
	}

	public Integer getProjektId() {
		return projektId;
	}

	public void setProjektId(Integer projektId) {
		this.projektId = projektId;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public LocalDateTime getDataczasUtworzenia() {
		return dataczasUtworzenia;
	}

	public void setDataczasUtworzenia(LocalDateTime dataczasUtworzenia) {
		this.dataczasUtworzenia = dataczasUtworzenia;
	}

	public LocalDateTime getDataczasModyfikacji() {
		return dataczasModyfikacji;
	}

	public void setDataczasModyfikacji(LocalDateTime dataczasModyfikacji) {
		this.dataczasModyfikacji = dataczasModyfikacji;
	}

	public LocalDate getDataOddania() {
		return dataOddania;
	}

	public void setDataOddania(LocalDate dataOddania) {
		this.dataOddania = dataOddania;
	}

	public List<Zadanie> getZadania() {
		return zadania;
	}

	public void setZadania(List<Zadanie> zadania) {
		this.zadania = zadania == null ? new ArrayList<>() : zadania;
	}

	public Set<Student> getStudenci() {
		return studenci;
	}

	public void setStudenci(Set<Student> studenci) {
		this.studenci = studenci == null ? new HashSet<>() : studenci;
	}
}
