package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.PatientProxy;
import com.mediscreen.library.dto.PatientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {


    @InjectMocks
    private PatientController patientController;
    @Mock
    private PatientProxy patientProxy;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;


    @Test
    @DisplayName("Should return the patient list view")
    public void homeTest() {

        // Arrange
        List<PatientDto> expectedPatients = new ArrayList<>();
        when(patientProxy.getPatientsList()).thenReturn(expectedPatients);
        Model model = new ExtendedModelMap();

        // Act
        String actualViewName = patientController.home(model);

        // Assert
        assertEquals("patient/list", actualViewName);
        assertEquals(expectedPatients, model.getAttribute("patients"));
    }


    @Test
    @DisplayName("Should return the add patient form view")
    public void addPatientFormTest() {

        // Arrange
        Model model = new ExtendedModelMap();

        // Act
        String actualViewName = patientController.addPatientForm(model);

        // Assert
        assertEquals("patient/add", actualViewName);
        assertNotNull(model.getAttribute("patientDto"));
    }


    @Test
    @DisplayName("Should redirect to the patient list view on successful patient creation")
    public void addPatientTest() {

        // Arrange
        PatientDto patientDto = new PatientDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String actualViewName = patientController.addPatient(patientDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/patient/list", actualViewName);
        verify(patientProxy, times(1)).validatePatient(patientDto);
    }


    @Test
    @DisplayName("Should return the add patient form view on failed patient creation")
    public void addPatientNegativeTest() {

        // Arrange
        PatientDto patientDto = new PatientDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String actualViewName = patientController.addPatient(patientDto, bindingResult, model);

        // Assert
        assertEquals("patient/add", actualViewName);
    }


    @Test
    public void updatePatientFormTest() {

        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        LocalDate birthdate = LocalDate.of(1990, 1, 1);
        patientDto.setBirthdate(birthdate);
        when(patientProxy.findPatientById(patientId)).thenReturn(patientDto);
        Model model = new ConcurrentModel();

        // Act
        String viewName = patientController.updatePatientForm(patientId, model);

        // Assert
        assertEquals("patient/update", viewName);
        assertEquals(patientDto, model.getAttribute("patientDto"));
        assertEquals("01-01-1990", model.getAttribute("birthdate"));
    }


    @Test
    public void updatePatientTest() {

        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = patientController.updatePatient(patientId, patientDto, result);

        // Assert
        assertEquals("redirect:/patient/list", viewName);
        verify(patientProxy, times(1)).updatePatient(eq(patientId), eq(patientDto));
    }


    @Test
    public void updatePatientTest_withErrors() {

        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = patientController.updatePatient(patientId, patientDto, result);

        // Assert
        assertEquals("patient/update", viewName);
        verify(patientProxy, never()).updatePatient(anyInt(), any(PatientDto.class));
    }


    @Test
    public void deletePatientTest() {

        // Arrange
        int patientId = 1;

        // Act
        String viewName = patientController.deletePatient(patientId);

        // Assert
        assertEquals("redirect:/patient/list", viewName);
        verify(patientProxy, times(1)).deletePatient(patientId);
    }
}
