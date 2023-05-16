package com.example.demo.Util;

import com.example.demo.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    public static List<User> excelUser(InputStream inputStream) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<User> UserList = new ArrayList<User>();
            DataFormatter formatter = new DataFormatter();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                User User = new User();
                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 0:
                            User.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            User.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            User.setEmail(currentCell.getStringCellValue());
                            break;


                        default:
                            break;
                    }
                    cellIndex++;
                }
                UserList.add(User);
            }
            workbook.close();
            return UserList;

        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel File" + e.getMessage());
        }
    }

}
