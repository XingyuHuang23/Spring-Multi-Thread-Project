package com.haiyisec.oa.inventorymanager.domain.service.test;


import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.CommodityFail;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.CommodityExcelVerifyVO;
import com.haiyisec.oa.inventorymanager.domain.model.vo.goods.HyExcelModel;
import com.haiyisec.oa.inventorymanager.domain.service.CommodityService;
import com.haiyisec.oa.inventorymanager.domain.service.ImportMonitorService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.CommodityFailService;
import com.haiyisec.oa.inventorymanager.domain.service.importservice.excelservice.CommodityExcelDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zen.frame.base.domain.obj.CheckResult;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommodityAdaption implements IHyFailHandler,IHySuccessHandler,IHyPreDateHandler {

    @Autowired
    ImportMonitorService importMonitorService;

    @Autowired
    private CommodityService commodityService;

    //@change: 该变量导致并发问题   XXX 由开发人员自己编写
    private CommodityExcelDataHandler commodityExcelDataHandler = new CommodityExcelDataHandler();

    @Autowired
    private CommodityFailService commodityFailService;
    //@change: 该变量导致并发问题  XXX 由开发人员自己编写
    private List<CommodityFail> commodityFail_list = new ArrayList<>();
    @Override
    public void preDateHandler(CheckResult cr, List successDatas, List failDatas,String taskId) {

        //先保留失败数据failData
        if (failDatas != null && failDatas.size() > 0) {

            for(int i=0;i<failDatas.size();i++) {

                commodityFail_list.add(toCommodityFail(commodityExcelDataHandler.toFailEntity((CommodityExcelVerifyVO)failDatas.get(i)), taskId));

            }

        }
    }
    @Override
    public  void failHandler(List<HyExcelModel> failDatas, String taskId){
        for(int i=0;i<commodityFail_list.size();i++) {
            commodityFailService.save(new CheckResult(), commodityFail_list.get(i));
        }
    }

    @Override
    //@change: 修改为返回 boolean XXX
    public  void successHandler(List<HyExcelModel> successDatas, String taskId){

        List<Commodity> datasList = commodityExcelDataHandler.toEntity(successDatas);
        importMonitorService.setStartTime(taskId);

        for (int index = 0; index < datasList.size(); index++) {

            try {
                commodityService.save(new CheckResult(), datasList.get(index));

                //@change: 挪到导入组件中处理
                importMonitorService.successNumRealTime(taskId);

            } catch (Exception e) {
                //即使过了第一层校验，第二次入库时还是会走一次check，这里面也有可能会出错误数据
                successDatas.get(index).setErrorMsg(e.getMessage());
                CommodityExcelVerifyVO commodityExcelVerifyVO = commodityExcelDataHandler.toFailEntity((CommodityExcelVerifyVO)successDatas.get(index));
                CommodityFail commodityFail = toCommodityFail(commodityExcelVerifyVO, taskId);
                commodityFailService.saveOne(commodityFail);

                //@change: 挪到导入组件中处理
                importMonitorService.failNumRealTime(taskId);
            }
        }
    }

//    @Override
//    public void importData(List<HyExcelModel> successDatas,String taskId) {
//
//        List<Commodity> datasList = commodityExcelDataHandler.toEntity(successDatas);
//        importMonitorService.setStartTime(taskId);
//
//        for (int index = 0; index < datasList.size(); index++) {
//
//            try {
//                commodityService.save(new CheckResult(), datasList.get(index));
//
//                importMonitorService.successNumRealTime(taskId);
//
//            } catch (Exception e) {
//                //即使过了第一层校验，第二次入库时还是会走一次check，这里面也有可能会出错误数据
//                successDatas.get(index).setErrorMsg(e.getMessage());
//                CommodityExcelVerifyVO commodityExcelVerifyVO = commodityExcelDataHandler.toFailEntity((CommodityExcelVerifyVO)successDatas.get(index));
//                CommodityFail commodityFail = toCommodityFail(commodityExcelVerifyVO, taskId);
//                commodityFailService.saveOne(commodityFail);
//
//                importMonitorService.failNumRealTime(taskId);
//            }
//        }
//    }

//    @Override
//    public void importTerminate() {
//
//    }

    private CommodityFail toCommodityFail(CommodityExcelVerifyVO commodityExcelVerifyVO, String taskId) {

        //@change: org.springframework.beans.BeanUtils.copyProperties
        CommodityFail commodityFail = new CommodityFail();
        commodityFail.setName(commodityExcelVerifyVO.getName());
        commodityFail.setType(commodityExcelVerifyVO.getType());
        commodityFail.setPosition(commodityExcelVerifyVO.getPosition());
        commodityFail.setStoreId("000");
        commodityFail.setUnit(commodityExcelVerifyVO.getUnit());
        commodityFail.setCommodityNo(commodityExcelVerifyVO.getCommodityNo());
//        commodityFail.setFailReason("导入文件第 " + commodityExcelVerifyVO.getRowNum() + " 行导入失败:" + commodityExcelVerifyVO.getErrorMsg());
        //@change:  注意这些属性需要手动设置
        commodityFail.setFailReason(commodityExcelVerifyVO.getErrorMsg());
        commodityFail.setImportMonitorId("import");
        commodityFail.setTaskId(taskId);
        return commodityFail;
    }
}
