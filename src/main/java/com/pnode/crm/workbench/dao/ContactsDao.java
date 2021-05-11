package com.pnode.crm.workbench.dao;

import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Customer;

import java.util.List;

public interface ContactsDao {

    int save(Contacts con);
    List<Contacts> getContactsListByNames(String cname);
    Contacts getById(String id);

    int update(Contacts c);

    int delete(String[] ids);

    int deleteByCustomerId(String[] ids);

    Contacts detail(String id);
}
