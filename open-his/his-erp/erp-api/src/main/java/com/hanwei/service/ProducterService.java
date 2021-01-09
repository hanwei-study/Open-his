package com.hanwei.service;

import com.hanwei.domain.Producter;
import com.hanwei.dto.ProducterDto;
import com.hanwei.vo.DataGridView;

import java.util.List;

public interface ProducterService {
    /**
     * 分页查询
     *
     * @param producterDto
     * @return
     */
    DataGridView listProducterPage(ProducterDto producterDto);

    /**
     * 根据ID查询
     *
     * @param producterId
     * @return
     */
    Producter getOne(Long producterId);

    /**
     * 添加
     *
     * @param producterDto
     * @return
     */
    int addProducter(ProducterDto producterDto);

    /**
     * 修改
     *
     * @param producterDto
     * @return
     */
    int updateProducter(ProducterDto producterDto);

    /**
     * 根据ID删除
     *
     * @param producterIds
     * @return
     */
    int deleteProducterByIds(Long[] producterIds);

    /**
     * 查询所有可用的生产厂家
     */
    List<Producter> selectAllProducter();
}
