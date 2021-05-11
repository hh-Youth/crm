package com.pnode.crm.workbench.service;

import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.Clue;
import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);


    boolean convert(String clueId, Tran t, String createBy);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean deleteList(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue c);

    PaginationVO<Contacts> contactspageList(Map<String, Object> map);

    boolean contactsave(Contacts c);
}
