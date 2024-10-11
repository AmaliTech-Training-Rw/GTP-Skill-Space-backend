package com.skillspace.application.Controller;


import com.skillspace.application.dto.CareerApplicationDTO;
import com.skillspace.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<?> applyToCareer(@RequestBody CareerApplicationDTO applicationDTO) {
        try {
            String result = applicationService.applyToCareer(applicationDTO);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while processing your application");
        }
    }

}
