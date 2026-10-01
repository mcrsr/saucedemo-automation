package com.saucedemo.utils;

import com.saucedemo.models.LoginData;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class LoginDataProvider {

    private static final String DEFAULT_FILE =
            "src/test/resources/testdata/LoginData.xlsx";
    private static final String DEFAULT_SHEET = "Login";

    @DataProvider(name = "loginData", parallel = false)
    public static Object[][] getLoginData() {

        // Priority: -Dexcel.path=... > environment variable EXCEL_PATH > default
        String filePath = System.getProperty("excel.path");
        if (filePath == null || filePath.isBlank()) {
            filePath = System.getenv("EXCEL_PATH");
        }
        if (filePath == null || filePath.isBlank()) {
            filePath = DEFAULT_FILE;
        }

        String sheetName = System.getProperty("excel.sheet", DEFAULT_SHEET);

        System.out.println(">>> Reading Excel from: " + filePath
                + " [sheet=" + sheetName + "]");

        ExcelUtils excel = new ExcelUtils(filePath, sheetName);
        int rows = excel.getRowCount();

        List<Object[]> data = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            String user     = excel.getCellData(i, "Username");
            String pass     = excel.getCellData(i, "Password");
            String expected = excel.getCellData(i, "ExpectedResult");

            if (user.isEmpty() && pass.isEmpty()) continue;

            data.add(new Object[]{ new LoginData(user, pass, expected) });
        }
        excel.close();

        return data.toArray(new Object[0][]);
    }
}