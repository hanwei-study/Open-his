package com.hanwei.service;

import com.hanwei.domain.Patient;
import com.hanwei.domain.PatientFile;
import com.hanwei.dto.PatientDto;
import com.hanwei.vo.DataGridView;

/**
 * 患者相关接口
 */
public interface PatientService {

    /**
     * 分页查询
     * @param patientDto
     * @return
     */
    DataGridView listPatientForPage(PatientDto patientDto);

    /**
     * 根据患者ID查询患者信息
     * @param patientId
     * @return
     */
    Patient getPatientById(String patientId);

    /**
     * 根据患者ID查询患者档案
     * @param patientId
     * @return
     */
    PatientFile getPatientFileById(String patientId);

    /**
     * 根据身份证号查询患者信息
     * @param idCard
     * @return
     */
    Patient getPatientByIdCard(String idCard);

    /**
     * 保存患者信息
     * @param patientDto
     */
    Patient addPatient(PatientDto patientDto);

}
