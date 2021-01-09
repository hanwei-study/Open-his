package com.hanwei.service;

import com.hanwei.domain.Dept;
import com.hanwei.dto.DeptDto;
import com.hanwei.vo.DataGridView;

import java.util.List;

public interface DeptService {

    /**
     * 分页查询
     * @param deptDto
     * @return
     */
    DataGridView listPage(DeptDto deptDto);

    /**
     * 查询所有有效部门
     * @return
     */
    List<Dept> list();

    /**
     * 根据ID查询一个
     * @param deptId
     * @return
     */
    Dept getOne(Long deptId);

    /**
     * 添加一个部门
     * @param deptDto
     * @return
     */
    int addDept(DeptDto deptDto);

    /**
     * 修改部门
     * @param deptDto
     * @return
     */
    int updateDept(DeptDto deptDto);

    /**
     * 根据IDS删除部门
     * @param deptIds
     * @return
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 根据部门ID查询部门信息
     * @param deptIds
     * @return
     */
    List<Dept> listDeptByDeptIds(List<Long> deptIds);

    /**
     * 根据部门ID更新部门挂号编号
     * @param deptId
     * @param regNumber
     */
    void updateDeptRegNumber(Long deptId, Integer regNumber);

}
