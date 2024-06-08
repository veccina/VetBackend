package dev.patika.vetManagementSystem.bussiness.concrets;

import dev.patika.vetManagementSystem.bussiness.abstracts.IAppointmentService;
import dev.patika.vetManagementSystem.bussiness.abstracts.IReportService;
import dev.patika.vetManagementSystem.bussiness.abstracts.IVaccineService;
import dev.patika.vetManagementSystem.core.config.modelmapper.IModelMapperService;
import dev.patika.vetManagementSystem.core.config.utilies.Msg;
import dev.patika.vetManagementSystem.core.config.utilies.ResultHelper;
import dev.patika.vetManagementSystem.core.exception.NotFoundException;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dao.ReportRepo;
import dev.patika.vetManagementSystem.dto.request.report.SaveReportRequest;
import dev.patika.vetManagementSystem.dto.request.report.UpdateReportRequest;
import dev.patika.vetManagementSystem.dto.response.availableDateResponse.AvailableDateResponse2;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse2;
import dev.patika.vetManagementSystem.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportManager implements IReportService {


    private final ReportRepo reportRepo;


    private final IModelMapperService modelMapper;


    private final IAppointmentService appointmentService;




    @Override
    public ResultData<ReportResponse2> save(SaveReportRequest saveReportRequest) {
        try {
            Report report = this.modelMapper.forRequest().map(saveReportRequest, Report.class);
            Appointment appointment = this.appointmentService.get(saveReportRequest.getAppointment().getId());
            if (appointment.getReport() != null) {
                return ResultData.error("Bu randevuya ait zaten bir rapor bulunmaktadır.", "400");
            }

            report.setAppointment(appointment);
            Report savedReport = this.reportRepo.save(report);
            ReportResponse2 reportResponse = this.modelMapper.forResponse().map(savedReport, ReportResponse2.class);
            return ResultHelper.created(reportResponse);
        } catch (Exception e) {
            return ResultData.error(Msg.VALIDATE_ERROR, "500");
        }
    }


    @Override
    public ResultData<ReportResponse> get(long id) {
        try {
            Report report = this.reportRepo.findById(id).orElseThrow();
            ReportResponse reportResponse = this.modelMapper.forResponse().map(report, ReportResponse.class);
            return ResultHelper.success(reportResponse);

        }catch (Exception e) {
            return ResultData.error(Msg.NOT_FOUND, "500");

        }
    }

    @Override
    public ResultData<ReportResponse2> update(UpdateReportRequest updateReportRequest) {
        Long reportId = updateReportRequest.getId();
        try {
            Report updatedReport = this.reportRepo.findById(reportId).orElseThrow();

            Long newAppointmentId = updateReportRequest.getAppointment().getId();
            Long existingAppointmentId = updatedReport.getAppointment().getId();


            if (newAppointmentId.equals(existingAppointmentId) || updatedReport.getAppointment().getReport() == null) {
                Appointment appointment = this.appointmentService.get(newAppointmentId);
                updatedReport.setAppointment(appointment);

                updatedReport.setTitle(updateReportRequest.getTitle());
                updatedReport.setDiagnosis(updateReportRequest.getDiagnosis());
                updatedReport.setPrice(updateReportRequest.getPrice());

                this.reportRepo.save(updatedReport);

                ReportResponse2 reportResponse = this.modelMapper.forResponse().map(updatedReport, ReportResponse2.class);
                return ResultHelper.success(reportResponse);
            } else {
                return ResultData.error("Bu randevuya ait zaten bir rapor bulunmaktadır.", "400");
            }
        } catch (Exception e) {
            return ResultData.error(Msg.NOT_FOUND, "404");
        }
    }



    @Override
    public Report getById(long id) {
        return this.reportRepo.findById(id).orElseThrow(() ->  new NotFoundException(Msg.NOT_FOUND));
    }


    @Override
    public boolean delete(long id) {
        try {

                Report report = this.getById(id);
            this.reportRepo.delete(report);
            return true;
        } catch (NotFoundException ex) {
            return false;
        }
    }

    @Override
    public ListResult<Report> getAllReports() {
        List<Report> reports = this.reportRepo.findAll();

        if (reports.isEmpty()) {
            return ResultHelper.notFoundErrorList(Msg.NOT_FOUND);
        }

        return ResultHelper.successList(reports);
    }


}
