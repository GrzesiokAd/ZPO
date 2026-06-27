package com.project.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zadanie {

	private Integer zadanieId;

	@NotBlank(message = "Pole nazwa nie może być puste!")
	@Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
	private String nazwa;

	@Size(max = 1000, message = "Opis może mieć maksymalnie {max} znaków!")
	private String opis;

	private Integer kolejnosc;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime dataczasDodania;

	@JsonIgnoreProperties({ "zadania", "studenci" })
	private Projekt projekt;

	public Zadanie() {
	}

	public Zadanie(Integer zadanieId, String nazwa, String opis, Integer kolejnosc,
			LocalDateTime dataczasDodania, Projekt projekt) {
		this.zadanieId = zadanieId;
		this.nazwa = nazwa;
		this.opis = opis;
		this.kolejnosc = kolejnosc;
		this.dataczasDodania = dataczasDodania;
		this.projekt = projekt;
	}

	public Integer getZadanieId() {
		return zadanieId;
	}

	public void setZadanieId(Integer zadanieId) {
		this.zadanieId = zadanieId;
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

	public Integer getKolejnosc() {
		return kolejnosc;
	}

	public void setKolejnosc(Integer kolejnosc) {
		this.kolejnosc = kolejnosc;
	}

	public LocalDateTime getDataczasDodania() {
		return dataczasDodania;
	}

	public void setDataczasDodania(LocalDateTime dataczasDodania) {
		this.dataczasDodania = dataczasDodania;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void setProjekt(Projekt projekt) {
		this.projekt = projekt;
	}
}
