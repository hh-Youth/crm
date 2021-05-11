package com.pnode.crm.workbench.dao;

import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);

    int getCustomerTotalByCondition(Map<String, Object> map);

    List<Customer> getCustomerListByCondition(Map<String, Object> map);

    int customersave(Customer c);

    Customer getById(String id);

    int update(Customer c);


    int delete(String[] ids);

    Customer detail(String id);
}
