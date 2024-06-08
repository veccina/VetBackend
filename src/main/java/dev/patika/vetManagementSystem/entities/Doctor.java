package dev.patika.vetManagementSystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "doctor_id", columnDefinition = "serial")
        private long id; // doctor_id

        @NotNull
        @NotEmpty
        @Column(name = "doctor_name")
        private String name; // doctor_name

        @NotNull
        @NotEmpty
        @Column(name = "doctor_phone")
        private String phone; // doctor_phone

        @NotNull
        @NotEmpty
        @Column(name = "doctor_mail")
        private String mail; // doctor_mail

        @NotNull
        @NotEmpty
        @Column(name = "doctor_address")
        private String address; // doctor_address

        @NotNull
        @NotEmpty
        @Column(name = "doctor_city")
        private String city; // doctor_city

        @OneToMany(mappedBy = "doctor" , cascade = CascadeType.ALL)
        private List<AvailableDate> availableDateList;

        @OneToMany(mappedBy = "doctor" , cascade = CascadeType.ALL)
        private List<Appointment> appointmentList;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
