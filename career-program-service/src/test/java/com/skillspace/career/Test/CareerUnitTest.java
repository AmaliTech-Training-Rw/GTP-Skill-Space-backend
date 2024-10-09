package com.skillspace.career.Test;


import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import com.skillspace.career.Service.CareerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CareerUnitTest {

    @Mock
    private CareerRepository careerRepository;

    @InjectMocks
    private CareerService careerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCareer() {
        Career career = new Career();
        career.setName("Software Developer");
        career.setDescription("Develop software applications.");
        when(careerRepository.save(any(Career.class))).thenReturn(career);

        Career createdCareer = careerService.createCareer(career);

        assertNotNull(createdCareer);
        assertEquals("Software Developer", createdCareer.getName());
        verify(careerRepository, times(1)).save(career);
    }

    @Test
    void testUpdateCareer() {
        UUID id = UUID.randomUUID();
        Career existingCareer = new Career();
        existingCareer.setId(id);
        existingCareer.setName("Software Developer");

        Career careerDetails = new Career();
        careerDetails.setName("Senior Software Developer");

        when(careerRepository.findById(id)).thenReturn(Optional.of(existingCareer));
        when(careerRepository.save(any(Career.class))).thenReturn(existingCareer);

        Career updatedCareer = careerService.updateCareer(id, careerDetails);

        assertEquals("Senior Software Developer", updatedCareer.getName());
        verify(careerRepository, times(1)).findById(id);
        verify(careerRepository, times(1)).save(existingCareer);
    }
}

