package com.pnode.crm.workbench.service;

import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Tran;
import com.pnode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;


public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();

    List<Contacts> getContactsListByNames(String aname);

    PaginationVO<Tran> pageList(Map<String, Object> map);

    boolean update(Tran t, String customerName);

    boolean delete(String[] ids);
}
