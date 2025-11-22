package com.familyfund.backend.services;

import java.io.IOException;
import java.util.List;

import com.familyfund.backend.modelo.Category;
import com.familyfund.backend.modelo.MaxiGoal;
import com.familyfund.backend.modelo.MaxiGoalSaving;
import com.familyfund.backend.modelo.Transaction;

public interface ExcelService {

    public byte[] generateFullExcel(
            List<Category> categories,
            List<Transaction> transactions,
            List<MaxiGoal> goals,
            List<MaxiGoalSaving> savings)
            throws IOException;
}
