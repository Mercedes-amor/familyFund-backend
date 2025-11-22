package com.familyfund.backend.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;
import com.familyfund.backend.modelo.Transaction;
import com.familyfund.backend.modelo.Usuario;

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

        // Estilos para encabezados

        // Fondo con color
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex()); // Fuente blanca sobre fondo azul
        headerStyle.setFont(font);

        headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 166, 196), null));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ------HOJA 1: CATEGORIES------

        // Crear hoja
        Sheet catSheet = workbook.createSheet("Categories");
        Row cHeader = catSheet.createRow(0); // Fila 0: Encabezados

        // Creamos cabeceras aplicando estilo a la cabecera de Categories
        String[] headers = { "ID", "Nombre", "Límite", "Total Gastos" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = cHeader.createCell(i); // Creamos la celda
            cell.setCellValue(headers[i]); // Insertamos valor
            cell.setCellStyle(headerStyle); // aplicamos estilo
        }

        int ci = 1; // índice
        for (Category c : categories) {
            // Filtramos las transacciones de la categoría y sumamos importes
            double total = transactions.stream()
                    .filter(t -> t.getCategory().getId().equals(c.getId()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            // Creamos una fila y escribimos datos
            Row row = catSheet.createRow(ci++);

            // Variables para datos que puedan ser nulos
            Double limit = c.getLimit();

            row.createCell(0).setCellValue(c.getId());
            row.createCell(1).setCellValue(c.getName());
            row.createCell(2).setCellValue(limit != null ? limit : 0.0);
            row.createCell(3).setCellValue(total);
        }

        // Ajustar ancho de columnas de Categories
        for (int i = 0; i < headers.length; i++) {
            catSheet.autoSizeColumn(i);
            catSheet.setColumnWidth(i, catSheet.getColumnWidth(i) + 1000); // un extra para que no quede tan justo
        }

        // ------HOJA 2: TRANSACTIONS------
        Sheet tSheet = workbook.createSheet("Transacciones");

        // Cabecera
        Row tHeader = tSheet.createRow(0);
        String[] tHeaders = { "Categoría", "ID", "Fecha registro", "Cantidad", "Usuario" };

        for (int i = 0; i < tHeaders.length; i++) {
            Cell cell = tHeader.createCell(i); // Creamos la celda
            cell.setCellValue(tHeaders[i]); // Insertamos valor
            cell.setCellStyle(headerStyle); // aplicamos estilo
        }

        // Filas de datos
        int ti = 1;
        for (Transaction t : transactions) {
            Row row = tSheet.createRow(ti++);
            row.createCell(0).setCellValue(t.getCategory().getName());
            row.createCell(1).setCellValue(t.getId());
            row.createCell(2).setCellValue(t.getDate().toString());
            row.createCell(3).setCellValue(t.getAmount());
            row.createCell(4).setCellValue(t.getUsuario().getNombre());
        }

        // Ajustar ancho de columnas de Transactions
        for (int i = 0; i < tHeaders.length; i++) {
            tSheet.autoSizeColumn(i);
            tSheet.setColumnWidth(i, tSheet.getColumnWidth(i) + 1000);
        }

        // -------HOJA 3: MAXIGOAL-------
        Sheet gSheet = workbook.createSheet("Objetivo Ahorro");

        // Cabecera
        Row gHeader = gSheet.createRow(0);
        String[] gHeaders = { "ID", "Nombre", "Objetivo", "Cantidad actual" };

        for (int i = 0; i < gHeaders.length; i++) {
            Cell cell = gHeader.createCell(i); // Creamos la celda
            cell.setCellValue(gHeaders[i]); // Insertamos valor
            cell.setCellStyle(headerStyle); // aplicamos estilo
        }

        int gi = 1;
        for (MaxiGoal g : goals) {
            Row row = gSheet.createRow(gi++);
            row.createCell(0).setCellValue(g.getId());
            row.createCell(1).setCellValue(g.getName());
            row.createCell(2).setCellValue(g.getTargetAmount());
            row.createCell(3).setCellValue(g.getActualSave());
        }
        // Ajustar ancho de columnas de MaxiGoal
        for (int i = 0; i < gHeaders.length; i++) {
            gSheet.autoSizeColumn(i);
            gSheet.setColumnWidth(i, gSheet.getColumnWidth(i) + 1000);
        }

        // -------HOJA 4: SAVINGS-------
        Sheet sSheet = workbook.createSheet("Savings");

        // Cabecera
        Row sHeader = sSheet.createRow(0);
        String[] sHeaders = { "ID", "Objetivo", "Fecha", "Importe", "Usuario" };

        for (int i = 0; i < sHeaders.length; i++) {
            Cell cell = sHeader.createCell(i); // Creamos la celda
            cell.setCellValue(sHeaders[i]); // Insertamos valor
            cell.setCellStyle(headerStyle); // aplicamos estilo
        }

        int si = 1;
        for (MaxiGoalSaving s : savings) {
            Row row = sSheet.createRow(si++);

            // Variables para datos que puedan ser nulos
            Usuario user = s.getUsuario();

            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getMaxiGoal().getName());
            row.createCell(2).setCellValue(s.getCreatedAt().toString());
            row.createCell(3).setCellValue(s.getAmount());
            row.createCell(4).setCellValue(user != null ? user.getNombre() : "Sistema");
        }

        // Ajustar ancho de columnas de Savings
        for (int i = 0; i < sHeaders.length; i++) {
            sSheet.autoSizeColumn(i);
            sSheet.setColumnWidth(i, sSheet.getColumnWidth(i) + 1000);
        }

        // ··· OUTPUT ··· Convertir el Excel a bytes
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // Datos en memoria
        workbook.write(out); // Guardamos los datos en el ByteArrayOutputStream
        workbook.close(); // Cerramos el workbook para liberar rescursos
        return out.toByteArray(); // Lo convertimos a un array de bytes-> byte[]
    }

}
