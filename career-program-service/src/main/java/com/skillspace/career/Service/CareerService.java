package com.skillspace.career.Service;



import com.skillspace.career.Model.Career;
import com.skillspace.career.Repository.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    @Autowired
    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public Career createCareer(Career career) {
        if (career.getId() == null) {
            career.setId(UUID.randomUUID()); // Generates a unique ID if null
        }
        return careerRepository.save(career);
    }

    public Career updateCareer(UUID id, Career careerDetails) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found"));

        career.setName(careerDetails.getName());
        career.setDescription(careerDetails.getDescription());
        career.setStartDate(careerDetails.getStartDate());
        career.setEndDate(careerDetails.getEndDate());
        career.setStatus(careerDetails.getStatus());
        career.setRequirements(careerDetails.getRequirements());
        career.setRequiredBadges(careerDetails.getRequiredBadges());
        career.setOptionalBadges(careerDetails.getOptionalBadges());

        return careerRepository.save(career);
    }

    public List<Career> getPublishedCareers() {
        return careerRepository.findByStatus("published");
    }
    public List<Career> getAllPrograms() {
        return careerRepository.findAll();
    }
    public Career saveAsDraft(Career career) {
        career.setStatus("draft");
        return careerRepository.save(career);
    }
    public Career saveAsPublished(Career career) {
        career.setStatus("published");
        return careerRepository.save(career);
    }

    public List<Career> getDraftCareers() {
        return careerRepository.findByStatus("draft");
    }

    public void deleteCareer(UUID id) {
        careerRepository.deleteById(id);
    }
}
