package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.dto.ProviderDto;
import com.hanwei.mapper.ProviderMapper;
import com.hanwei.domain.Provider;
import com.hanwei.service.ProviderService;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;

@Service(methods = {@Method(name = "addProvider",retries = 0)})
public class ProviderServiceImpl implements ProviderService{

    @Autowired
    private ProviderMapper providerMapper;

    @Override
    public DataGridView listProviderPage(ProviderDto providerDto) {
        Page<Provider> page=new Page<>(providerDto.getPageNum(),providerDto.getPageSize());
        QueryWrapper<Provider> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(providerDto.getProviderName()),Provider.COL_PROVIDER_NAME,providerDto.getProviderName());
        qw.like(StringUtils.isNotBlank(providerDto.getContactName()),Provider.COL_CONTACT_NAME,providerDto.getContactName());
        //(tel like ? or mobile like ?)
        qw.and(StringUtils.isNotBlank(providerDto.getContactTel()),
                providerQueryWrapper -> providerQueryWrapper.like(Provider.COL_CONTACT_TEL, providerDto.getContactTel())
                                .or().like(Provider.COL_CONTACT_MOBILE, providerDto.getContactTel()));
        qw.eq(StringUtils.isNotBlank(providerDto.getStatus()),Provider.COL_STATUS,providerDto.getStatus());
        this.providerMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Provider getOne(Long providerId) {
        return this.providerMapper.selectById(providerId);
    }

    @Override
    public int addProvider(ProviderDto providerDto) {
        Provider provider=new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        provider.setCreateTime(DateUtil.date());
        provider.setCreateBy(providerDto.getSimpleUser().getUserName());
        return this.providerMapper.insert(provider);
    }

    @Override
    public int updateProvider(ProviderDto providerDto) {
        Provider provider=new Provider();
        BeanUtil.copyProperties(providerDto,provider);
        provider.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return this.providerMapper.updateById(provider);
    }

    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        List<Long> ids= Arrays.asList(providerIds);
        if(ids.size()>0){
            return this.providerMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Provider> selectAllProvider() {
        QueryWrapper<Provider> qw=new QueryWrapper<>();
        qw.eq(Provider.COL_STATUS, Constants.STATUS_TRUE);
        return this.providerMapper.selectList(qw);
    }
}
