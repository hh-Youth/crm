package com.pnode.crm.settings.dao;

import com.pnode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * Author 北京动力节点
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
