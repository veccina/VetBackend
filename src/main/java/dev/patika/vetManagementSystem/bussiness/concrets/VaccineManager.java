package dev.patika.vetManagementSystem.bussiness.concrets;

import dev.patika.vetManagementSystem.bussiness.abstracts.IAnimalService;
import dev.patika.vetManagementSystem.bussiness.abstracts.ICustomerService;
import dev.patika.vetManagementSystem.bussiness.abstracts.IReportService;
import dev.patika.vetManagementSystem.bussiness.abstracts.IVaccineService;
import dev.patika.vetManagementSystem.core.config.modelmapper.IModelMapperService;
import dev.patika.vetManagementSystem.core.config.utilies.Msg;
import dev.patika.vetManagementSystem.core.config.utilies.ResultHelper;
import dev.patika.vetManagementSystem.core.exception.NotFoundException;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dao.VaccineRepo;
import dev.patika.vetManagementSystem.dto.request.vaccine.SaveVaccineRequest;
import dev.patika.vetManagementSystem.dto.request.vaccine.UpdateVaccineRequest;
import dev.patika.vetManagementSystem.dto.response.animal.AnimalResponse;
import dev.patika.vetManagementSystem.dto.response.animal.AnimalResponse4;
import dev.patika.vetManagementSystem.dto.response.animal.AnimalResponse5;
import dev.patika.vetManagementSystem.dto.response.availableDateResponse.AvailableDateResponse2;
import dev.patika.vetManagementSystem.dto.response.vaccine.VaccineResponse;
import dev.patika.vetManagementSystem.dto.response.vaccine.VaccineResponse2;
import dev.patika.vetManagementSystem.entities.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineManager implements IVaccineService {


    private final VaccineRepo vaccineRepo;

    private final IAnimalService animalService;

    private final IModelMapperService modelMapper;

    private final IReportService reportService;






    @Override
    public Vaccine save(Vaccine vaccine) {


        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public ResultData<VaccineResponse> save2(SaveVaccineRequest saveVaccineRequest) {
        try {
            Vaccine newVaccine = this.modelMapper.forRequest().map(saveVaccineRequest, Vaccine.class);

            Animal animal = this.animalService.get(saveVaccineRequest.getAnimal().getId());
            newVaccine.setAnimal(animal);

            Report report = this.reportService.getById(saveVaccineRequest.getReport().getId());
            newVaccine.setReport(report);

            List<Vaccine> existingVaccines = animal.getVaccineList();
            List<Vaccine> vaccinesToDelete = new ArrayList<>();

            for (Vaccine existingVaccine : existingVaccines) {
                if (existingVaccine.getName().equals(newVaccine.getName())
                        && existingVaccine.getProtectionEndDate() != null
                        && LocalDate.now().isAfter(existingVaccine.getProtectionEndDate())) {
                    vaccinesToDelete.add(existingVaccine);
                }
            }


            for (Vaccine vaccineToDelete : vaccinesToDelete) {
                this.vaccineRepo.delete(vaccineToDelete);
            }


            for (Vaccine existingVaccine : existingVaccines) {
                if (existingVaccine.getName().equals(newVaccine.getName())
                        && existingVaccine.getProtectionEndDate() != null
                        && LocalDate.now().isBefore(existingVaccine.getProtectionEndDate())) {
                    return ResultData.error("Aynı isimdeki aşı için koruyuculuk bitiş tarihi devam ediyor.", "500");
                }
            }



            this.vaccineRepo.save(newVaccine);

            return ResultHelper.created(this.modelMapper.forResponse().map(newVaccine, VaccineResponse.class));
        } catch (Exception e) {
            return ResultData.error(Msg.VALIDATE_ERROR, "500");
        }
    }


    @Override
    public Vaccine get(long id) {
        return this.vaccineRepo.findById(id).orElseThrow(() ->  new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public boolean delete(long id) {
        try {
            Vaccine vaccine = this.get(id);
            this.vaccineRepo.delete(vaccine);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Vaccine getVaccineByName(String name) {
        return vaccineRepo.findByNameIgnoreCase(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }



    @Override
    public List<Vaccine> getVaccinesByName(String vaccineName) {
        return vaccineRepo.findByName(vaccineName);
    }

    @Override
    public List<Animal> getAnimalsByVaccineAndAnimalNames(String vaccineName, String animalName) {
        List<Vaccine> vaccines = vaccineRepo.findByNameIgnoreCase(vaccineName);
        List<Animal> matchingAnimals = new ArrayList<>();

        for (Vaccine vaccine : vaccines) {
            Animal animal = vaccine.getAnimal();
            if (animal != null && animal.getName().equalsIgnoreCase(animalName)) {
                matchingAnimals.add(animal);
            }
        }

        return matchingAnimals;
    }

    @Override
    public ResultData<List<AnimalResponse4>> getVaccineAndAnimal(String vaccineName, String animalName) {
        try {

            List<Animal> matchingAnimals = getAnimalsByVaccineAndAnimalNames(vaccineName, animalName);


            List<AnimalResponse4> animalResponses = matchingAnimals.stream()
                    .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse4.class))
                    .collect(Collectors.toList());

            List<Vaccine> vaccines = animalResponses.stream()
                    .filter(animal -> animal.getVaccineList().stream()
                            .anyMatch(vaccine -> vaccine.getName().equalsIgnoreCase(vaccineName)))
                    .map(animal -> animal.getVaccineList().stream()
                            .filter(vaccine -> vaccine.getName().equalsIgnoreCase(vaccineName))
                            .findFirst().orElse(null)) // findFirst().orElse(null) ile null kontrolü ekledim
                    .filter(Objects::nonNull) // Null kontrollerini temizle
                    .collect(Collectors.toList());


            for (Vaccine vaccine : vaccines) {
                if (vaccine.getReport() != null) {
                    Report report = reportService.getById(vaccine.getReport().getId());
                    vaccine.setReport(report);
                    // Her hayvan için rapor adını ayarla
                    animalResponses.stream()
                            .filter(animal -> animal.getVaccineList().contains(vaccine))
                            .findFirst()
                            .ifPresent(animal -> animal.setReportTitle(report.getTitle()));
                }
            }

            animalResponses.forEach(animal -> animal.setVaccineList(vaccines));

            if (animalResponses.isEmpty()) {
                return ResultData.error("Belirtilen aşı veya hayvan adına göre veri bulunamadı.", "404");
            }

            // Başarı durumunda sonuçları döndür
            return ResultHelper.success(animalResponses);
        } catch (NotFoundException ex) {
            // Eğer bulunamayan bir durum oluştuysa hata mesajı döndür
            return ResultData.error(Msg.NOT_FOUND, "404");
        }
    }

    @Override
    public ResultData<List<VaccineResponse>> get2(LocalDate protectionStartDate, LocalDate protectionEndDate)
            {

        if (protectionStartDate.isAfter(protectionEndDate)) {
            return ResultData.error("Başlangıç tarihi, bitiş tarihinden sonra olamaz.", "404");
        }
        try {
            List<Vaccine> vaccinesWithUpcomingProtection = vaccineRepo.findByProtectionStartDateAfterAndProtectionEndDateBefore(protectionStartDate, protectionEndDate);

            if (vaccinesWithUpcomingProtection.isEmpty()) {
                return ResultData.error("Belirtilen tarih aralığında aşı bulunmamaktadır.", "404");
            }

            List<VaccineResponse> vaccineResponses = vaccinesWithUpcomingProtection.stream()
                    .map(vaccine -> modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .collect(Collectors.toList());

            return ResultHelper.success(vaccineResponses);
        } catch (NotFoundException ex) {
            return ResultData.error(Msg.NOT_FOUND, "404");
        }
    }

    @Override
    public ListResult<VaccineResponse> get3( String name) {
        try {
            List<Vaccine> vaccines = this.vaccineRepo.findByNameIgnoreCase(name);

            List<VaccineResponse> vaccineResponses = vaccines.stream()
                    .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .collect(Collectors.toList());

            if (vaccineResponses.isEmpty()) {
                return ResultHelper.notFoundErrorList(Msg.NOT_FOUND);
            }

            return ResultHelper.successList(vaccineResponses);
        } catch (NotFoundException ex) {
            return ResultHelper.notFoundErrorList(ex.getMessage());
        }
    }

    @Override
    public ResultData<VaccineResponse> update2( UpdateVaccineRequest updateVaccineRequest) {
        Long customerId = updateVaccineRequest.getId();

        try {

            Vaccine existingVaccine = this.vaccineRepo.findById(customerId).orElseThrow();


            this.modelMapper.forRequest().map(updateVaccineRequest, existingVaccine);


            Animal animal = null;

            try {
                animal = this.animalService.get(updateVaccineRequest.getAnimal().getId());
            } catch (NotFoundException e) {
                return ResultData.error("Hayvan bulunamadı.", "404");
            }

            existingVaccine.setAnimal(animal);


            Report report = null;

            try {
                report = this.reportService.getById(updateVaccineRequest.getReport().getId());
            } catch (NotFoundException e) {
                return ResultData.error("Rapor bulunamadı.", "404");
            }

            existingVaccine.setReport(report);



            List<Vaccine> existingVaccinesforAnimal = animal.getVaccineList();
            for (Vaccine vaccine : existingVaccinesforAnimal) {
                if (vaccine.getId() != existingVaccine.getId()
                        && vaccine.getName().equals(updateVaccineRequest.getName())) {
                    return ResultData.error("Hayvan zaten bu aşıya sahip.", "500");
                }
            }


            List<Vaccine> existingVaccines = animal.getVaccineList();
            for (Vaccine vaccine : existingVaccines) {

                if (vaccine.getId() != existingVaccine.getId()
                        && vaccine.getName().equals(updateVaccineRequest.getName())
                        && vaccine.getProtectionEndDate() != null
                        && LocalDate.now().isBefore(vaccine.getProtectionEndDate())) {
                    return ResultData.error("Aynı isimdeki aşı için koruyuculuk bitiş tarihi devam ediyor.", "500");
                }
            }


            this.vaccineRepo.save(existingVaccine);

            return ResultHelper.success(this.modelMapper.forResponse().map(existingVaccine, VaccineResponse.class));
        } catch (NotFoundException e) {
            return ResultData.error("Aşı bulunamadı.", "404");
        } catch (Exception e) {
            return ResultData.error(Msg.VALIDATE_ERROR, "500");
        }
    }



    @Override
    public ResultData<List<VaccineResponse>> getAnimalAllVaccines(String animalName) {
        try {

            List<Animal> animals = this.animalService.getAnimalByNames(animalName);

            List<VaccineResponse> vaccineResponses = new ArrayList<>();


            for (Animal animal : animals) {
                List<Vaccine> vaccines = animal.getVaccineList();
                for (Vaccine vaccine : vaccines) {
                    VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(vaccine, VaccineResponse.class);
                    vaccineResponses.add(vaccineResponse);
                }
            }


            if (vaccineResponses.isEmpty()) {
                return ResultData.error("Belirtilen hayvan adına göre veri bulunamadı.", "404");
            }


            return ResultHelper.success(vaccineResponses);
        } catch (NotFoundException ex) {

            return ResultData.error(Msg.NOT_FOUND, "404");
        }
    }



    @Override
    public  ResultData<List<VaccineResponse>> getAllVaccines() {
        List<Vaccine> vaccines = this.vaccineRepo.findAll();

        try {
            List<VaccineResponse> responseList = new ArrayList<>();
            for (Vaccine vaccine : vaccines) {
                VaccineResponse response = this.modelMapper.forResponse().map(vaccine, VaccineResponse.class);
                responseList.add(response);
            }
            return ResultHelper.success(responseList);
        } catch (Exception e) {
            return ResultData.error(Msg.VALIDATE_ERROR, "500");
        }
    }


    @Override
    public List<Vaccine> getVaccinesByDate(LocalDate protectionStartDate, LocalDate protectionEndDate) {

        return vaccineRepo.findByProtectionStartDateAfterAndProtectionEndDateBefore(protectionStartDate, protectionEndDate);
    }

    @Override
    public List<Vaccine> findByName(String name) {
        List<Vaccine> vaccines = vaccineRepo.findByNameIgnoreCase(name);
        if (vaccines.isEmpty()) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }

        return vaccines;
    }





}





