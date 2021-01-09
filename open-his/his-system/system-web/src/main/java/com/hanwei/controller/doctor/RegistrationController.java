package com.hanwei.controller.doctor;

import com.hanwei.constants.Constants;
import com.hanwei.domain.Dept;
import com.hanwei.domain.Patient;
import com.hanwei.domain.Registration;
import com.hanwei.dto.PatientDto;
import com.hanwei.dto.RegistrationDto;
import com.hanwei.dto.RegistrationFormDto;
import com.hanwei.dto.RegistrationQueryDto;
import com.hanwei.service.DeptService;
import com.hanwei.service.PatientService;
import com.hanwei.service.RegistrationService;
import com.hanwei.service.SchedulingService;
import com.hanwei.util.ShiroSecurityUtils;
import com.hanwei.utils.IdGeneratorSnowflake;
import com.hanwei.vo.AjaxResult;
import com.hanwei.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("doctor/registration")
public class RegistrationController {

    @Reference
    private SchedulingService schedulingService;

    @Reference
    private PatientService patientService;

    @Reference
    private RegistrationService registrationService;

    @Autowired
    private DeptService deptService;

    /**
     * 查询出所有的排班的部门列表
     * 1.从排班表里面查询有排班的部门编号集合
     * 2.根据查询出来的部门编号集合再去查询部门
     */
    @GetMapping("listDeptForScheduling")
    @HystrixCommand
    public AjaxResult listDeptForScheduling(@Validated RegistrationQueryDto registrationQueryDto) {
        Long deptId = registrationQueryDto.getDeptId();
        String subsectionType = registrationQueryDto.getSubsectionType();
        //挂号类型
        String schedulingType = registrationQueryDto.getSchedulingType();
        //时间
        String schedulingDay = registrationQueryDto.getSchedulingDay().substring(0, 10);
        List<Long> deptIds = schedulingService.queryHasSchedulingDeptIds(deptId, schedulingDay, schedulingType, subsectionType);
        if (null == deptIds || deptIds.size() == 0) {
            return AjaxResult.success(Collections.EMPTY_LIST);
        } else {
            List<Dept> list = this.deptService.listDeptByDeptIds(deptIds);
            return AjaxResult.success(list);
        }
    }


    /**
     * 根据身份证号查询患者信息
     */
    @GetMapping("getPatientByIdCard/{idCard}")
    public AjaxResult getPatientByIdCard(@PathVariable String idCard) {

        Patient patient = this.patientService.getPatientByIdCard(idCard);
        if (null == patient) {
            return AjaxResult.fail("【" + idCard + "】对应的患者不存在，请在下面新建患者信息");
        } else {
            return AjaxResult.success(patient);
        }

    }

    /**
     * 挂号
     */
    @PostMapping("addRegistration")
    @HystrixCommand
    public AjaxResult addRegistration(@RequestBody @Validated RegistrationFormDto registrationFormDto) {
        PatientDto patientDto = registrationFormDto.getPatientDto();
        RegistrationDto registrationDto = registrationFormDto.getRegistrationDto();
        Patient patient = null;
        //说明要新添加患者
        if (StringUtils.isBlank(patientDto.getPatientId())) {
            patientDto.setPatientId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_HZ));
            patientDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            patient = patientService.addPatient(patientDto);
        } else {
            //如果有患者编号，就把患者信息查询出来
            patient = this.patientService.getPatientById(patientDto.getPatientId());
        }
        if (null == patient) {
            return AjaxResult.fail("当前患者ID不存在，请确认后再提交");
        }
        //查询部门信息
        Dept dept = this.deptService.getOne(registrationDto.getDeptId());
        //保存患者信息
        registrationDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        registrationDto.setRegId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PREFIX_GH));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        //编号+1
        registrationDto.setRegNumber(dept.getRegNumber() + 1);
        this.registrationService.addRegistration(registrationDto);
        //更新当天的科室号段
        this.deptService.updateDeptRegNumber(dept.getDeptId(), dept.getRegNumber() + 1);
        //返回挂号编号给前端
        return AjaxResult.success("", registrationDto.getRegId());
    }

    /**
     * 收费
     */
    @PostMapping("collectFee/{regId}")
    @HystrixCommand
    public AjaxResult collectFee(@PathVariable String regId) {
        //查询挂号单
        Registration registration = this.registrationService.queryRegistrationByRegId(regId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号单【" + regId + "】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_0)) {
            return AjaxResult.fail("当前挂号单【" + regId + "】的状态不是未收费状态，不能进行收费");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_1);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));
    }

    /**
     * 分页查询挂号信息
     */
    @GetMapping("queryRegistrationForPage")
    @HystrixCommand
    public AjaxResult queryRegistrationForPage(RegistrationDto registrationDto) {
        DataGridView gridView = this.registrationService.queryRegistrationForPage(registrationDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }


    /**
     * 作废
     */
    @PostMapping("doInvalid/{regId}")
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String regId) {
        Registration registration = this.registrationService.queryRegistrationByRegId(regId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号单【" + regId + "】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_0)) {
            return AjaxResult.fail("当前挂号单【" + regId + "】的状态不是未收费状态，不能作废");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_5);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));
    }

    /**
     * 退号
     */
    @PostMapping("doReturn/{regId}")
    @HystrixCommand
    public AjaxResult doReturn(@PathVariable String regId) {
        Registration registration = this.registrationService.queryRegistrationByRegId(regId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号单【" + regId + "】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_1)) {
            return AjaxResult.fail("当前挂号单【" + regId + "】的状态不是待就诊状态，不能退号");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));
    }
}