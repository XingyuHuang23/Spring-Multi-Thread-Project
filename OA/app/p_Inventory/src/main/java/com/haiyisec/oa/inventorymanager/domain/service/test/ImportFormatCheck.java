package com.haiyisec.oa.inventorymanager.domain.service.test;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;
import org.zen.frame.base.domain.obj.IOutResult;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ImportFormatCheck {

    public IOutResult formatVerify(IOutResult or,ImportConfig importConfig) throws Exception {

        //@change:  or 作为方法的参数传入
        MultipartFile file = importConfig.getFile();

        //@change: 将下面的 xls 和 csv的处理 拆分两个方法独立出去
        if (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")) {

          return  xlsCheck(or,importConfig);

        } else if (file.getOriginalFilename().endsWith(".csv")) {

          return   csvCheck(or,importConfig);

        } else {
            or.setSuccess(false);
            or.setErrorCode(AppCommonErrorConfig.导入_文件格式错误_code);
            or.setMsg(AppCommonErrorConfig.导入_文件格式错误_msg);
            return or;

        }

    }

    private IOutResult csvCheck(IOutResult or, ImportConfig importConfig) throws Exception{
        //@change: multipartFileToFile方法 以及 后面的编码判断、获取CSV的表头 是否作为 工具类方法提供？
        //@change: 是否可以直接用 file.getInputStream() 获取 InputStream？ multipartFileToFile方法是否还需要？
        //编码判断
        File file_csv = multipartFileToFile(importConfig.getFile());
        List excelTitle =Arrays.asList( importConfig.getTitles());
        InputStream ios = new FileInputStream(file_csv);
        byte[] b = new byte[3];
        ios.read(b);
        ios.close(); //xxx 要放在final中，不然会有可能报错

        if (!(b[0] == -17 && b[1] == -69 && b[2] == -65)) {
            or.setSuccess(false);
            or.setErrorCode(AppCommonErrorConfig.导入_文件编码错误_code);
            or.setMsg(AppCommonErrorConfig.导入_文件编码错误_msg);

            return or;
        }

        //使用原生获取表头
        //@change: 读取文件可以使用这种方法：
//            try (BufferedReader br = new BufferedReader(new FileReader(file_csv))) {
//                ...
//            }

//        BufferedReader br = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file_csv))){
//            FileReader f = new FileReader(file_csv);
//            br = new BufferedReader(f);

            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line;
            if ((line = br.readLine()) != null) {

                String[] titleSet = line.split(DELIMITER);


                if (titleSet.length > 5) {

                    or.setSuccess(false);
                    or.setErrorCode(AppCommonErrorConfig.导入_表头错误_code);
                    or.setMsg(AppCommonErrorConfig.导入_表头错误_msg);

                    return or;
                }
                for (String titleName : titleSet) {
                    titleName = titleName.replaceAll("\\p{C}", "");
                    if (!excelTitle.contains(titleName)) {
                        or.setSuccess(false);
                        or.setErrorCode(AppCommonErrorConfig.导入_表头错误_code);
                        or.setMsg(AppCommonErrorConfig.导入_表头错误_msg);
                        return or;
                    }
                }
            }
        }
        or.addData("format","csv");
        return or;
    }


    public IOutResult xlsCheck(IOutResult or,ImportConfig importConfig) throws Exception{
        ImportParams params = new ImportParams();
        List excelTitle =Arrays.asList( importConfig.getTitles());

        //读取一行，先判断excel表头是否符合规定
        //@todo: 提供表头跟sheet的判断方法
        params.setReadRows(1);
        //@change: 这个是否还需要？
        params.setNeedSave(true);

        //@todo:  是否还需要指定读取哪个 sheet? 比如：params.setSheetNum(); params.setStartSheetIndex();

        //一次读取,   这里就进行了一次读取，而仅仅只是为了校验表头，觉得不合理
        //@change: 测试下该步骤处理时间为多长 XXX

        //@change: 捕获非excle 或 csv 的处理异常
        ExcelImportResult excelImportResult = ExcelImportUtil.importExcelMore(importConfig.getFile().getInputStream(), Map.class, params);

        //判断sheet
        Sheet sheet = excelImportResult.getWorkbook().getSheetAt(0);
        //@change:  这个判断的用处：excelImportResult.getWorkbook().getNumberOfSheets() > 1
        if (excelImportResult.getWorkbook().getNumberOfSheets() > 1 || sheet == null) {

            or.setSuccess(false);
            or.setErrorCode(AppCommonErrorConfig.导入_文件格式错误_code);
            or.setMsg(AppCommonErrorConfig.导入_文件格式错误_msg);

            return or;
        }
        //判断表头数量
        List<Map> list = excelImportResult.getList();
        Set<String> titleSet = list.get(0).keySet();

        if (titleSet.size() != excelTitle.size()) {

            or.setSuccess(false);
            or.setErrorCode(AppCommonErrorConfig.导入_表头错误_code);
            or.setMsg(AppCommonErrorConfig.导入_表头错误_msg);

            return or;
        }
        //判断表头详情
        for (String titleName : titleSet) {
            if (!excelTitle.contains(titleName)) {
                or.setSuccess(false);
                or.setErrorCode(AppCommonErrorConfig.导入_表头错误_code);
                or.setMsg(AppCommonErrorConfig.导入_表头错误_msg);

                return or;
            }
        }
        or.addData("format","xls");

        return or;
    }


    //下面两个方法是Mutipart转File
    //@change: 判断 multipartFileToFile方法 是否需要？ 若需要，则做成工具类
    public File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }


    private void inputStreamToFile(InputStream ins, File file) throws Exception {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } finally {

        }
    }
}
