package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.PatientProxy;
import com.mediscreen.library.dto.PatientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PatientController {


    private final PatientProxy patientProxy;

    public PatientController(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    private static final Logger log = LogManager.getLogger(PatientController.class);



    /**
     * Handles the request to display the list of patients.
     * @param model the model to store the list of patients
     * @return the view to display the list of patients
     */
    @GetMapping("/patient/list")
    @Operation(summary = "Get the list of patients",
            description = "Retrieves a list of all patients in the system.")
    @ApiResponse(responseCode = "200", description = "List of patients returned successfully",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    public String home(Model model){
        log.info("Displaying the list of patients");
        List<PatientDto> patients =  patientProxy.getPatientsList();
        model.addAttribute("patients", patients);
        return "patient/list";
    }


    /**
     * Handles the request to display the form for adding a patient.
     * @param model the model to store the patient information
     * @return the view to display the add patient form
     */
    @GetMapping("/patient/add")
    @Operation(summary = "Display the form for adding a new patient",
            description = "Displays the form for adding a new patient.")
    @ApiResponse(responseCode = "200", description = "The form for adding a new patient is displayed",
            content = @Content(mediaType = "text/html", schema = @Schema(type = "string")))
    public String addPatientForm(Model model) {
        log.info("Displaying the form for adding a patient");
        model.addAttribute("patientDto", new PatientDto());
        return "patient/add";
    }


    /**
     * Handles the request to add a patient.
     * @param patientDto the patient information
     * @param result the result of validation
     * @return the view to redirect to the list of patients or the add patient form
     */
    @PostMapping("/patient/validate")
    @Operation(summary = "Add a new patient", description = "Adds a new patient to the system.")
    @ApiResponse(responseCode = "302", description = "Patient validated and redirected to the list page.")
    public String addPatient(@Valid PatientDto patientDto, BindingResult result) {

        if (!result.hasErrors()) {
            log.info("Validating a new patient: {}", patientDto);
            patientProxy.validatePatient(patientDto);
            return "redirect:/patient/list";
        }
        return "/patient/add";
    }


    /**
     * Handles the request to display the form for updating a patient.
     * @param id the id of the patient to update
     * @param model the model to store the patient information
     * @return the view to display the update patient form
     */
    @GetMapping("/patient/update/{id}")
    @Operation(summary = "Display the form to update a patient", description = "Display the form to update a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient form displayed", content = @Content(mediaType = "text/html")),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "text/plain"))
    })
    public String updatePatientForm(@PathVariable int id, Model model) {

        log.info("Displaying the form for updating the patient with id: {}", id);
        PatientDto patientDto = patientProxy.findPatientById(id);
        model.addAttribute("patientDto", patientDto);

        LocalDate birthdate = patientDto.getBirthdate();
        String birthdateAsString = birthdate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        model.addAttribute("birthdate", birthdateAsString);
        return "patient/update";
    }


    /**
     Handles the request to update a patient.
     @param id the id of the patient to update
     @param patientDto the patient data
     @param result the binding result
     @return the view of the patient update page or the patient list page if the update was successful
     */
    @PostMapping("/patient/update/{id}")
    @Operation(summary = "Update an existing patient", description = "Update an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Patient updated successfully and redirected to the patient list page"),
            @ApiResponse(responseCode = "400", description = "Bad request, validation error(s) occurred")})
    public String updatePatient(@PathVariable int id, @Valid PatientDto patientDto, BindingResult result) {
        log.debug("Updating patient with id {}", id);
        if (!result.hasErrors()) {
            patientProxy.updatePatient(id, patientDto);
            log.info("Patient with id {} was successfully updated", id);
            return "redirect:/patient/list";
        }
        log.debug("Error(s) updating patient with id {}: {}", id, result.getAllErrors());
        return "/patient/update";
    }


    /**
     Handles the request to delete a patient.
     @param id the id of the patient to delete
     @return the view of the patient list page
     */
    @PostMapping("/patient/delete/{id}")
    @Operation(summary = "Delete a patient by ID", description = "Delete a patient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public String deletePatient(@PathVariable int id) {
        log.debug("Deleting patient with id {}", id);
        patientProxy.deletePatient(id);
        log.info("Patient with id {} was successfully deleted", id);
        return "redirect:/patient/list";
    }
}
