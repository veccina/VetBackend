package dev.patika.vetManagementSystem.dto.response.animal;

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
public class AnimalResponse5 {
    private List<AnimalResponse2> animalList;
    private List<Vaccine> vaccineList;
    private List<String> reportTitles;
}
