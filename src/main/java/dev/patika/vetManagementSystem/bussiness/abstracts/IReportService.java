package dev.patika.vetManagementSystem.bussiness.abstracts;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dto.request.report.SaveReportRequest;
import dev.patika.vetManagementSystem.dto.request.report.UpdateReportRequest;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse2;
import dev.patika.vetManagementSystem.entities.Report;

public interface IReportService {

    ResultData<ReportResponse2> save(SaveReportRequest saveReportRequest);

    ResultData<ReportResponse> get(long id);

    ResultData<ReportResponse2> update(UpdateReportRequest updateReportRequest);

    Report getById(long id);

    boolean delete (long id);

    ListResult<Report> getAllReports();




}
