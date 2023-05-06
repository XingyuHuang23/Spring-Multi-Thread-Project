package com.haiyisec.oa.inventorymanager.domain.service.importservice;

import app.frame.exception.AppException;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.haiyisec.oa.inventorymanager._init.AppGlobal;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.CommodityFail;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.ImportMonitor;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.HyExcelModel;
import com.haiyisec.oa.inventorymanager.domain.service.CommodityService;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataHandler;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataVerifyHandler;
import com.haiyisec.oa.inventorymanager.domain.service.test.Status;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zen.frame.base.domain.obj.CheckResult;
import org.zen.frame.func.excel.ExcelUtil;
import org.zen.frame.func.threadpool.HyThreadPoolBuilder;
import org.zen.frame.func.threadpool.threadpool.HyRunnable;
import org.zen.frame.func.threadpool.threadpool.HyThreadPoolExecutor;
import org.zen.frame.func.threadpool.threadpool.IThreadPoolTerminated;
import org.zen.frame.global.ErrorConfig;
import org.zen.frame.util.JsonSpringUtil;
import org.zen.frame.vendor.spring.springmvc.service.ICurUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
public class ImportService {
    /*
     * @todo: 排队线程池
     *   @todo:public  ConcurrentHashMap<String, Map> xxx = new ConcurrentHashMap<>();
     * ImportConfig：：    ImportConfig
     *导入接收的vo.class
     *
     *   校验线程池
     * 导入线程池
     * */

    @Autowired
    private ImportMonitorService importMonitorService;
    @Autowired
    private ICurUserService curUserService;
    @Autowired
    private CommodityFailService commodityFailService;
    @Autowired
    private CommodityService commodityService;

    private CommodityExcelDataHandler commodityExcelDataHandler = new CommodityExcelDataHandler();

    //缓存
    //private

    //只用作导入中的时候用
    private ImportMonitor importMonitor;
    private boolean verifyCancelFlag = false;

    private String loginuserId = "";

    //数据校验线程池
    private HyThreadPoolExecutor importVerifyAndDataHandlePool = null;
    //数据导入线程池
    private HyThreadPoolExecutor datasImportPool = null;

    //项目路径
    @Value("${server.tomcat.basedir}")
    private String basedir;


    public String importDatas(CheckResult cr, MultipartFile file, List<String> excelTitle) {
//        MultipartFile file = _file;
        //@todo: 待修改，命名规范, 查询条件可能有很多 / 根据 importType + loginuserId 去查某个导入任务
        //@todo: 旧数据处理可当新方法提供
        log.info("导入开始...................");
        loginuserId = curUserService.getLoginUserId();
        //获取旧的导入批次
        ImportMonitor oldImportMonitor = importMonitorService.queryOneByAccountId(loginuserId);
        //获取已存在的导入进度
        ImportMonitor currentProgressRate = null;
        if (oldImportMonitor != null) {
            currentProgressRate = AppGlobal.IMPORTRATECACHE.get(oldImportMonitor.getId());
        }
        //@todo: csv/excel 导入类型判断
        boolean importStatusFlag = importStatusVerify(cr, oldImportMonitor, currentProgressRate);
        //判断是否存在正在导入内容
        if (!importStatusFlag) {
            return null;
        }
        //校验表头以及excel文档内部样式
        //@todo： 准备状态
        if (!importFileFormatVerify(cr, file, excelTitle)) {
            return null;
        }

        ImportMonitor startimportMonitor = null;
        try {
            startimportMonitor = startImport(cr, file, oldImportMonitor);
        } catch (Exception e) {
            importMonitor.setTaskStatus(Status.INTERRUPT);
            importMonitorService.save(importMonitor);
        }

        return startimportMonitor.getId();
    }

