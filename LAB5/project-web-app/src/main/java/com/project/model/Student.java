package com.project.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

	private Integer studentId;

	@NotBlank(message = "Pole imię nie może być puste!")
	@Size(max = 50, message = "Imię może mieć maksymalnie {max} znaków!")
	private String imie;

	@NotBlank(message = "Pole nazwisko nie może być puste!")
	@Size(max = 100, message = "Nazwisko może mieć maksymalnie {max} znaków!")
	private String nazwisko;

	@NotBlank(message = "Pole nr indeksu nie może być puste!")
	@Size(max = 20, message = "Nr indeksu może mieć maksymalnie {max} znaków!")
	private String nrIndeksu;

	@Email(message = "Niepoprawny adres e-mail!")
	@Size(max = 50, message = "E-mail może mieć maksymalnie {max} znaków!")
	private String email;

	private Boolean stacjonarny;

	@JsonIgnoreProperties({ "studenci", "zadania" })
	private Set<Projekt> projekty = new HashSet<>();

	public Student() {
	}

	public Student(Integer studentId, String imie, String nazwisko, String nrIndeksu, String email,
			Boolean stacjonarny, Set<Projekt> projekty) {
		this.studentId = studentId;
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.nrIndeksu = nrIndeksu;
		this.email = email;
		this.stacjonarny = stacjonarny;
		this.projekty = projekty == null ? new HashSet<>() : projekty;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}

	public String getNrIndeksu() {
		return nrIndeksu;
	}

	public void setNrIndeksu(String nrIndeksu) {
		this.nrIndeksu = nrIndeksu;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getStacjonarny() {
		return stacjonarny;
	}

	public void setStacjonarny(Boolean stacjonarny) {
		this.stacjonarny = stacjonarny;
	}

	public Set<Projekt> getProjekty() {
		return projekty;
	}

	public void setProjekty(Set<Projekt> projekty) {
		this.projekty = projekty == null ? new HashSet<>() : projekty;
	}
}
