package com.mediscreen.front.controller;

import com.mediscreen.front.proxy.PatientProxy;
import com.mediscreen.library.dto.PatientDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String addPatient(@Valid PatientDto patientDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            log.info("Validation errors for new patient: {}", patientDto);
            model.addAttribute("patientDto", patientDto);
            return "patient/add";
        }

        log.info("Validating a new patient: {}", patientDto);
        patientProxy.validatePatient(patientDto);
        return "redirect:/patient/list";
    }


    /**
     * Handles the request to display the form for updating a patient.
     * @param id the id of the patient to update
     * @param model the model to store the patient information
     * @return the view to display the update patient form
     */
    @GetMapping("/patient/update/{id}")
    public String updatePatientForm(@PathVariable int id, Model model) {

        log.info("Displaying the form for updating the patient with id: {}", id);
        PatientDto patientDto = patientProxy.findPatientById(id);
        model.addAttribute("patientDto", patientDto);
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
    public String updatePatient(@PathVariable int id, @Valid PatientDto patientDto, BindingResult result) {
        log.debug("Updating patient with id {}", id);
        if (!result.hasErrors()) {
            patientProxy.updatePatient(id, patientDto);
            log.info("Patient with id {} was successfully updated", id);
            return "redirect:/patient/list";
        }
        log.debug("Error(s) updating patient with id {}: {}", id, result.getAllErrors());
        return "patient/update";
    }


    /**
     Handles the request to delete a patient.
     @param id the id of the patient to delete
     @return the view of the patient list page
     */
    @PostMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable int id) {
        log.debug("Deleting patient with id {}", id);
        patientProxy.deletePatient(id);
        log.info("Patient with id {} was successfully deleted", id);
        return "redirect:/patient/list";
    }
}
