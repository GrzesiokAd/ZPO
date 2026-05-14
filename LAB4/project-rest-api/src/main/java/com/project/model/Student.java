package com.project.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "student",
        indexes = {
                @Index(name = "idx_nazwisko", columnList = "nazwisko", unique = false),
                @Index(name = "idx_nr_indeksu", columnList = "nr_indeksu", unique = true)
        }
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;

    @NotBlank(message = "Pole imię nie może być puste!")
    @Size(max = 50, message = "Imię może mieć maksymalnie {max} znaków!")
    @Column(nullable = false, length = 50)
    private String imie;

    @NotBlank(message = "Pole nazwisko nie może być puste!")
    @Size(max = 100, message = "Nazwisko może mieć maksymalnie {max} znaków!")
    @Column(nullable = false, length = 100)
    private String nazwisko;

    @NotBlank(message = "Pole nr indeksu nie może być puste!")
    @Size(max = 20, message = "Nr indeksu może mieć maksymalnie {max} znaków!")
    @Column(name = "nr_indeksu", nullable = false, unique = true, length = 20)
    private String nrIndeksu;

    @Email(message = "Niepoprawny adres email!")
    @Size(max = 50, message = "Email może mieć maksymalnie {max} znaków!")
    @Column(unique = true, length = 50)
    private String email;

    @Column
    private Boolean stacjonarny;

    @Builder.Default
    @ManyToMany(mappedBy = "studenci")
    @JsonIgnoreProperties({ "studenci", "zadania" })
    private Set<Projekt> projekty = new HashSet<>();
}