package com.wojtek.api.StarWarsReportGeneratorAPI.reposotories;

import com.wojtek.api.StarWarsReportGeneratorAPI.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
