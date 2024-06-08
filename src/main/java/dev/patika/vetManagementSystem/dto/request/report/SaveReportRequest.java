package dev.patika.vetManagementSystem.dto.request.report;

import dev.patika.vetManagementSystem.entities.Appointment;
import dev.patika.vetManagementSystem.entities.Vaccine;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveReportRequest {

    private String title;
    private String diagnosis;
    private double price;
    private Appointment appointment;

}
