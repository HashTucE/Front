package com.mediscreen.front.proxy;

import com.mediscreen.library.dto.RecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assessment.ms", url = "${proxy.assessment.url}")
public interface AssessmentProxy {


    @GetMapping("/api/assessment/record/{patientId}")
    RecordDto generateRecord(@PathVariable int patientId);
}
