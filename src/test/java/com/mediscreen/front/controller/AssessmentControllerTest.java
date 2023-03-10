package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.AssessmentProxy;
import com.mediscreen.front.proxy.NoteProxy;
import com.mediscreen.library.dto.NoteDto;
import com.mediscreen.library.dto.RecordDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssessmentControllerTest {


    @InjectMocks
    private AssessmentController assessmentController;
    @Mock
    private AssessmentProxy assessmentProxy;
    @Mock
    private NoteProxy noteProxy;
    @Mock
    private BindingResult result;
    @Mock
    private Model model;


    @Test
    @DisplayName("Should generate record")
    public void generateRecordSubmitTest() {

        // Arrange
        int patientId = 1;
        RecordDto expectedRecordDto = new RecordDto();
        when(assessmentProxy.generateRecord(patientId)).thenReturn(expectedRecordDto);
        Model model = new ExtendedModelMap();

        // Act
        String result = assessmentController.generateRecordSubmit(patientId, model);

        // Assert
        assertEquals("assessment/record", result);
        assertEquals(expectedRecordDto, model.getAttribute("record"));
        assertNotNull(model.getAttribute("noteDto"));
    }


    @Test
    @DisplayName("Should add a new note for a patient")
    public void addNoteTest() {

        // Arrange
        int patientId = 1;
        NoteDto noteDto = new NoteDto();
        noteDto.setPatientId(patientId);
        BindingResult result = new BeanPropertyBindingResult(noteDto, "noteDto");
        Model model = new ExtendedModelMap();

        // Act
        String resultUrl = assessmentController.addNote(patientId, noteDto, result, model);

        // Assert
        assertEquals("redirect:/record/patient/" + patientId, resultUrl);
        assertFalse(result.hasErrors());
    }


    @Test
    @DisplayName("Should return error message for invalid note data provided")
    public void addNoteNegativeTest() {

        // Arrange
        int patientId = 1;
        NoteDto noteDto = new NoteDto();
        noteDto.setPatientId(patientId);
        when(result.hasErrors()).thenReturn(true);
        RecordDto recordDto = new RecordDto();
        when(assessmentProxy.generateRecord(patientId)).thenReturn(recordDto);

        // Act
        String resultUrl = assessmentController.addNote(patientId, noteDto, result, model);

        // Assert
        assertEquals("assessment/record", resultUrl);
        assertTrue(result.hasErrors());
    }


    @Test
    @DisplayName("Should display update note form")
    public void displayUpdateNoteTest() {

        // Arrange
        int patientId = 1;
        int noteId = 2;
        RecordDto record = new RecordDto();
        NoteDto note = new NoteDto();
        Model model = new ConcurrentModel();
        when(assessmentProxy.generateRecord(patientId)).thenReturn(record);
        when(noteProxy.findNoteById(noteId)).thenReturn(note);

        // Act
        String viewName = assessmentController.displayUpdateNote(patientId, noteId, model);

        // Assert
        assertEquals("assessment/updateRecord", viewName);
        assertEquals(record, model.getAttribute("record"));
        assertEquals(note, model.getAttribute("noteDto"));
        assertEquals(patientId, model.getAttribute("patientId"));
    }


    @Test
    @DisplayName("Should redirect to record patient")
    public void submitUpdateNoteTest() {

        // Arrange
        int patientId = 1;
        int noteId = 2;
        NoteDto noteDto = new NoteDto(patientId, "n");
        RecordDto recordDto = new RecordDto();
        recordDto.setPatientId(patientId);
        when(assessmentProxy.generateRecord(patientId)).thenReturn(recordDto);
        when(result.hasErrors()).thenReturn(false);

        // Act
        String resultUrl = assessmentController.submitUpdateNote(patientId, noteId, noteDto, result, model);

        // Assert
        assertEquals("redirect:/record/patient/" + recordDto.getPatientId(), resultUrl);
        verify(noteProxy, times(1)).updateNote(noteId, noteDto);
    }


    @Test
    @DisplayName("Should redirect to update note form when validation fails")
    public void submitUpdateNoteNegativeTest() {

        // Arrange
        int patientId = 1;
        int noteId = 2;
        RecordDto record = new RecordDto();
        NoteDto noteDto = new NoteDto(1, "note");
        Model model = new ConcurrentModel();
        when(assessmentProxy.generateRecord(patientId)).thenReturn(record);
        when(result.hasErrors()).thenReturn(true);

        // Act
        String redirectUrl = assessmentController.submitUpdateNote(patientId, noteId, noteDto, result, model);

        // Assert
        assertEquals("redirect:/record/patient/" + patientId + "/updateNote/" + noteId, redirectUrl);
        verify(noteProxy, times(0)).updateNote(noteId, noteDto);
        verify(result, times(1)).hasErrors();
        assertTrue(model.containsAttribute("noteDto"));
        assertTrue(model.containsAttribute("patientId"));
        assertEquals(record, model.getAttribute("record"));
    }


    @Test
    @DisplayName("Should delete note for patient redirecting to record")
    public void deleteNoteTest() {

        // Arrange
        int patientId = 1;
        int noteId = 2;
        doNothing().when(noteProxy).deleteNote(noteId);

        // Act
        String result = assessmentController.deleteNote(patientId, noteId);

        // Assert
        assertEquals("redirect:/record/patient/" + patientId, result);
    }


}
