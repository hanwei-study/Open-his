package com.hanwei.controller.doctor;


import cn.hutool.core.bean.BeanUtil;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.*;
import com.hanwei.dto.PatientDto;
import com.hanwei.service.CareService;
import com.hanwei.service.PatientService;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author hanwei
 * @ClassName PatientController
 * @date 2020/12/10
 */
@RestController
@RequestMapping("doctor/patient")
public class PatientController  extends BaseController {

    @Reference
    private PatientService patientService;

    @Reference
    private CareService careService;

    /**
     * 分页查询
     */
    @GetMapping("listPatientForPage")
    public AjaxResult listPatientForPage(PatientDto patientDto) {
        DataGridView gridView = this.patientService.listPatientForPage(patientDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 根据患者ID查询患者信息
     */
    @GetMapping("getPatientById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientById(@PathVariable String patientId) {
        Patient patient = this.patientService.getPatientById(patientId);
        return AjaxResult.success(patient);
    }


    /**
     * 根据患者ID查询患者的档案信息
     */
    @GetMapping("getPatientFileById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientFileById(@PathVariable String patientId) {
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }

    /**
     * 根据患者ID查询患者信息 患者档案信息  历史病例
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId){
        //查询病历
        List<CareHistory> careHistories=this.careService.queryCareHistoryByPatientId(patientId);
        List<Map<String, Object>> res = new ArrayList<>();
        for (CareHistory careHistory : careHistories) {
            Map<String, Object> careHistoryMap = BeanUtil.beanToMap(careHistory);
            careHistoryMap.put("careOrders", Collections.EMPTY_LIST);
            List<Map<String, Object>> reCareOrders = new ArrayList<>();
            //根据病历ID查询处方列表
            List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder order : careOrders) {
                Map<String, Object> careOrder = BeanUtil.beanToMap(order);
                List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByCoId(order.getCoId(), null);
                careOrder.put("careOrderItems", careOrderItems);
                reCareOrders.add(careOrder);
            }
            careHistoryMap.put("careOrders", reCareOrders);
            res.add(careHistoryMap);
        }
        return AjaxResult.success(res);
    }


}