package com.example.tofi.common.util.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public abstract class AbstractExcelReport<T> {
    protected SXSSFWorkbook wb;
    protected SXSSFSheet sh;

    private void fillHeader(List<String> columns) {
        wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
        sh = wb.createSheet("Report");
        sh.trackAllColumnsForAutoSizing();
        sh.setHorizontallyCenter(true);
        for (int rownum = 0; rownum < 1; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < columns.size(); cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(columns.get(cellnum));
                cell.setCellStyle(getStyle());
                sh.autoSizeColumn(cellnum);
            }
        }
    }

    public abstract void fillData(List<T> list);

    public final SXSSFWorkbook exportExcel(List<String> columns, List<T> dataList) {
        fillHeader(columns);
        fillData(dataList);
        return wb;
    }

    protected CellStyle getStyle() {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = wb.createFont();
        font.setColor(IndexedColors.BLUE.index);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
