package com.hanwei.service;

import com.hanwei.domain.CheckResult;
import com.hanwei.dto.CheckResultDto;
import com.hanwei.dto.CheckResultFormDto;
import com.hanwei.vo.DataGridView;

public interface CheckResultService{

    /**
     * 保存检查项目信息
     *
     * @param checkResult
     * @return
     */
    int saveCheckResult(CheckResult checkResult);

    /**
     * 根据条件查询所有检查中的和检查完成了的项目
     * @param checkResultDto
     * @return
     */
    DataGridView queryAllCheckResultForPage(CheckResultDto checkResultDto);

    /**
     * 完成检查
     * @param checkResultFormDto
     * @return
     */
    int completeCheckResult(CheckResultFormDto checkResultFormDto);


}
