package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.domain.PatientFile;
import com.hanwei.dto.PatientDto;
import com.hanwei.mapper.PatientFileMapper;
import com.hanwei.mapper.PatientMapper;
import com.hanwei.domain.Patient;
import com.hanwei.service.PatientService;
import com.hanwei.utils.AppMd5Utils;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientFileMapper patientFileMapper;

    @Override
    public DataGridView listPatientForPage(PatientDto patientDto) {
        Page<Patient> page = new Page<>(patientDto.getPageNum(), patientDto.getPageSize());
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(patientDto.getName()), Patient.COL_NAME, patientDto.getName());
        qw.like(StringUtils.isNotBlank(patientDto.getIdCard()), Patient.COL_ID_CARD, patientDto.getIdCard());
        qw.like(StringUtils.isNotBlank(patientDto.getPhone()), Patient.COL_PHONE, patientDto.getPhone());
        this.patientMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public Patient getPatientById(String patientId) {
        return this.patientMapper.selectById(patientId);
    }

    @Override
    public PatientFile getPatientFileById(String patientId) {
        if (null == patientId) {
            return null;
        }
        QueryWrapper<PatientFile> qw = new QueryWrapper<>();
        qw.eq(PatientFile.COL_PATIENT_ID, patientId);
        return this.patientFileMapper.selectOne(qw);
    }

    @Override
    public Patient getPatientByIdCard(String idCard) {
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        qw.eq(Patient.COL_ID_CARD, idCard);
        return this.patientMapper.selectOne(qw);
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        BeanUtil.copyProperties(patientDto, patient);
        patient.setCreateTime(DateUtil.date());
        patient.setIsFinal(Constants.IS_FINAL_FALSE);
        String defaultPwd = patient.getPhone().substring(5);
        patient.setPassword(AppMd5Utils.md5(defaultPwd, patient.getPhone(), 2));
        this.patientMapper.insert(patient);
        return patient;
    }

}