package dev.patika.vetManagementSystem.dto.request.report;

import dev.patika.vetManagementSystem.entities.Appointment;
import dev.patika.vetManagementSystem.entities.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReportRequest {

    private long id;
    private String title;
    private String diagnosis;
    private double price;
    private Appointment appointment;

}
