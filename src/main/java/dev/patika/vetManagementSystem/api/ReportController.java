package dev.patika.vetManagementSystem.api;

import dev.patika.vetManagementSystem.bussiness.abstracts.IReportService;
import dev.patika.vetManagementSystem.core.config.utilies.Msg;
import dev.patika.vetManagementSystem.core.config.utilies.ResultHelper;
import dev.patika.vetManagementSystem.core.result.ListResult;
import dev.patika.vetManagementSystem.core.result.Result;
import dev.patika.vetManagementSystem.core.result.ResultData;
import dev.patika.vetManagementSystem.dto.request.report.SaveReportRequest;
import dev.patika.vetManagementSystem.dto.request.report.UpdateReportRequest;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse;
import dev.patika.vetManagementSystem.dto.response.report.ReportResponse2;
import dev.patika.vetManagementSystem.entities.Doctor;
import dev.patika.vetManagementSystem.entities.Report;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/report")
@RequiredArgsConstructor
public class ReportController {


    private final IReportService reportService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ListResult<Report> getAllReports() {
        ListResult<Report> reportList = this.reportService.getAllReports();
        return reportList;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<ReportResponse2> save(@Valid @RequestBody SaveReportRequest saveReportRequest) {
        return this.reportService.save(saveReportRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<ReportResponse> get(@PathVariable("id") long id) {
        return this.reportService.get(id);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<ReportResponse2> update(@Valid @RequestBody UpdateReportRequest updateReportRequest) {
        return this.reportService.update(updateReportRequest);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id) {
        boolean isDeleted = this.reportService.delete(id);

        if (isDeleted) {
            return ResultHelper.ok();
        } else {
            return ResultHelper.notFoundError(Msg.NOT_FOUND);
        }
    }
}
