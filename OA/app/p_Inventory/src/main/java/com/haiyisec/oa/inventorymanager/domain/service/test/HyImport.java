package com.haiyisec.oa.inventorymanager.domain.service.test;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.test.test_strategy.ImportLimitCheck;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.base.domain.obj.IOutResult;
import org.zen.frame.func.threadpool.HyThreadPoolBuilder;
import org.zen.frame.func.threadpool.threadpool.HyRunnable;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;
import org.zen.frame.func.threadpool.threadpool.IThreadPoolTerminated;
import org.zen.frame.util.JsonSpringUtil;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

//@change: 最后修改：  将导入功能的所有内容放入 com.haiyisec.common.component.import

@Slf4j
@Service
public class HyImport {

    @Autowired
    private ImportMonitorService importMonitorService;
    @Autowired
    private ImportLimitCheck importLimitCheck;
    @Autowired
    private ImportFormatCheck importFormatCheck;
    //项目路径
    @Value("${server.tomcat.basedir}")
    private String basedir;

    //@change: 下面这些变量都会导致并发问题
    //private String userId = "";
    //private Map<String, Object> result = new HashMap(); //修改为内部类
    //private CheckResult cr = new CheckResult();

    private String format;

    //@change: 方法传参 及 返回 都使用 IOutResult
    public IOutResult importData(CheckResult cr,IOutResult or,ImportConfig importConfig) throws Exception {
        //@change:  or 作为方法的参数传入

        String taskId = importConfig.getTaskId();


        //@change: 下面这一段可修改为： or.addData("taskId", taskId);
        or.addData("taskId", taskId);

        //@change:  注释意思不明确
        //判断能否单模块多导入
        //@change:  后面代码修改为：
        if (importConfig.getOperationConfig()!=null) {
            if(!importLimitCheck.check(or,importConfig.getOperationConfig(),importConfig.getModel(),importConfig.getUserId())){
                return or;
            }
        }


//        if (importConfig.getOperationConfig()!=null && importMonitorService.userWorking(importConfig).isSuccess()) {
//            or.setSuccess(false);
//            or.setErrorCode(AppCommonErrorConfig.导入_限定策略_阈值_code);
//            or.setMsg(AppCommonErrorConfig.导入_限定策略_阈值_msg);
//            return or;
//        }


        //校验表头以及excel文档内部样式
        //@todo： 准备状态
//        IOutResult or_format = importFileFormatVerify(file, excelTitle);

        or = importFormatCheck.formatVerify(or,importConfig);
        if (!or.isSuccess()) {
            return or;
        }

     ImportMonitor importMonitor =   importMonitorService.prepareWork(importConfig);
//        prepareWork(importConfig);

        //@change: startCheck方法不会抛出可捕获的异常，是否需要try catch？
//        try {
            //@change: 此处的userId未被赋值
            startCheck(cr,or,importConfig, importMonitor);
//        } catch (Exception e) {
//            //@change: 此处抛出的异常 是否代表了 用户中断？
//            importMonitorService.setStatus(taskId, Status.ERROR);
//            or.setSuccess(false);
//            or.setMsg("物品导入错误");
//            //@change: 需增加 errorcode，并且 msg 应该为 e.getMessage
//            return or;
//        }
        return or;
    }

    //@change: 是否将该方法挪入 ImporMonitorService?

