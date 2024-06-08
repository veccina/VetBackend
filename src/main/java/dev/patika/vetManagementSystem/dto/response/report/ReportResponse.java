package dev.patika.vetManagementSystem.dto.response.report;

import dev.patika.vetManagementSystem.dto.response.appointment.AppointmentResponse2;
import dev.patika.vetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.vetManagementSystem.dto.response.vaccine.VaccineResponse2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ReportResponse {

    private Long id;
    private String title;
    private String diagnosis;
    private double price;
    private AppointmentResponse2 appointment;
    private List<VaccineResponse2>  vaccineList;
}
