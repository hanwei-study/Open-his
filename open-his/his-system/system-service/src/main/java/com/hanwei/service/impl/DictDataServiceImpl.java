package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.domain.DictData;
import com.hanwei.dto.DictDataDto;
import com.hanwei.mapper.DictDataMapper;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.hanwei.service.DictDataService;
import java.util.Arrays;
import java.util.List;

/**
 * @author hanwei
 */
@Service
public class DictDataServiceImpl implements DictDataService{

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public DataGridView listPage(DictDataDto dictDataDto) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<>();
        Page<DictData> page = new Page<>(dictDataDto.getPageNum(), dictDataDto.getPageSize());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataDto.getDictType()), DictData.COL_DICT_TYPE,dictDataDto.getDictType());
        queryWrapper.like(StringUtils.isNotBlank(dictDataDto.getDictLabel()), DictData.COL_DICT_LABEL,dictDataDto.getDictLabel());
        queryWrapper.eq(StringUtils.isNotBlank(dictDataDto.getStatus()), DictData.COL_STATUS,dictDataDto.getStatus());
        this.dictDataMapper.selectPage(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int insert(DictDataDto dictDataDto) {
        DictData dictData=new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        //设置创建者，创建时间
        dictData.setCreateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setCreateTime(DateUtil.date());
        return this.dictDataMapper.insert(dictData);
    }

    @Override
    public int update(DictDataDto dictDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        //设置修改人
        dictData.setUpdateBy(dictDataDto.getSimpleUser().getUserName());
        return this.dictDataMapper.updateById(dictData);
    }

    @Override
    public int deleteDictDataByIds(Long[] dictCodeIds) {
        List<Long> ids= Arrays.asList(dictCodeIds);
        if(ids.size() > 0){
            return this.dictDataMapper.deleteBatchIds(ids);
        }else{
            return -1;
        }
    }

    /**
     * 通过redis缓存获取
     * @param dictType
     * @return
     */
    @Override
    public List<DictData> selectDictDataByDictType(String dictType) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String json = opsForValue.get(Constants.DICT_REDIS_PREFIX + dictType);
        return JSON.parseArray(json, DictData.class);
    }

    @Override
    public DictData selectDictDataById(Long dictCode) {
        return this.dictDataMapper.selectById(dictCode);
    }
}
