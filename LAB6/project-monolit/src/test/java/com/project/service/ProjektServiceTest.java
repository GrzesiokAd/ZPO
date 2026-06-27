package com.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repository.ProjektRepository;
import com.project.repository.ZadanieRepository;

@ExtendWith(MockitoExtension.class)
public class ProjektServiceTest {

    @Mock
    private ProjektRepository projektRepository;

    @Mock
    private ZadanieRepository zadanieRepository;

    @InjectMocks
    private ProjektServiceImpl projektService;

    @Test
    void getProjekt_whenValidId_returnsProjekt() {
        Integer projektId = 1;
        Projekt projekt = createProjektTestowy(projektId, "Projekt testowy");

        given(projektRepository.findById(projektId)).willReturn(Optional.of(projekt));

        Optional<Projekt> result = projektService.getProjekt(projektId);

        assertThat(result).isPresent();
        assertThat(result.get().getProjektId()).isEqualTo(projektId);
        assertThat(result.get().getNazwa()).isEqualTo("Projekt testowy");

        verify(projektRepository).findById(projektId);
    }

    @Test
    void setProjekt_whenValidData_savesProjekt() {
        Projekt projekt = createProjektTestowy(null, "Nowy projekt");
        Projekt savedProjekt = createProjektTestowy(1, "Nowy projekt");

        given(projektRepository.save(projekt)).willReturn(savedProjekt);

        Projekt result = projektService.setProjekt(projekt);

        assertThat(result).isNotNull();
        assertThat(result.getProjektId()).isEqualTo(1);
        assertThat(result.getNazwa()).isEqualTo("Nowy projekt");

        verify(projektRepository).save(projekt);
    }

    @Test
    void getProjekty_returnsPagedProjects() {
        PageRequest pageable = PageRequest.of(0, 10);

        List<Projekt> projekty = List.of(
                createProjektTestowy(1, "Projekt 1"),
                createProjektTestowy(2, "Projekt 2")
        );

        Page<Projekt> page = new PageImpl<>(projekty, pageable, projekty.size());

        given(projektRepository.findAll(pageable)).willReturn(page);

        Page<Projekt> result = projektService.getProjekty(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        verify(projektRepository).findAll(pageable);
    }

    @Test
    void searchByNazwa_returnsMatchingProjects() {
        PageRequest pageable = PageRequest.of(0, 10);
        String nazwa = "test";

        List<Projekt> projekty = List.of(
                createProjektTestowy(1, "Projekt testowy")
        );

        Page<Projekt> page = new PageImpl<>(projekty, pageable, projekty.size());

        given(projektRepository.findByNazwaContainingIgnoreCase(nazwa, pageable)).willReturn(page);

        Page<Projekt> result = projektService.searchByNazwa(nazwa, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNazwa()).containsIgnoringCase("test");

        verify(projektRepository).findByNazwaContainingIgnoreCase(nazwa, pageable);
    }

    @Test
    void deleteProjekt_whenProjectHasTasks_deletesTasksAndProject() {
        Integer projektId = 1;

        Zadanie zadanie1 = createZadanieTestowe(1, "Zadanie 1");
        Zadanie zadanie2 = createZadanieTestowe(2, "Zadanie 2");

        given(zadanieRepository.findZadaniaProjektu(projektId)).willReturn(List.of(zadanie1, zadanie2));

        projektService.deleteProjekt(projektId);

        verify(zadanieRepository).findZadaniaProjektu(projektId);
        verify(zadanieRepository).delete(zadanie1);
        verify(zadanieRepository).delete(zadanie2);
        verify(projektRepository).deleteById(projektId);
    }

    private Projekt createProjektTestowy(Integer id, String nazwa) {
        return Projekt.builder()
                .projektId(id)
                .nazwa(nazwa)
                .opis("Opis testowy")
                .dataOddania(LocalDate.of(2026, 6, 1))
                .build();
    }

    private Zadanie createZadanieTestowe(Integer id, String nazwa) {
        return Zadanie.builder()
                .zadanieId(id)
                .nazwa(nazwa)
                .opis("Opis zadania")
                .kolejnosc(id)
                .build();
    }
}