    //开始导入过程包含：文件数据校验；文件数据导入
    //@change: 此处不会抛出可捕获的异常
    private CheckResult startCheck(CheckResult cr,IOutResult or,ImportConfig importConfig, ImportMonitor importMonitor) throws Exception {
        //@todo: 可能不用删数据 上一次的处理过程独立为新的方法
        //@todo:创造ImportMonitor的位置待讨论

        String taskId = importConfig.getTaskId();
        MultipartFile file = importConfig.getFile();
        //初始化监控po的任务id，用户id，任务状态


        Class<?> pojoClass = importConfig.getDataEntity();

        List successDatas = new ArrayList<>();
        List failDatas = new ArrayList<>();
        //单线程的线程池进行文件数据校验过滤
        //@todo: 单线程线程池，可收集任务，进入排队
        //@todo: 修改为spring线程池该业务公用进行排序
        HyThreadPoolExecutor checkPool = new HyThreadPoolBuilder().setPoolName("数据校验线程池-").fiexedBuild(1);
        importMonitorService.setCheckPool(importMonitor, checkPool);

        File tmpFile = null;
        try {
            //文件复制
            tmpFile = new File(basedir + "/excel/" + UUID.randomUUID());
            final File newFile = tmpFile;
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);

            //@change: 最后修改  将该 HyRunnable的操作 拆到一个新的class中进行实现
            HyRunnable importVerify = new HyRunnable("000") {
                //            @SneakyThrows
                @Override
                public void run() {

                    successDatas.clear();
                    failDatas.clear();
                    //@change: 此处的failData为静态变量，是否合适？ XXX csv不会返回错误数据，思路:一边校验，一遍存放
                    //importMonitorService.clearFailData(importMonitor);

                  importConfig.getHyExcelDataVerifyHandler().failData.clear();

                    try {
                        //校验开始，初始化标志位
                        //@todo: 中断标识应该放在po内部判断 中断标志位
                        log.info("校验开始...................");

                        if (or.getData("format").equals("xls")) {

                            ImportParams params =  getExcelImportParamsByImportConfig(importConfig);
                            //@change: 独立为一个方法： getExcelImportParamsByImportConfig,是返回一个params吗？XXX

                            ExcelImportResult excelImportResult = null;

                            try (FileInputStream fis = new FileInputStream(newFile)) {
                                //二次读取
                                excelImportResult = ExcelImportUtil.importExcelMore(fis, pojoClass, params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            successDatas.addAll(excelImportResult.getList());
                            failDatas.addAll(excelImportResult.getFailList());

                        } else if (or.getData("format").equals("csv")) {

                            //@change: 独立为一个方法： getCsvImportParamsByImportConfig
                            CsvImportParams params_csv =  getCsvImportParamsByImportConfig(importConfig);

                            List result = null;
                            try (FileInputStream fis = new FileInputStream(newFile)) {
                                result = CsvImportUtil.importCsv(fis, pojoClass, params_csv);
                                //csv需要在verify过程中加入failData
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            successDatas.addAll(result);
                            failDatas.addAll(importMonitorService.getFailList(importMonitor));
                        }

                        //@change: 此处无需保存两次数据库
                        //@change: 将下面的3个操作合并成一个，传入相应的参数，最后一次保存即可
                        importMonitorService.endCheck(importMonitor,successDatas.size(),failDatas.size());

                    } finally {
                        if (newFile.exists()) {
                            newFile.delete();
                        }
                    }
                }
            };

            //@change: 最后修改  将该 IThreadPoolTerminated 的操作 拆到一个新的class中进行实现
            checkPool.setiThreadPoolTerminated(new IThreadPoolTerminated() {
                @Override
                public void terminated() {

                    importMonitorService.removeCheckPool(taskId);

                    //@change: 此处判断有疑问，不为true 则 提示 数据全部校验不通过？
                    if (!importMonitorService.isInterrupted(importMonitor) && successDatas != null && successDatas.size() > 0) {


                        if (importConfig.getIHyDataAdaption() instanceof IHyPreDateHandler) {
                            ((IHyPreDateHandler)importConfig.getIHyDataAdaption()).preDateHandler(cr, successDatas, failDatas, taskId);
                        }

                        if (importConfig.getIHyDataAdaption() instanceof IHyFailHandler) {
                            //@change: importConfig.getTaskId() 是否可以直接用 taskId?
                            ((IHyFailHandler)importConfig.getIHyDataAdaption()).failHandler(failDatas, taskId);
                        }
                    } else {
                        cr.setSuccess(false);
                        cr.getErrorMsg().append("导入失败，请检查后重新导入！");
                        importMonitorService.finished(importMonitor);
                        return;
                    }

                    //@change: 将 result 做成内部类 传递参数
                    Result result = new Result(importConfig.getUserId(),taskId,successDatas,failDatas);

                    excelImportDatas(result, importConfig,importMonitor);
                }
            });


            importMonitorService.setStatusAndSave(importMonitor, Status.CHECK);

            checkPool.submit(importVerify);

            return cr;
        } catch (Exception e) {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        } finally {
            checkPool.shutdown();
        }
        //@todo: importMonitor记录中断原因
        return null;
    }

    private CsvImportParams getCsvImportParamsByImportConfig(ImportConfig importConfig) {
        CsvImportParams params_csv = new CsvImportParams();
        //@change: 该参数默认为0，但可以在ImportConfig中设置
        //@change: 该设置在excel时没有，是否也需要加上？
        params_csv.setTitleRows(importConfig.getFileConfig().getTitleRows());
        //@change: 该参数默认为1，但可以在ImportConfig中设置
        params_csv.setHeadRows(importConfig.getFileConfig().getHeadRows());
        //该值是否应该由  importConfig.getHyExcelDataVerifyHandler() != null 决定？

        if (importConfig.getHyExcelDataHandler() != null) {
            params_csv.setDataHandler(importConfig.getHyExcelDataHandler());
        }

        if (importConfig.getHyExcelDataVerifyHandler() != null) {
            params_csv.setNeedVerify(true);
            params_csv.setVerifyHandler(importConfig.getHyExcelDataVerifyHandler());
        }

        return  params_csv;
    }

    private ImportParams getExcelImportParamsByImportConfig(ImportConfig importConfig) {
        ImportParams params = new ImportParams();
        //@change: 该参数默认为1，但可以在ImportConfig中设置
        params.setHeadRows(1);
        //该值是否应该由  importConfig.getHyExcelDataVerifyHandler() != null 决定？
        params.setNeedVerify(true);

        if (importConfig.getHyExcelDataHandler() != null) {
            params.setDataHandler(importConfig.getHyExcelDataHandler());
        }

        if (importConfig.getHyExcelDataVerifyHandler() != null) {
            params.setVerifyHandler(importConfig.getHyExcelDataVerifyHandler());
        }
        return params;
    }


    public void cancelTask(String taskId) {

        importMonitorService.interrupt(taskId);

    }

    public void excelImportDatas(Result result, ImportConfig importConfig,ImportMonitor importMonitor) {

        List success_list = result.getSuccessData(); //获取通过了check的数据
        String taskId = result.getTaskId();

        HyThreadPoolExecutor datasImportPool = new HyThreadPoolBuilder().setPoolName("数据导入线程池-").setCoreSize(1).build();

        importMonitorService.setImportPool(importMonitor, datasImportPool);

        //@change:  结果的并行处理 需要在 ImportConfig 中由开发人员指定，多少数据进行分隔，最大多少线程
        //根据200个进行切割
        int threadDataNum = importConfig.getFileConfig().getDividedNum();


        if (success_list.size() > threadDataNum) {
            int x = success_list.size() / threadDataNum;
            //开启n个线程
            //@change: 测试的时候打印一些日志，看看开启的线程数是否符合预期
            for (int y = 0; y < x + 1; y++) {
                int endNum = (threadDataNum * (y + 1)) < success_list.size() ? (threadDataNum * (y + 1)) : success_list.size();
                importDatasByThreadPool(success_list.subList(threadDataNum * y, endNum),  String.valueOf(y), datasImportPool, importConfig,importMonitor);
            }
        } else {
            //小于200开启单线程
            importDatasByThreadPool(success_list,"0", datasImportPool, importConfig,importMonitor);
        }


        //线程池关闭后续处理逻辑
        datasImportPool.setiThreadPoolTerminated(new IThreadPoolTerminated() {
            @SneakyThrows
            @Override
            public void terminated() {
                if (!importMonitorService.isInterrupted(importMonitor)) {
                    importMonitorService.finished(importMonitor);
                }
            }
        });
        //关闭线程池提交任务
        datasImportPool.shutdown();
    }

    private void importDatasByThreadPool(List success_list,  String id, HyThreadPoolExecutor datasImportPool, ImportConfig importConfig,ImportMonitor importMonitor) {

        HyRunnable taskRunnable = new HyRunnable(id) {
            @Override
            public void run() {

                if (importConfig.getIHyDataAdaption() instanceof IHySuccessHandler) {
                    ((IHySuccessHandler)importConfig.getIHyDataAdaption()).successHandler(success_list, importConfig.getTaskId());
                }

            }
        };

        try {

            importMonitorService.setStatusAndSave(importMonitor, Status.WORK);//执行导入runnable
            datasImportPool.submit(taskRunnable);

        } catch (RejectedExecutionException e) {
            log.info("第： " + id + " 个任务提交被拒绝，超过线程池容量。");
        }
    }

    //@change: 这个是SSE 请求，每隔几秒就会请求一次，是否考虑了效率问题？
    public String getProgressRateResult(String taskId) {
        String str = "";
        //@change: 改成判断 null ？
        if (taskId.equals("1")) {
            ImportMonitor newImportMonitor = new ImportMonitor();
            newImportMonitor.setTaskStatus(Status.NONE);
            str = JsonSpringUtil.obj2Json(newImportMonitor);
            str = "data: " + str + "\n\n";
            return str;
        }
        //数据库实例
        ImportMonitor importMonitor;
        //缓存
        ImportMonitor cacheMonitor;


        DecimalFormat df = new DecimalFormat("0");


        cacheMonitor = importMonitorService.getImportMonitor(taskId);

        importMonitor = importMonitorService.queryOneByTaskId(taskId);

        ConcurrentHashMap l = importMonitorService.getCacheList();

        if (cacheMonitor != null) {
            //存在正在导入任务
            float progress;
            if (cacheMonitor.getTotalNum() == 0) {
                progress = 0;
            } else {
                progress = (float) (cacheMonitor.getCheckFailNum() + cacheMonitor.getFailNumRealTime().get() + cacheMonitor.getSuccessNumRealTime().get()) / cacheMonitor.getTotalNum() * 100;
            }

            cacheMonitor.setProgressRate(df.format(progress));

            cacheMonitor.setImportFailNum(cacheMonitor.getFailNumRealTime().get());
            cacheMonitor.setSuccessNum(cacheMonitor.getSuccessNumRealTime().get());
            cacheMonitor.setProgressRate(df.format(progress));
            //sse每访问一次进行一次成功/失败数的更新
            importMonitorService.save(cacheMonitor);

            str = JsonSpringUtil.obj2Json(cacheMonitor);
        } else if (importMonitor != null) {
            //关机-重启  展示上次导入情况
            importMonitor.getSuccessNumRealTime().getAndAdd(importMonitor.getSuccessNum());
            importMonitor.getFailNumRealTime().getAndAdd(importMonitor.getImportFailNum());
            float progress = (float) (importMonitor.getCheckFailNum() + importMonitor.getFailNumRealTime().get() + importMonitor.getSuccessNumRealTime().get()) / importMonitor.getTotalNum() * 100;

            importMonitor.setProgressRate(df.format(progress));
            //@change: 需要将 前端不需要的字段 加上 @JsonIgnore
            str = JsonSpringUtil.obj2Json(importMonitor);
        }

        str = "data: " + str + "\n\n";
        return str;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Result{

        private String userId;
        private String taskId;
        private List successData;
        private List failData;

        public Result(String userId, String taskId, List successDatas, List failDatas) {
            this.userId = userId;
            this.taskId = taskId;
            this.successData = successDatas;
            this.failData = failDatas;
        }
    }
//    //下载错误文件
//    public void downloadErrorDatas(HttpServletResponse response, String taskId) throws Exception {
//
//        //获取旧的导入批次
//        List<CommodityFail> errorDatas = commodityFailService.getErrorDatas(taskId);
//
//
//        ExcelUtil.downExcel(response, "errorDatas", errorDatas, CommodityFail.class);
//    }
}
