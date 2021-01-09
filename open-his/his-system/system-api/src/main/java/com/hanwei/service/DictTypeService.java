package com.hanwei.service;

import com.hanwei.domain.DictType;
import com.hanwei.dto.DictTypeDto;
import com.hanwei.vo.DataGridView;

/**
 * @author hanwei
 */
public interface DictTypeService {
    /**
     * 分页查询字典类型
     * @param dictTypeDto
     * @return
     */
    DataGridView listPage(DictTypeDto dictTypeDto);
    /**
     * 查询所有字典类型（可用的）
     * @return
     */
    DataGridView list();

    /**
     * 检查字典类型是否存在，返回true为存在，false为不存在
     * @param dictType
     * @param dictId
     * @return
     */
    Boolean checkDictTypeUnique(Long dictId, String dictType);

    /**
     * 插入新的字典类型
     * @param dictTypeDto
     * @return
     */
    int insert(DictTypeDto dictTypeDto);
    /**
     * 修改的字典类型
     * @param dictTypeDto
     * @return
     */
    int update(DictTypeDto dictTypeDto);

    /**
     * 根据ID删除字典类型
     * @param dictIds
     * @return
     */
    int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 根据ID查询一个字典类型
     * @param dictId
     * @return
     */
    DictType selectDictTypeById(Long dictId);

    /**
     * 数据同步到redis
     */
    void dictCacheAsync();
}
