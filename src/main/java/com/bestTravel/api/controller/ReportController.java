package com.bestTravel.api.controller;

import com.bestTravel.infrastructure.abstract_services.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author claudio.vilas
 * date. 08/2023
 */

@RestController
@RequestMapping("/reports")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Best Travel - Reportes")
public class ReportController {
    private final ReportService reportService;

    private static final MediaType FORCE_DOWNLOAD = new MediaType("application", "force-download");
    private static final String FORCE_DOWNLOAD_HEADER_VALUE = "attachment: filename=report.xlsx";
    @GetMapping
    public ResponseEntity<Resource> getReport(){
        var headers = new HttpHeaders();
        headers.setContentType(FORCE_DOWNLOAD);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, FORCE_DOWNLOAD_HEADER_VALUE);
        log.info("#### endpoint generacion de reportes excel");
        var fileInBytes = reportService.readFile();
        ByteArrayResource response = new ByteArrayResource(fileInBytes);
        return ResponseEntity.ok().headers(headers)
                .contentLength(fileInBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(response);
    }
}
