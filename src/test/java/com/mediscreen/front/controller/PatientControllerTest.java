package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.PatientProxy;
import com.mediscreen.library.dto.PatientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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
    @DisplayName("Should return the right view name patient/update")
    public void updatePatientFormTest() {
        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        when(patientProxy.findPatientById(patientId)).thenReturn(patientDto);

        // Act
        String viewName = patientController.updatePatientForm(patientId, model);

        // Assert
        assertEquals("patient/update", viewName);
        verify(patientProxy).findPatientById(patientId);
        verify(model).addAttribute("patientDto", patientDto);
    }


    @Test
    @DisplayName("Should return the right view name patient/list")
    public void updatePatientTest() {

        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = patientController.updatePatient(patientId, patientDto, bindingResult);

        // Assert
        assertEquals("redirect:/patient/list", viewName);
        verify(patientProxy, times(1)).updatePatient(eq(patientId), eq(patientDto));
    }


    @Test
    @DisplayName("Should return the right view name patient/update")
    public void updatePatientNegativeTest() {

        // Arrange
        int patientId = 1;
        PatientDto patientDto = new PatientDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = patientController.updatePatient(patientId, patientDto, bindingResult);

        // Assert
        assertEquals("patient/update", viewName);
        verify(patientProxy, never()).updatePatient(anyInt(), any(PatientDto.class));
    }


    @Test
    @DisplayName("Should redirect to /patient/list")
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
