package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.domain.CareOrderItem;
import com.hanwei.domain.CheckResult;
import com.hanwei.domain.OrderChargeItem;
import com.hanwei.dto.CheckResultDto;
import com.hanwei.dto.CheckResultFormDto;
import com.hanwei.mapper.CareOrderItemMapper;
import com.hanwei.mapper.CheckResultMapper;
import com.hanwei.mapper.OrderChargeItemMapper;
import com.hanwei.service.CheckResultService;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(methods = {@Method(name = "saveCheckResult",retries = 0)})
public class CheckResultServiceImpl implements CheckResultService {

    @Autowired
    private CheckResultMapper checkResultMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Override
    public int saveCheckResult(CheckResult checkResult) {
        //保存检查项目
        int i=checkResultMapper.insert(checkResult);
        //更新收费详情的状态
        OrderChargeItem orderChargeItem=new OrderChargeItem();
        orderChargeItem.setItemId(checkResult.getItemId());
        //已完成
        orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.orderChargeItemMapper.updateById(orderChargeItem);
        //更新处方详情的状态
        CareOrderItem careOrderItem=new CareOrderItem();
        careOrderItem.setItemId(checkResult.getItemId());
        //已完成
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
        this.careOrderItemMapper.updateById(careOrderItem);
        return i;
    }

    @Override
    public DataGridView queryAllCheckResultForPage(CheckResultDto checkResultDto) {
        Page<CheckResult> page=new Page<>(checkResultDto.getPageNum(),checkResultDto.getPageSize());
        QueryWrapper<CheckResult> qw=new QueryWrapper<>();
        qw.in(checkResultDto.getCheckItemIds().size()>0,CheckResult.COL_CHECK_ITEM_ID,checkResultDto.getCheckItemIds());
        qw.like(StringUtils.isNotBlank(checkResultDto.getPatientName()),CheckResult.COL_PATIENT_NAME,checkResultDto.getPatientName());
        qw.like(StringUtils.isNotBlank(checkResultDto.getRegId()),CheckResult.COL_REG_ID,checkResultDto.getRegId());
        qw.eq(StringUtils.isNotBlank(checkResultDto.getResultStatus()),CheckResult.COL_RESULT_STATUS,checkResultDto.getResultStatus());
        this.checkResultMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int completeCheckResult(CheckResultFormDto checkResultFormDto) {
        CheckResult checkResult=new CheckResult();
        BeanUtil.copyProperties(checkResultFormDto,checkResult);
        //检查完成
        checkResult.setResultStatus(Constants.RESULT_STATUS_1);
        checkResult.setUpdateBy(checkResultFormDto.getSimpleUser().getUserName());
        return this.checkResultMapper.updateById(checkResult);
    }
}