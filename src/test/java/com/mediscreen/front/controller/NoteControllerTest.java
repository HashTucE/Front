package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.NoteProxy;
import com.mediscreen.library.dto.NoteDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteControllerTest {


    @InjectMocks
    private NoteController noteController;
    @Mock
    private NoteProxy noteProxy;
    @Mock
    private BindingResult result;


    @Test
    @DisplayName("Should redirect to the record page when note is valid")
    void addNoteValidTest() {

        // Arrange
        NoteDto noteDto = new NoteDto();
        int patientId = 1;
        BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

        // Act
        String viewName = noteController.addNote(patientId, noteDto, result);

        // Assert
        verify(noteProxy).validateNote(noteDto);
        assertEquals("redirect:/record/patient/" + patientId, viewName);
    }

    @Test
    @DisplayName("Should return add note form when note is invalid")
    void addNoteInvalidTest() {

        // Arrange
        NoteDto noteDto = new NoteDto();
        int patientId = 1;
        when(result.hasErrors()).thenReturn(true);

        // Act
        String viewName = noteController.addNote(patientId, noteDto, result);

        // Assert
        verifyNoInteractions(noteProxy);
        assertEquals("/note/add", viewName);
    }

    @Test
    @DisplayName("Should redirect to the record page after deleting note")
    void deleteNoteTest() {

        // Arrange
        int patientId = 1;
        int noteId = 1;

        // Act
        String viewName = noteController.deleteNote(patientId, noteId);

        // Assert
        verify(noteProxy).deleteNote(noteId);
        assertEquals("redirect:/record/patient/" + patientId, viewName);
    }
}
