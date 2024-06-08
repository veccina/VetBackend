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
@Table(name = "reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", columnDefinition = "serial")
    private long id; // report_id

    @NotNull
    @NotEmpty
    @Column(name = "report_title")
    private String title; // report_title

    @NotNull
    @NotEmpty
    @Column(name = "report_diagnosis")
    private String diagnosis; // report_diagnosis

    @NotNull

    @Column(name = "report_price")
    private double price; // report_price

    @OneToOne
    @JoinColumn (name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "report" , cascade = CascadeType.REMOVE)
    private List<Vaccine> vaccineList;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", diagnosis='" + diagnosis + '\'' +
                ", price=" + price +
                ", appointment=" + appointment +
                '}';
    }
}
