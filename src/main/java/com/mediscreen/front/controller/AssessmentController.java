package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.AssessmentProxy;
import com.mediscreen.front.proxy.NoteProxy;
import com.mediscreen.library.dto.NoteDto;
import com.mediscreen.library.dto.RecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AssessmentController {


    private final AssessmentProxy assessmentProxy;
    private final NoteProxy noteProxy;

    public AssessmentController(AssessmentProxy assessmentProxy, NoteProxy noteProxy) {
        this.assessmentProxy = assessmentProxy;
        this.noteProxy = noteProxy;
    }

    private static final Logger log = LogManager.getLogger(AssessmentController.class);


    /**
     * Handles GET requests for generating a patient record.
     * @param id the ID of the patient.
     * @param model the Model object containing the record data and note form data.
     * @return the name of the Thymeleaf template for rendering the record.
     */
    @GetMapping("/record/patient/{id}")
    @Operation(summary = "Generate patient record submission form",
            description = "Generates a form to submit a patient record based on the given patient ID.")
    @ApiResponse(responseCode = "200", description = "Patient record form generated successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Patient with the given ID not found")
    public String generateRecordSubmit(@Parameter(description = "ID of the patient to generate a record for", required = true) @PathVariable int id, Model model) {

        log.info("Submitting the form for generating a report with patient id: " + id);
        RecordDto record = assessmentProxy.generateRecord(id);
        model.addAttribute("record", record);
        model.addAttribute("noteDto", new NoteDto());
        return "assessment/record";
    }


    @PostMapping("/record/patient/{patientId}/addNote")
    @Operation(summary = "Add a new note for a patient",
            description = "Adds a new note for the patient with the given ID.")
    @ApiResponse(responseCode = "302", description = "Note added successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "400", description = "Invalid note data provided")
    public String addNote(@Parameter(description = "ID of the patient to add a note for", required = true) @PathVariable int patientId,
                          @Parameter(description = "DTO object containing the new note information", required = true) @Valid NoteDto noteDto,
                          BindingResult result, Model model) {

        if (result.hasErrors()) {
            log.info("Validation errors for new note: {}", noteDto);
            RecordDto record = assessmentProxy.generateRecord(patientId);
            model.addAttribute("record", record);
            model.addAttribute("noteDto", noteDto);
            return "assessment/record";
        }

        log.info("Validating a new note: {}", noteDto);
        noteDto.setPatientId(patientId);
        noteProxy.validateNote(noteDto);

        return "redirect:/record/patient/" + patientId;
    }


    /**
     * Handles GET requests for displaying a form to update a note.
     * @param patientId the ID of the patient.
     * @param noteId the ID of the note.
     * @param model the Model object containing the record data and note form data.
     * @return the name of the Thymeleaf template for rendering the note update form.
     */
    @GetMapping("/record/patient/{patientId}/updateNote/{noteId}")
    @Operation(summary = "Display form to update patient note",
            description = "Displays a form to update a note for the given patient and note ID.")
    @ApiResponse(responseCode = "200", description = "Update note form displayed successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Patient or note with the given ID not found")
    public String displayUpdateNote(@Parameter(description = "ID of the patient to update a note for", required = true) @PathVariable int patientId,
                                    @Parameter(description = "ID of the note to update", required = true) @PathVariable int noteId, Model model) {

        log.info("Displaying the form for updating the note with id: {}", noteId);
        RecordDto record = assessmentProxy.generateRecord(patientId);
        model.addAttribute("record", record);
        NoteDto noteDto = noteProxy.findNoteById(noteId);
        model.addAttribute("noteDto", noteDto);
        model.addAttribute("patientId", patientId);
        return "assessment/updateRecord";
    }


    /**
     * Handles POST requests for submitting a note update.
     * @param patientId the ID of the patient.
     * @param noteId the ID of the note.
     * @param noteDto the NoteDto object containing the updated note data.
     * @param result the BindingResult object for validating the note form data.
     * @param model the Model object containing the record data and note form data.
     * @return a redirect to the patient record page.
     */
    @PostMapping("/record/patient/{patientId}/updateNote/{noteId}")
    @Operation(summary = "Submit updated note for patient",
            description = "Submits an updated note for the given patient and note ID.")
    @ApiResponse(responseCode = "302", description = "Note updated successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "404", description = "Patient or note with the given ID not found")
    public String submitUpdateNote(@Parameter(description = "ID of the patient to update a note for", required = true) @PathVariable int patientId,
                                   @Parameter(description = "ID of the note to update", required = true) @PathVariable int noteId,
                                   @Parameter(description = "DTO object containing the updated note information", required = true) @Valid NoteDto noteDto,
                                   BindingResult result, Model model) {

        RecordDto record = assessmentProxy.generateRecord(patientId);
        if (result.hasErrors()) {
            log.warn("Failed to update note with id: {}. Validation failed.", noteId);
            model.addAttribute("record", record);
            model.addAttribute("patientId", patientId);
            model.addAttribute("noteDto", noteDto);
            return "redirect:/record/patient/" + patientId + "/updateNote/" + noteId;
        }
        noteProxy.updateNote(noteId, noteDto);
        log.info("Note with id {} was successfully updated", noteId);
        return "redirect:/record/patient/" + record.getPatientId();
    }


    /**
     Handles the request to delete a note.
     @param id the id of the note to delete
     @return the view of the note list page
     */
    @PostMapping("/record/patient/{patientId}/deleteNote/{id}")
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
