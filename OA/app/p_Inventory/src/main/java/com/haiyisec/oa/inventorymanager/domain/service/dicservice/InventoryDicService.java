package com.haiyisec.oa.inventorymanager.domain.service.dicservice;

import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfo;
import com.haiyisec.oa.inventorymanager.domain.model.po.baseinfo.AttributeInfoItem;
import com.haiyisec.oa.inventorymanager.domain.model.po.goods.Commodity;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.AttributeInfoItemRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.CommodityRepository;
import com.haiyisec.oa.inventorymanager.domain.repository.InStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zen.frame.base.domain.annotation.dic.Zen_Dic_DD;
import org.zen.modules.commonservice.dic.domain.model.vo.DicItemVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InventoryDicService {
    @Autowired
    private AttributeInfoRepository attributeInfoRepository;
    @Autowired
    private AttributeInfoItemRepository attributeInfoItemRepository;

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private InStoreRepository instoreRepository;
    @Zen_Dic_DD(dicName = "dd_物品类别", beanName = "InventoryDicService")
    public List<DicItemVO> getDic_DD_type(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        AttributeInfo list_attri =  attributeInfoRepository.findByName("物品类别");
        List<AttributeInfoItem> list_attri_info = attributeInfoItemRepository.findByAttributeInfoId(list_attri.getId());
        for (AttributeInfoItem attributeInfoItem : list_attri_info) {
            DicItemVO item = new DicItemVO();
            item.setCode(attributeInfoItem.getId());
            item.setText(attributeInfoItem.getName());
            list_vo.add(item);
        }
        return list_vo;
    }

    @Zen_Dic_DD(dicName = "dd_所属位置", beanName = "InventoryDicService")
    public List<DicItemVO> getDic_DD_position(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        AttributeInfo list_attri =  attributeInfoRepository.findByName("所属位置");
        List<AttributeInfoItem> list_attri_info = attributeInfoItemRepository.findByAttributeInfoId(list_attri.getId());
        for (AttributeInfoItem attributeInfoItem : list_attri_info) {
            DicItemVO item = new DicItemVO();
            item.setCode(attributeInfoItem.getId());
            item.setText(attributeInfoItem.getName());
            list_vo.add(item);
        }
        return list_vo;
    }

    @Zen_Dic_DD(dicName = "dd_物品信息", beanName = "InventoryDicService")
    public List<DicItemVO> getDic_DD_commodity(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        List<Commodity> list_commodity =  commodityRepository.findAll();
        for (Commodity comodity : list_commodity) {
            DicItemVO item = new DicItemVO();
            item.setCode(comodity.getId());
            item.setText(comodity.getName());
            list_vo.add(item);
        }
        return list_vo;
    }
    @Zen_Dic_DD(dicName = "dd_入库物品信息", beanName = "InventoryDicService")
    public List<DicItemVO> getDic_DD_in_commodity(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        List<Commodity> list_commodity =  commodityRepository.findAndRest();
        for (Commodity commodity : list_commodity) {
            DicItemVO item = new DicItemVO();
            item.setCode(commodity.getId());
            item.setText(commodity.getName());


            list_vo.add(item);
        }
        return list_vo;
    }

    @Zen_Dic_DD(dicName = "dd_申请人", beanName = "InventoryDicService")
    public List<DicItemVO> getDic_DD_applicant(Map<String, Object> map_param) {
        List<DicItemVO> list_vo = new ArrayList<DicItemVO>();
        AttributeInfo list_attri =  attributeInfoRepository.findByName("申请人");
        List<AttributeInfoItem> list_attri_info_items =  attributeInfoItemRepository.findByAttributeInfoId(list_attri.getId());
        for (AttributeInfoItem attributeInfoItem : list_attri_info_items) {
            DicItemVO item = new DicItemVO();
            item.setCode(attributeInfoItem.getId());
            item.setText(attributeInfoItem.getName());
            list_vo.add(item);
        }
        return list_vo;
    }
}
