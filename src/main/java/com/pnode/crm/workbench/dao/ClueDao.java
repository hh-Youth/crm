package com.pnode.crm.workbench.dao;

import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.Clue;
import com.pnode.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    int update(Clue c);

    int deleteList(String[] ids);

    int getContactsTotalByCondition(Map<String, Object> map);

    List<Contacts> getContactsListByCondition(Map<String, Object> map);

    int contactsave(Contacts c);
}
