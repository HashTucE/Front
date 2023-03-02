package com.mediscreen.front.proxy;

import com.mediscreen.library.dto.PatientDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patient.ms", url = "${proxy.patient.url}")
public interface PatientProxy {


    @GetMapping("/api/patient/find/{id}")
    PatientDto findPatientById(@PathVariable int id);

    @GetMapping(value = "/api/patient/list")
    List<PatientDto> getPatientsList();

    @PostMapping("/api/patient/validate")
    PatientDto validatePatient(@Valid @RequestBody PatientDto patientDto);

    @PostMapping("/api/patient/update/{id}")
    PatientDto updatePatient(@PathVariable int id, @Valid @RequestBody PatientDto patientDto);

    @PostMapping("/api/patient/delete/{id}")
    void deletePatient(@PathVariable int id);
}
