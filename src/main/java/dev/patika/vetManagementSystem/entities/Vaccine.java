package dev.patika.vetManagementSystem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vaccines")

public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id", columnDefinition = "serial")
    private long id; // vaccine_id

    @NotNull
    @NotEmpty
    @Column(name = "vaccine_name")
    private String name; // vaccine_name

    @NotNull
    @NotEmpty
    @Column(name = "vaccine_code")
    private String code; // vaccine_code

    @NotNull
    @Column(name = "protection_start_date")
    private LocalDate protectionStartDate; // protection_start_date

    @NotNull

    @Column(name = "protection_end_date")
    private LocalDate protectionEndDate; // protection_end_date

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_vaccine_id", referencedColumnName = "animal_id")
    private Animal animal;

    @JsonIgnore
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "vaccine_report_id" , referencedColumnName = "report_id")
    private Report report;

    @Override
    public String toString() {
        return "Vaccine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", protectionStartDate=" + protectionStartDate +
                ", protectionEndDate=" + protectionEndDate +
                ", animal=" + animal +
                ", report=" + report +
                '}';
    }
}
