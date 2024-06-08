package dev.patika.vetManagementSystem.dto.response.report;

import dev.patika.vetManagementSystem.dto.response.appointment.AppointmentResponse2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse2 {

    private Long id;
    private String title;
    private String diagnosis;
    private double price;
    private AppointmentResponse2 appointment;
}
