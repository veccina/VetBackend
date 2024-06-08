package dev.patika.vetManagementSystem.dao;

import dev.patika.vetManagementSystem.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long>{

}
