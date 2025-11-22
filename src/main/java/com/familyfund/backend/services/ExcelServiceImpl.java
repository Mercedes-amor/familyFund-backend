package com.familyfund.backend.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;
import com.familyfund.backend.modelo.Transaction;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public byte[] generateFullExcel(
            List<Category> categories,
            List<Transaction> transactions,
            List<MaxiGoal> goals,
            List<MaxiGoalSaving> savings) throws IOException {

        // Crear archivo Excel
        Workbook workbook = new XSSFWorkbook();

        // ------HOJA 1: CATEGORIES------

        // Crear hoja
        Sheet catSheet = workbook.createSheet("Categories");
        Row cHeader = catSheet.createRow(0); // Fila 0: Encabezados

        // Columnas
        cHeader.createCell(0).setCellValue("ID");
        cHeader.createCell(1).setCellValue("Name");
        cHeader.createCell(2).setCellValue("Limit");
        cHeader.createCell(3).setCellValue("TotalGastos");

        int ci = 1; // índice
        for (Category c : categories) {
            // Filtramos las transacciones de la categoría y sumamos importes
            double total = transactions.stream()
                    .filter(t -> t.getCategory().getId().equals(c.getId()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            // Creamos una fila y escribimos datos
            Row row = catSheet.createRow(ci++);
            row.createCell(0).setCellValue(c.getId());
            row.createCell(1).setCellValue(c.getName());
            row.createCell(2).setCellValue(c.getLimit());
            row.createCell(3).setCellValue(total);
        }

        // ------HOJA 2: TRANSACTIONS------

        Sheet tSheet = workbook.createSheet("Transactions");
        Row tHeader = tSheet.createRow(0);

        tHeader.createCell(0).setCellValue("CategoryName");
        tHeader.createCell(1).setCellValue("ID");
        tHeader.createCell(2).setCellValue("Date");
        tHeader.createCell(3).setCellValue("Amount");
        tHeader.createCell(4).setCellValue("User");

        int ti = 1;
        for (Transaction t : transactions) {
            Row row = tSheet.createRow(ti++);
            row.createCell(0).setCellValue(t.getCategory().getName());
            row.createCell(1).setCellValue(t.getId());
            row.createCell(2).setCellValue(t.getDate().toString());
            row.createCell(3).setCellValue(t.getAmount());
            row.createCell(4).setCellValue(t.getUsuario().getNombre());
        }

        // -------HOJA 3: MAXIGOAL-------

        Sheet gSheet = workbook.createSheet("MaxiGoal");
        Row gHeader = gSheet.createRow(0);

        gHeader.createCell(0).setCellValue("ID");
        gHeader.createCell(1).setCellValue("Name");
        gHeader.createCell(2).setCellValue("Target");
        gHeader.createCell(3).setCellValue("Current");

        int gi = 1;
        for (MaxiGoal g : goals) {
            Row row = gSheet.createRow(gi++);
            row.createCell(0).setCellValue(g.getId());
            row.createCell(1).setCellValue(g.getName());
            row.createCell(2).setCellValue(g.getTargetAmount());
            row.createCell(3).setCellValue(g.getActualSave());
        }

        // -------HOJA 4: SAVINGS-------

        Sheet sSheet = workbook.createSheet("Savings");
        Row sHeader = sSheet.createRow(0);

        sHeader.createCell(0).setCellValue("ID");
        sHeader.createCell(1).setCellValue("MaxiGoalName");
        sHeader.createCell(2).setCellValue("Date");
        sHeader.createCell(3).setCellValue("Amount");
        sHeader.createCell(4).setCellValue("User");

        int si = 1;
        for (MaxiGoalSaving s : savings) {
            Row row = sSheet.createRow(si++);
            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getMaxiGoal().getName());
            row.createCell(2).setCellValue(s.getCreatedAt().toString());
            row.createCell(3).setCellValue(s.getAmount());
            row.createCell(4).setCellValue(s.getUsuario().getNombre());
        }

        // ··· OUTPUT ··· Convertir el Excel a bytes
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // Datos en memoria
        workbook.write(out); // Guardamos los datos en el ByteArrayOutputStream
        workbook.close(); // Cerramos el workbook para liberar rescursos
        return out.toByteArray(); // Lo convertimos a un array de bytes-> byte[]
    }

}
