package com.skillspace.career.Test;


import com.skillspace.career.Model.Career;
import com.skillspace.career.Service.CareerService;
import com.skillspace.career.controllers.CareerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

    @RestController
    public class CareerIntergrationTest {

        @Mock
        private CareerService careerService;

        @InjectMocks
        private CareerController careerController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testSaveCareerAsDraft() {
            Career career = new Career();
            career.setName("Software Developer");
            career.setDescription("Develop software applications.");

            when(careerService.saveAsDraft(career)).thenReturn(career);

            ResponseEntity<Career> response = careerController.saveCareerAsDraft(career);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(career, response.getBody());
        }
    }

