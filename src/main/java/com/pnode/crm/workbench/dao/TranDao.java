package com.pnode.crm.workbench.dao;

import com.pnode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranListByCondition(Map<String, Object> map);

    int update(Tran t);

    int delete(String[] ids);

    int deleteHistory(String[] ids);
}
