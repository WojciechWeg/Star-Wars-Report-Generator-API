package com.wojtek.api.StarWarsReportGeneratorAPI.reposotories;

import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
