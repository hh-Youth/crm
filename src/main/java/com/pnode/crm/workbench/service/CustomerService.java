package com.pnode.crm.workbench.service;

import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public interface CustomerService {
    List<String> getCustomerName(String name);

    PaginationVO<Customer> customerpageList(Map<String, Object> map);

    boolean customersave(Customer c);

    Map<String, Object> getUserListAndActivity(String id);

    boolean customerupdate(Customer c);

    Map<String, Object> getUserListAndContacts(String id);

    boolean contactUpdate(Contacts c);

    boolean deleteCustomerList(String[] ids);

    boolean deleteContactsList(String[] ids);

    Customer customerDetail(String id);

    Contacts contactsDetail(String id);
}
