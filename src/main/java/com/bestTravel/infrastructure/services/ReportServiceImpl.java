package com.bestTravel.infrastructure.services;

import com.bestTravel.domain.entity.CustomerEntity;
import com.bestTravel.domain.repository.CustomerRepository;
import com.bestTravel.infrastructure.abstract_services.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final CustomerRepository customerRepository;

    private static final String SHEET_NAME = "Customer total sales";
    private static final String FONT_TYPE = "Arial";
    private static final String COLUMN_CUSTOMER_ID = "id";
    private static final String COLUMN_CUSTOMER_NAME = "name";
    private static final String COLUMN_CUSTOMER_PURCHASES = "purchases";
    private static final String REPORTS_PATHS_WITH_NAME = "report/sales-%s";
    private static final String REPORTS_PATH = "report";
    private static final String FILE_TYPE = ".xlsx";
    private static final String FILE_NAME = "sales-%s.xlsx";

    @Override
    public byte[] readFile() {
        try {
            this.createReport();
            var path = Paths.get(REPORTS_PATH, String.format(FILE_NAME,
                    LocalDate.now().getMonth())).toAbsolutePath();
            return Files.readAllBytes(path);
        }catch (IOException e){
            log.error("error leyendo archivo exel");
            throw new RuntimeException();
        }
    }

    private void createReport(){
        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet(SHEET_NAME);
        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 3000);

        var header = sheet.createRow(0);
        var headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.VIOLET.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        var font = workbook.createFont();
        font.setFontName(FONT_TYPE);
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        headerStyle.setFont(font);

        var headerCell = header.createCell(0);
        headerCell.setCellValue(COLUMN_CUSTOMER_ID);
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue(COLUMN_CUSTOMER_NAME);
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue(COLUMN_CUSTOMER_PURCHASES);
        headerCell.setCellStyle(headerStyle);
        var style = workbook.createCellStyle();
        style.setWrapText(true);

        var customers = customerRepository.findAll();
        var rowPos = 1;

        for (CustomerEntity customer: customers){
            var row = sheet.createRow(rowPos);
            var cel = row.createCell(0);
            cel.setCellValue(customer.getDni());
            cel.setCellStyle(style);

            cel = row.createCell(1);
            cel.setCellValue(customer.getFullName());
            cel.setCellStyle(style);

            cel = row.createCell(2);
            cel.setCellValue(totalPurchase(customer));
            cel.setCellStyle(style);

            rowPos++;
        }

        var report = new File(String.format(REPORTS_PATHS_WITH_NAME,LocalDate.now().getMonth()));
        var path = report.getAbsolutePath();
        var fileLocation = path + FILE_TYPE;

        try(var outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
            workbook.close();
        }catch (IOException e){
            log.error("cant create excel {}", e.getMessage());
            throw new RuntimeException();
        }
    }

    private static Integer totalPurchase(CustomerEntity entity){
        return entity.getTotalFlights() + entity.getTotalLodgings() + entity.getTotalTours();
    }
}
