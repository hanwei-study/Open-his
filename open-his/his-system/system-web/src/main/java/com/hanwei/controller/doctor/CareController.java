package com.hanwei.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.hanwei.constants.Constants;
import com.hanwei.controller.BaseController;
import com.hanwei.domain.*;
import com.hanwei.dto.CareHistoryDto;
import com.hanwei.dto.CareOrderFormDto;
import com.hanwei.service.CareService;
import com.hanwei.service.DeptService;
import com.hanwei.service.PatientService;
import com.hanwei.service.RegistrationService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.utils.HisDateUtils;
import com.hanwei.utils.IdGeneratorSnowflake;
import com.hanwei.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hanwei
 * @ClassName CareController
 * @date 2020/12/13
 */
@RestController
@RequestMapping("doctor/care")
public class CareController extends BaseController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private PatientService patientService;

    @Reference
    private CareService careService;

    @Autowired
    private DeptService deptService;


    /**
     * 查询待就诊的挂号信息
     * GET/doctor/care/queryToBeSeenRegistration/{scheudlingType}
     */
    @GetMapping("queryToBeSeenRegistration/{scheudlingType}")
    public AjaxResult queryToBeSeenRegistration(@PathVariable String scheudlingType) {
        //得到当前用户的部门ID
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        //设置要查询的状态  只能是挂号单的待就诊的挂号信息
        String regStatus = Constants.REG_STATUS_1;
        //计算时段
        String subsectionType = HisDateUtils.getCurrentTimeType();
        //查询
        Long userId = null;
        List<Registration> list = this.registrationService.queryRegistration(deptId, subsectionType, scheudlingType, regStatus, userId);
        return AjaxResult.success(list);
    }


    /**
     * 查询就诊中的挂号信息
     * GET/doctor/care/queryVisitingRegistration/{scheudlingType}
     */
    @GetMapping("queryVisitingRegistration/{scheudlingType}")
    public AjaxResult queryVisitingRegistration(@PathVariable String scheudlingType) {
        //得到当前用户的部门ID
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        //设置要查询的状态  只能是挂号单的待就诊的挂号信息
        String regStatus = Constants.REG_STATUS_2;
        //查询
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> list = this.registrationService.queryRegistration(deptId, null, scheudlingType, regStatus, userId);
        return AjaxResult.success(list);
    }

    /**
     * 查询就诊完成的挂号信息
     * GET/doctor/care/queryVisitCompletedRegistration/{scheudlingType}
     */
    @GetMapping("queryVisitCompletedRegistration/{scheudlingType}")
    public AjaxResult queryVisitCompletedRegistration(@PathVariable String scheudlingType) {
        //得到当前用户的部门ID
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        //设置要查询的状态  只能是挂号单的待就诊的挂号信息
        String regStatus = Constants.REG_STATUS_3;
        //查询
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> list = this.registrationService.queryRegistration(deptId, null, scheudlingType, regStatus, userId);
        return AjaxResult.success(list);
    }

    /**
     * 接诊
     * POST/doctor/care/receivePatient/{regId}
     */
    @PostMapping("receivePatient/{regId}")
    public AjaxResult receivePatient(@PathVariable String regId) {
        //防止并发接诊的问题，保证接诊人之间没有冲突
        synchronized (this) {
            //根据挂号单ID查询挂号信息
            Registration registration = this.registrationService.queryRegistrationByRegId(regId);
            if (null == registration) {
                return AjaxResult.fail("【" + regId + "】挂号单的不存在，不能接诊");
            }
            //只有当挂号单的状态 regStatus为 待就诊时可以接诊
            if (registration.getRegStatus().equals(Constants.REG_STATUS_1)) {
                //就诊中
                registration.setRegStatus(Constants.REG_STATUS_2);
                registration.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
                registration.setDoctorName(ShiroSecurityUtils.getCurrentUser().getUserName());
                return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));
            } else {
                return AjaxResult.fail("【" + regId + "】挂号单的状态不是待就诊状态，不能接诊");
            }
        }
    }


    /**
     * 根据患者ID获取患者信息、档案信息、病历信息
     * GET/doctor/care/getPatientAllMessageByPatientId/{patientId}
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId) {
        //查询患者信息
        Patient patient = this.patientService.getPatientById(patientId);
        //查询档案
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        //查询病历表
        List<CareHistory> careHistories = this.careService.queryCareHistoryByPatientId(patientId);
        Map<String, Object> res = new HashMap<>();
        res.put("patient", patient);
        res.put("patientFile", patientFile);
        res.put("careHistoryList", careHistories);
        return AjaxResult.success(res);
    }

    /**
     * 保存病例信息
     */
    @PostMapping("saveCareHistory")
    public AjaxResult saveCareHistory(@RequestBody CareHistoryDto careHistoryDto){
        careHistoryDto.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        careHistoryDto.setUserName(ShiroSecurityUtils.getCurrentUserName());
        careHistoryDto.setDeptId(ShiroSecurityUtils.getCurrentUser().getDeptId());
        Dept dept=this.deptService.getOne(ShiroSecurityUtils.getCurrentUser().getDeptId());
        careHistoryDto.setDeptName(dept.getDeptName());
        careHistoryDto.setCareDate(DateUtil.date());
        CareHistory ch = this.careService.saveOrUpdateCareHistory(careHistoryDto);
        return AjaxResult.success(ch);
    }
    /**
     * 根据挂号单位ID查询对应的病历信息
     */
    @GetMapping("getCareHistoryByRegId/{regId}")
    @HystrixCommand
    public AjaxResult getCareHistoryByRegId(@PathVariable String regId){
        CareHistory careHistory=this.careService.queryCareHistoryByRegId(regId);
        return AjaxResult.success(careHistory);
    }


    /**
     * 根据病历ID查询处方信息及详情信息
     */
    @GetMapping("queryCareOrdersByChId/{chId}")
    @HystrixCommand
    public AjaxResult queryCareOrdersByChId(@PathVariable String chId){
        List<CareOrder> careOrders=this.careService.queryCareOrdersByChId(chId);
        List<Map<String,Object>> res=new ArrayList<>();
        for (CareOrder careOrder : careOrders) {
            Map<String,Object> map=new HashMap<>();
            map.put("careOrder",careOrder);
            List<CareOrderItem> careOrderItems=this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), null);
            map.put("careOrderItems",careOrderItems);
            res.add(map);
        }
        return AjaxResult.success(res);
    }
    /**
     * 保存处方及详情信息
     */
    @PostMapping("saveCareOrderItem")
    @HystrixCommand
    public AjaxResult saveCareOrderItem(@RequestBody @Validated CareOrderFormDto careOrderFormDto){
        //根据病例ID查询病历信息
        CareHistory careHistory=this.careService.queryCareHistoryByChId(careOrderFormDto.getCareOrder().getChId());
        if(null==careHistory){
            return AjaxResult.fail("病历ID不存在，请核对后再提交");
        }
        careOrderFormDto.getCareOrder().setCoId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_CO));
        careOrderFormDto.getCareOrder().setPatientId(careHistory.getPatientId());
        careOrderFormDto.getCareOrder().setPatientName(careHistory.getPatientName());
        careOrderFormDto.getCareOrder().setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        careOrderFormDto.getCareOrder().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.careService.saveCareOrderItem(careOrderFormDto));
    }

    /**
     * 根据处方详情ID删除处方详情【只能删除未支付的】
     */
    @DeleteMapping("deleteCareOrderItemById/{itemId}")
    @HystrixCommand
    public AjaxResult deleteCareOrderItemById(@PathVariable String itemId){
        CareOrderItem careOrderItem=this.careService.queryCareOrderItemByItemId(itemId);
        if(null==careOrderItem){
            return AjaxResult.fail("处方详情ID不存在");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_0)){
            return AjaxResult.fail("【"+itemId+"】不是未支付状态，不能删除");
        }
        return AjaxResult.toAjax(this.careService.deleteCareOrderItemByItemId(itemId));
    }

    /**
     *完成就诊
     */
    @PostMapping("visitComplete/{regId}")
    @HystrixCommand
    public AjaxResult visitComplete(@PathVariable String regId){
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("【"+regId+"】挂号单号不存在，请核对后再提交");
        }
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_2)){
            return AjaxResult.fail("【"+regId+"】状态不是就诊中状态，不能完成就诊");
        }
        //更改挂号单的状态
        return AjaxResult.toAjax(this.careService.visitComplete(regId));
    }



}