    //当前导入状态校验
    private boolean importStatusVerify(CheckResult cr, ImportMonitor oldImportMonitor, ImportMonitor currentProgressRateVO) {
        //跳转页面进来，新导入进来
        //@todo： 判断正在导入的状态不严谨，应该先判断缓存
        if (oldImportMonitor != null && oldImportMonitor.getTaskStatus().equals("WORKINNG") && currentProgressRateVO != null) {
            //真的working,不能导入了
            cr.getErrorMsg().append("当前用户存在正在导入的任务，请稍后再试。");
            cr.setSuccess(false);
            return false;
        }
        //@todo 异常结束的话，数据库标志位要修改
        //@todo 有没有一种情况是缓存有记录，数据库没记录
        return true;
    }

    //导入文件格式校验
    //@todo: 提供外部校验方法 只提供单行表头校验
    //@todo: csv的话有很多编码的校验
    //@todo: csv能不能只读第一行
    private boolean importFileFormatVerify(CheckResult cr, MultipartFile file, List<String> excelTitle) {
        if (!(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
            cr.getErrorMsg().append("文件格式错误，请上传正确的excel文档。");
            cr.setSuccess(false);
            return false;
        } else {
            ImportParams params = new ImportParams();
            //读取一行，先判断excel表头是否符合规定
            //@todo: 提供表头跟sheet的判断方法
            params.setReadRows(1);
            params.setNeedSave(true);
            ExcelImportResult excelImportResult = null;
            try {
                //一次读取,   这里就进行了一次读取，而仅仅只是为了校验表头，觉得不合理
                excelImportResult = ExcelImportUtil.importExcelMore(file.getInputStream(), Map.class, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //判断sheet
            Sheet sheet = excelImportResult.getWorkbook().getSheetAt(0);
            if (excelImportResult.getWorkbook().getNumberOfSheets() > 1 || sheet == null) {
                cr.setSuccess(false);
                cr.getErrorMsg().append("导入文档有误，重新选择符合导入模板的excel文件！");
                return false;
            }
            //判断表头数量
            List<Map> list = excelImportResult.getList();
            Set<String> titleSet = list.get(0).keySet();
            if (titleSet.size() > 5) {
                cr.setSuccess(false);
                cr.getErrorMsg().append("导入文档表头多于导入模板，重新选择导入文件！");
                return false;
            }
            //判断表头详情
            for (String titleName : titleSet) {
                if (!excelTitle.contains(titleName)) {
                    cr.setSuccess(false);
                    cr.getErrorMsg().append("导入文档表头与导入模板不符，重新选择导入文件！");
                    return false;
                }
            }
        }

        return true;
    }

    //开始导入过程包含：文件数据校验；文件数据导入
    private ImportMonitor startImport(CheckResult cr, MultipartFile file, ImportMonitor oldImportMonitor) {
        //删除上一次导入的错误数据表的相关数据
        //@todo: 可能不用删数据 上一次的处理过程独立为新的方法
        if (oldImportMonitor != null) {
            commodityFailService.deleteByImportMonitorId(oldImportMonitor.getId());
            //进度监控数据
            AppGlobal.IMPORTRATECACHE.remove(oldImportMonitor.getId());
            importMonitorService.delOldImportMonitor(oldImportMonitor);
        }

        //初始化监控po的任务id，用户id，任务状态
        importMonitor = new ImportMonitor();
        importMonitor.setUserId(loginuserId);
        importMonitor.setTaskStatus(Status.WORK);
        importMonitor.setStartTime(new DateTime());
        //hxy:这里看看需不需要变成多条记录的导入
        importMonitorService.save(importMonitor);
        log.info("保存导入状态...................");
        AppGlobal.IMPORTRATECACHE.put(importMonitor.getId(), importMonitor);  //缓存monitor初始化


        List<CommodityExcelVerifyVO> successDatas = new ArrayList<>();
        List<CommodityExcelVerifyVO> failDatas = new ArrayList<>();
        //单线程的线程池进行文件数据校验过滤
        //@todo: 单线程线程池，可收集任务，进入排队
        //@todo: 修改为spring线程池该业务公用进行排序
        importVerifyAndDataHandlePool = new HyThreadPoolBuilder().setPoolName("数据校验线程池-").fiexedBuild(1);
        File tmpFile = null;
        try {
            //文件复制
            tmpFile = new File(basedir + "/excel/" + UUID.randomUUID());
            final File newFile = tmpFile;
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
            HyRunnable importVerify = new HyRunnable("000") {
                //            @SneakyThrows
                @Override
                public void run() {
                    try {
                        //校验开始，初始化标志位
                        //@todo: 中断标识应该放在po内部判断 中断标志位
                        log.info("校验开始...................");
                        verifyCancelFlag = false;

                        //@todo: 业务相关
                        //@todo: 应该多线程校验 暂缓
                        CommodityExcelDataHandler commodityExcelDataHandler = new CommodityExcelDataHandler();
                        commodityExcelDataHandler.setNeedHandlerFields(new String[]{"物品类别", "所属位置"});   //设置需要校验的是那些行
                        CommodityExcelDataVerifyHandler commodityExcelDataVerifyHandler = new CommodityExcelDataVerifyHandler(); //存在性校验

                        ImportParams params = new ImportParams();
                        params.setHeadRows(1);
                        params.setNeedVerify(true);
                        params.setDataHandler(commodityExcelDataHandler);
                        params.setVerifyHandler(commodityExcelDataVerifyHandler);

                        ExcelImportResult<CommodityExcelVerifyVO> excelImportResult = null;
                        try (FileInputStream fis = new FileInputStream(newFile)) {
                            //二次读取
                            excelImportResult = ExcelImportUtil.importExcelMore(fis, CommodityExcelVerifyVO.class, params);
                        } catch (Exception e) {
                            log.info("第二次读取错误.....");
                            importMonitor.setTaskStatus(Status.INTERRUPT);
                            importMonitorService.save(importMonitor);
                            e.printStackTrace();
                        }

                        successDatas.addAll(excelImportResult.getList());
                        failDatas.addAll(excelImportResult.getFailList());

                        importMonitor.setTotalNum(successDatas.size() + failDatas.size());
                        importMonitor.setProgressRate("0");


                        //@todo 校验阶段的中断不太合理（还不能在校验阶段进行中断），仅仅只是为了保存到commodity_fail这张表当中

                        if (failDatas != null && failDatas.size() > 0) {
                            for (CommodityExcelVerifyVO commodityExcelVerifyVO : failDatas) {
                                if (Thread.currentThread().isInterrupted()) {
                                    importMonitor.setFinishTime(new DateTime());
                                    importMonitor.setTaskStatus(Status.INTERRUPT);
                                    importMonitorService.save(importMonitor);
                                    verifyCancelFlag = true;
                                    return;
                                }

                                //将错误的数据直接用一张表去保存，方便后面取出
                                CommodityFail commodityFail = toCommodityFail(commodityExcelDataHandler.toFailEntity(commodityExcelVerifyVO), importMonitor.getId());
                                commodityFailService.save(new CheckResult(), commodityFail);
                                importMonitor.getFailNumRealTime().getAndIncrement();

                                //全部为错误则直接改成finish
                                if (importMonitor.getFailNumRealTime().get() == importMonitor.getTotalNum()) {
                                    importMonitor.setTaskStatus(Status.FINISH);
                                    importMonitor.setFinishTime(new DateTime());
                                    importMonitorService.save(importMonitor);
                                }
                            }
                        }

                        if (Thread.currentThread().isInterrupted()) {
                            importMonitor.setFinishTime(new DateTime());
                            importMonitor.setTaskStatus(Status.INTERRUPT);
                            importMonitorService.save(importMonitor);
                            verifyCancelFlag = true;
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        throw new AppException(ErrorConfig.通用_通用错误_Code,e.getMessage());
                    } finally {
                        if (newFile.exists()) {
                            newFile.delete();
                        }
                    }
                }
            };

            //校验完成以后，进行后续导入，相当于校验池完成之后的导入池的进行，进行的是真正的successData
            importVerifyAndDataHandlePool.setiThreadPoolTerminated(new IThreadPoolTerminated() {
                @Override
                public void terminated() {
                    //校验线程完成后，进行文件数据导入，真正的successData导入。
                    if ((!verifyCancelFlag) && successDatas != null && successDatas.size() > 0) {
                        excelImportDatas(successDatas, importMonitor);
                    }
//                    else {
//                        cr.setSuccess(false);
//                        cr.getErrorMsg().append("导入文件的数据全部校验不通过，请检查后重新导入！");
//                        return;
//                    }
                }
            });

            importVerifyAndDataHandlePool.submit(importVerify);
            importVerifyAndDataHandlePool.shutdown();
        } catch (Exception e) {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }
        //@todo: importMonitor记录中断原因 + break_reason 字段
        return importMonitor;
    }

    private CommodityFail toCommodityFail(CommodityExcelVerifyVO commodityExcelVerifyVO, String importMonitorId) {
        CommodityFail commodityFail = new CommodityFail();
        commodityFail.setName(commodityExcelVerifyVO.getName());
        commodityFail.setType(commodityExcelVerifyVO.getType());
        commodityFail.setPosition(commodityExcelVerifyVO.getPosition());
        commodityFail.setStoreId("000");
        commodityFail.setUnit(commodityExcelVerifyVO.getUnit());
        commodityFail.setCommodityNo(commodityExcelVerifyVO.getCommodityNo());
//        commodityFail.setFailReason("导入文件第 " + commodityExcelVerifyVO.getRowNum() + " 行导入失败:" + commodityExcelVerifyVO.getErrorMsg());
        commodityFail.setFailReason(commodityExcelVerifyVO.getErrorMsg());
        commodityFail.setImportMonitorId(importMonitorId);

        return commodityFail;
    }

    //导入
    private void excelImportDatas(List<CommodityExcelVerifyVO> list, ImportMonitor importMonitor) {
        datasImportPool = new HyThreadPoolBuilder().setPoolName("数据导入线程池-").setCoreSize(1).build();

        StringBuffer interrupt = new StringBuffer("INTERRUPT_FALSE");
        //根据200个进行切割
        int threadDataNum = 200;

        if (list.size() > threadDataNum) {
            int x = list.size() / threadDataNum;
            //开启n个线程
            for (int y = 0; y < x + 1; y++) {
                int endNum = (threadDataNum * (y + 1)) < list.size() ? (threadDataNum * (y + 1)) : list.size();
                //importDatasByThreadPool(list.subList(threadDataNum * y, endNum), String.valueOf(y), importMonitor, interrupt);
            }
        } else {
            //小于200开启单线程
            //importDatasByThreadPool(list, "0", importMonitor, interrupt);
        }

        //线程池关闭后续处理逻辑
        datasImportPool.setiThreadPoolTerminated(new IThreadPoolTerminated() {
            @SneakyThrows
            @Override
            public void terminated() {

                if ("INTERRUPT_FALSE".equals(interrupt.toString())) {
                    importMonitor.setTaskStatus(Status.FINISH);
                } else{
                    importMonitor.setTaskStatus(Status.INTERRUPT);
                }

                importMonitor.setSuccessNum(importMonitor.getSuccessNumRealTime().get());
                importMonitor.setImportFailNum(importMonitor.getFailNumRealTime().get());
                importMonitor.setFinishTime(new DateTime());
                importMonitorService.save(importMonitor);
            }
        });
        //关闭线程池提交任务
        datasImportPool.shutdown();
    }

    //导入操作的业务逻辑
    //@todo: runnable的解决方法
    private void importDatasByThreadPool(List<HyExcelModel> list, String id, ImportMonitor importMonitor, StringBuffer interrupt) {
        HyRunnable taskRunnable = new HyRunnable(id) {
            @Override
            public void run() {
                List<Commodity> datasList = commodityExcelDataHandler.toEntity(list);

                for (int index = 0; index < datasList.size(); index++) {
                    if (Thread.currentThread().isInterrupted()) { //判断是否中断的埋点
                        interrupt.replace(10, 14, "TRUE");  //针对整个线程池
                        return;
                    }

                    try {
                        commodityService.save(new CheckResult(), datasList.get(index));
                        importMonitor.getSuccessNumRealTime().getAndIncrement();
                    } catch (Exception e) {
                        //即使过了第一层校验，第二次入库时还是会走一次check，这里面也有可能会出错误数据
                        list.get(index).setErrorMsg(e.getMessage());
                        CommodityExcelVerifyVO commodityExcelVerifyVO = commodityExcelDataHandler.toFailEntity((CommodityExcelVerifyVO)list.get(index));
                        CommodityFail commodityFail = toCommodityFail(commodityExcelVerifyVO, importMonitor.getId());
                        commodityFailService.saveOne(commodityFail);
                        importMonitor.getFailNumRealTime().getAndIncrement();
                    }
                }
            }
        };

        try {
            datasImportPool.submit(taskRunnable); //执行导入runnable
        } catch (RejectedExecutionException e) {
            log.info("第： " + id + " 个任务提交被拒绝，超过线程池容量。");
        }
    }

    //获取导入进度数据
    //@todo:抽取成固定方法
    public String getProgressRateResult(String id) {
        loginuserId = curUserService.getLoginUserId();
        ImportMonitor curImportMonitor = importMonitorService.queryOneByAccountId(loginuserId);
        ImportMonitor importMonitorCache = null;
        if (curImportMonitor != null) {
            importMonitorCache = AppGlobal.IMPORTRATECACHE.get(curImportMonitor.getId());
        }

        String str = "";
        DecimalFormat df = new DecimalFormat("0");
        if (importMonitorCache != null) {
            //存在正在导入任务
            float progress = (float) (importMonitorCache.getFailNumRealTime().get() + importMonitorCache.getSuccessNumRealTime().get()) / importMonitorCache.getTotalNum() * 100;

            importMonitorCache.setProgressRate(df.format(progress));
            importMonitorCache.setImportFailNum(importMonitorCache.getFailNumRealTime().get());
            importMonitorCache.setSuccessNum(importMonitorCache.getSuccessNumRealTime().get());
            //sse每访问一次进行一次成功/失败数的更新
            importMonitorService.save(importMonitorCache);

            str = JsonSpringUtil.obj2Json(importMonitorCache);
        } else if (curImportMonitor != null) {
            //关机-重启  展示上次导入情况
            curImportMonitor.getSuccessNumRealTime().getAndAdd(curImportMonitor.getSuccessNum());
            curImportMonitor.getFailNumRealTime().getAndAdd(curImportMonitor.getImportFailNum());
            float progress = (float) (curImportMonitor.getFailNumRealTime().get() + curImportMonitor.getSuccessNumRealTime().get()) / curImportMonitor.getTotalNum() * 100;

            curImportMonitor.setProgressRate(df.format(progress));
            str = JsonSpringUtil.obj2Json(curImportMonitor);
        } else {
            //初次进入页面导入，创建一个空的ImportMonitor返回，不展示页面
            ImportMonitor newImportMonitor = new ImportMonitor();
            newImportMonitor.setTaskStatus(Status.ERROR);
            str = JsonSpringUtil.obj2Json(newImportMonitor);
        }

        str = "data: " + str + "\n\n";
        return str;
    }

    //取消所有任务
    //@todo:封装成固定方法
    public void cancleAllTask() {
        if (importVerifyAndDataHandlePool != null) {
            importVerifyAndDataHandlePool.cancelAllTask();
        }
        if (datasImportPool != null) {
            datasImportPool.cancelAllTask();
        }
    }

    //下载错误文件
    public void downloadErrorDatas(HttpServletResponse response) {
        loginuserId = curUserService.getLoginUserId();
        //获取旧的导入批次
        ImportMonitor oldImportMonitor = importMonitorService.queryOneByAccountId(loginuserId);
        List<CommodityFail> errorDatas = commodityFailService.getErrorDatas(oldImportMonitor.getId());
        try {
            ExcelUtil.downExcel(response, "errorDatas", errorDatas, CommodityFail.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
