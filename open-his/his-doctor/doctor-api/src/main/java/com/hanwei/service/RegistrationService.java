package com.hanwei.service;

import com.hanwei.domain.Registration;
import com.hanwei.dto.RegistrationDto;
import com.hanwei.vo.DataGridView;

import java.util.List;

/**
 * 挂号相关接口
 */
public interface RegistrationService {
    /**
     * 保存挂号单信息
     * @param registrationDto
     */
    void addRegistration(RegistrationDto registrationDto);

    /**
     * 分页加载挂号列表
     * @param registrationDto
     * @return
     */
    DataGridView queryRegistrationForPage(RegistrationDto registrationDto);

    /**
     * 更新挂号单信息
     * @param registration
     * @return
     */
    int updateRegistrationById(Registration registration);

    /**
     * 根据挂号ID查询挂号信息
     * @param registrationId
     * @return
     */
    Registration queryRegistrationByRegId(String registrationId);

    /**
     * 根据条件查询挂号信息
     * @param deptId 部门
     * @param subsectionType  时段
     * @param scheudlingType  类型  门诊 急诊
     * @param regStatus    挂号单状态
     * @param userId   医生ID
     * @return
     */
    List<Registration> queryRegistration(Long deptId, String subsectionType, String scheudlingType, String regStatus, Long userId);

}
