package com.hanwei.service;

import com.hanwei.dto.InventoryLogDto;
import com.hanwei.vo.DataGridView;

public interface InventoryLogService {
    /**
     * 分页查询
     *
     * @param inventoryLogDto
     * @return
     */
    DataGridView listInventoryLogPage(InventoryLogDto inventoryLogDto);
}
