package com.hanwei.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanwei.constants.Constants;
import com.hanwei.domain.DictData;
import com.hanwei.domain.DictType;
import com.hanwei.dto.DictTypeDto;
import com.hanwei.mapper.DictDataMapper;
import com.hanwei.mapper.DictTypeMapper;
import com.hanwei.service.DictTypeService;
import com.hanwei.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hanwei
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {
        Page<DictType> page = new Page<>(dictTypeDto.getPageNum(), dictTypeDto.getPageSize());
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dictTypeDto.getDictName()), DictType.COL_DICT_NAME, dictTypeDto.getDictName());
        queryWrapper.like(StringUtils.isNotBlank(dictTypeDto.getDictType()), DictType.COL_DICT_NAME, dictTypeDto.getDictType());
        queryWrapper.eq(StringUtils.isNotBlank(dictTypeDto.getStatus()), DictType.COL_STATUS, dictTypeDto.getStatus());
        queryWrapper.ge(dictTypeDto.getCreateTime() != null, DictType.COL_CREATE_TIME, dictTypeDto.getCreateTime());
        queryWrapper.le(dictTypeDto.getEndTime() != null, DictType.COL_CREATE_TIME, dictTypeDto.getEndTime());
        dictTypeMapper.selectPage(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public DataGridView list() {
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        List<DictType> list = dictTypeMapper.selectList(queryWrapper);
        return new DataGridView((long) list.size(), list);
    }

    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId = (dictId == null) ? -1L : dictId;
        QueryWrapper<DictType> qw = new QueryWrapper<>();
        qw.eq(DictType.COL_DICT_TYPE, dictType);
        DictType sysDictType = this.dictTypeMapper.selectOne(qw);
        return null != sysDictType && dictId.longValue() != sysDictType.getDictId().longValue();
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        dictType.setCreateTime(DateUtil.date());
        return dictTypeMapper.insert(dictType);
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setUpdateTime(DateUtil.date());
        return dictTypeMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        List<Long> longs = Arrays.asList(dictIds);
        if(longs.size() > 0) {
            return dictTypeMapper.deleteBatchIds(longs);
        } else {
            return -1;
        }

    }

    @Override
    public DictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    /**
     * 查询出所有可用的字典类型
     * 再根据字典类型查询字典数据
     * 把字典数据生成json传到redis
     * redis的key-value设计
     * dict:dictType-dictData(Json数组)
     */
    @Override
    @Scheduled()
    public void dictCacheAsync() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        List<DictType> list = dictTypeMapper.selectList(queryWrapper);
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        for (DictType dictType : list) {
            qw.clear();
            qw.eq(DictData.COL_DICT_TYPE, dictType.getDictType());
            qw.eq(DictData.COL_STATUS, Constants.STATUS_TRUE);
            List<DictData> dictData = dictDataMapper.selectList(qw);
            String json = JSON.toJSONString(dictData);
            opsForValue.set(Constants.DICT_REDIS_PREFIX + dictType.getDictType(), json, 31, TimeUnit.DAYS);
        }
    }
}
