package com.haiyisec.modules.inventorymanager.domain.service.importservice;

import org.testng.annotations.DataProvider;

import java.util.UUID;


public class TestDataProvider {
    @DataProvider(name = "excelTitleVerify")
    public Object[][] verifyTitleexcelFiles() {

        return new Object[][]{
                {"title_null.xls"},
                {"title_error.xls"},
                {"title_more.xls"},
                {"title_null_error.xls"},
                {"title_mutilError.xls"}
        };

    }

    @DataProvider(name = "excelSmallDatas")
    public Object[][] smallDatas() {

        return new Object[][]{
                {"data_correct_80.xls"},
                {"data_null_cells.xls"},
                {"data_null_line.xls"}
        };

    }

    @DataProvider(name = "excelBigDatas")
    public Object[][] bigDatas() {

        return new Object[][]{
                {"data_correct_1000.xls"},
                {"data_correct_1000_2.xls"},
                {"data_correct_1000_3.xls"}
        };

    }

    @DataProvider(name = "csvTitleVerify")
    public Object[][] verifyTitleCsvFiles() {

        return new Object[][]{
                {"csv_null_utf-8-bom_titleMore.csv"},
                {"csv_null_utf-8-bom_titleNull.csv"},
                {"csv_null_utf-8-bom_titleError.csv"}
        };

    }

    @DataProvider(name = "csvCodingVerify")
    public Object[][] verifyCodingCsvFiles() {

        return new Object[][]{
                //{"csv_null_utf-8-bom.csv"},
                {"csv_oneData_ansi.csv"},
                {"csv_oneData_utf-8.csv"}
        };

    }

    @DataProvider(name = "csvSmallDatas")
    public Object[][] csvSmallDatas() {

        return new Object[][]{
                {"csv_utf-8-bom_100.csv"},
                {"csv_utf-8-bom_nullCell.csv"},
            {"csv_utf-8-bom_nullLine.csv"}
        };

    }

    @DataProvider(name = "csvBigDatas")
    public Object[][] csvBigDatas() {

        return new Object[][]{
                {"data_correct_1000.xls"}
        };

    }


    @DataProvider(name = "monopolizeTest")
    public Object[][] monopolizeTest() {
        //每次引用的登陆id都是同步一个模拟登陆？可实现？
        String loginId = String.valueOf(UUID.randomUUID());
        return new Object[][]{
                {"data_correct_1000.xls", loginId},
                {"data_correct_1001.xls", loginId},
                {"data_correct_1002.xls", loginId}
        };

    }
}
