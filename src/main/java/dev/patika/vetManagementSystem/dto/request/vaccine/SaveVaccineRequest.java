package dev.patika.vetManagementSystem.dto.request.vaccine;

import dev.patika.vetManagementSystem.entities.Animal;
import dev.patika.vetManagementSystem.entities.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveVaccineRequest {

    private String name;
    private String code;
    private LocalDate protectionStartDate;
    private LocalDate protectionEndDate;
    private Animal animal;
    private Report report;
}
