package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.NoteProxy;
import com.mediscreen.library.dto.NoteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NoteController {


    private final NoteProxy noteProxy;

    public NoteController(NoteProxy noteProxy) {
        this.noteProxy = noteProxy;
    }

    private static final Logger log = LogManager.getLogger(NoteController.class);



    /**
     * Handles the request to add a note.
     * @param noteDto the note information
     * @param result the result of validation
     * @return the view to redirect to the list of notes or the add note form
     */
    @PostMapping("/note/validate/{patientId}")
    @Operation(summary = "Add a new note for a patient",
            description = "Adds a new note for the patient with the given ID.")
    @ApiResponse(responseCode = "302", description = "Note added successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "400", description = "Invalid note data provided")
    public String addNote(@Parameter(description = "ID of the patient to add a note for", required = true) @PathVariable int patientId,
                          @Parameter(description = "DTO object containing the new note information", required = true) @Valid NoteDto noteDto,
                          BindingResult result) {

        if (!result.hasErrors()) {
            log.info("Validating a new note: {}", noteDto);
            noteDto.setPatientId(patientId);
            noteProxy.validateNote(noteDto);
            return "redirect:/record/patient/" + patientId;
        }
        return "/note/add";
    }


    /**
     Handles the request to delete a note.
     @param id the id of the note to delete
     @return the view of the note list page
     */
    @PostMapping("/note/delete/{patientId}/{id}")
    @Operation(summary = "Delete a note for a patient",
            description = "Deletes the note with the given ID for the patient with the given ID.")
    @ApiResponse(responseCode = "302", description = "Note deleted successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    public String deleteNote(@Parameter(description = "ID of the patient to delete a note for", required = true) @PathVariable int patientId,
                             @Parameter(description = "ID of the note to delete", required = true) @PathVariable int id) {
        log.debug("Deleting note with id {}", id);
        noteProxy.deleteNote(id);
        log.info("Note with id {} was successfully deleted", id);
        return "redirect:/record/patient/" + patientId;
    }
}
