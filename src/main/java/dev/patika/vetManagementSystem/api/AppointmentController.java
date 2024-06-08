package dev.patika.vetManagementSystem.api;


import dev.patika.vetManagementSystem.bussiness.abstracts.IAppointmentService;

import dev.patika.vetManagementSystem.bussiness.concrets.AppointmentManager;

import dev.patika.vetManagementSystem.core.config.utilies.Msg;
import dev.patika.vetManagementSystem.core.config.utilies.ResultHelper;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.Result;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dto.request.appointment.SaveAppointmentRequest;
import dev.patika.vetManagementSystem.dto.request.appointment.UpdateAppointmentRequest;
import dev.patika.vetManagementSystem.dto.response.appointment.AppointmentResponse2;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private final IAppointmentService appointmentService;
    private final AppointmentManager appointmentManager;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse2>> getAllAppointments() {
        return appointmentManager.getAllAppointments();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED) //Ekleme
    public ResultData<AppointmentResponse2> save2(@Valid @RequestBody SaveAppointmentRequest saveAppointmentRequest) {
        return appointmentManager.save2(saveAppointmentRequest);
    }




    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) //Id'ye göre getirme
    public ResultData<AppointmentResponse2> get(@PathVariable("id") long id) {
        return appointmentService.get2(id);
    }


    @DeleteMapping("/{id}") //Silme
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id) {
        boolean isDeleted = this.appointmentService.delete(id);

        if (isDeleted) {
            return ResultHelper.ok();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }



    @GetMapping("/filter") //hayvan adı ve tarih aralığına göre getirme
    public ListResult<AppointmentResponse2> getAppointmentsInDateRange(
            @RequestParam("animalName") String animalName,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<AppointmentResponse2> appointments = appointmentManager.getAppointmentsByAnimalNameAndDateRange(animalName, startDate, endDate);
        if (appointments.isEmpty()) {
            return ResultHelper.notFoundErrorList(Msg.NOT_FOUND);
        }
        return ResultHelper.successList(appointments);
    }


    @GetMapping("/filtered") //doktor adı ve tarih aralığına göre getirme
    public ListResult<AppointmentResponse2> getAppointmentsInDateRangeAndDoctorName(
            @RequestParam("doctorName") String doctorName,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        List<AppointmentResponse2> appointments = appointmentManager.getAppointmentsByDoctorNameAndDateRange(doctorName, startDate, endDate);
        if (appointments.isEmpty()) {
            return ResultHelper.notFoundErrorList(Msg.NOT_FOUND);
        }
        return ResultHelper.successList(appointments);
    }




    @PutMapping() //Güncelleme
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse2> update2(@Valid @RequestBody UpdateAppointmentRequest updateAppointmentRequest){
        return appointmentService.update2(updateAppointmentRequest);
    }



}



