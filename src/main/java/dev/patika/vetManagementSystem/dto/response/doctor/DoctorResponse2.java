package dev.patika.vetManagementSystem.dto.response.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse2 {
    private long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
}
