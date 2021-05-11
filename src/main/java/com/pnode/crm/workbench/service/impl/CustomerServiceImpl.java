package com.pnode.crm.workbench.service.impl;

import com.pnode.crm.settings.dao.UserDao;
import com.pnode.crm.settings.domain.User;
import com.pnode.crm.utils.SqlSessionUtil;
import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.dao.ContactsDao;
import com.pnode.crm.workbench.dao.CustomerDao;
import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.Contacts;
import com.pnode.crm.workbench.domain.Customer;
import com.pnode.crm.workbench.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);



    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }

    public PaginationVO<Customer> customerpageList(Map<String, Object> map) {
        //取得total
        int total = customerDao.getCustomerTotalByCondition(map);

        //取得dataList
        List<Customer> dataList = customerDao.getCustomerListByCondition(map);
//        for (Customer customer : dataList) {
//            System.out.println(customer.toString());
//        }
        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Customer> vo = new PaginationVO<Customer>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public boolean customersave(Customer c) {
        boolean flag = true;
        int count = customerDao.save(c);

        if(count!=1){

            flag = false;

        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList
        List<User> uList = userDao.getUserList();

        //取a
        Customer a = customerDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("a", a);

        //返回map就可以了
        return map;
    }

    public boolean customerupdate(Customer c) {
        boolean flag = true;

        int count = customerDao.update(c);
        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public Map<String, Object> getUserListAndContacts(String id) {
        //取uList
        List<User> uList = userDao.getUserList();

        //取a
        Contacts a = contactsDao.getById(id);
        //通过客户id找到客户信息(名字)
        Customer customer=customerDao.getById(a.getCustomerId());
        if (customer!=null){
            a.setCustomerId(customer.getName());
        }
        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("a", a);

        //返回map就可以了
        return map;
    }

    public boolean contactUpdate(Contacts c) {
        boolean flag = true;
        //通过客户名找到客户id
        Customer cus = customerDao.getCustomerByName(c.getCustomerId());
        if (cus!=null){
            c.setCustomerId(cus.getId());
            //修改联系人信息
            int count = contactsDao.update(c);
            if(count!=1){

                flag = false;

            }
        }


        return flag;
    }

    public boolean deleteCustomerList(String[] ids) {
        boolean flag = true;
        //先删除联系人里面关联客户id外键的信息
        int count1 = contactsDao.deleteByCustomerId(ids);

        if(count1<1){

            flag = false;

        }

        //删除客户
        int count2 = customerDao.delete(ids);
        if(count2<1){

            flag = false;

        }

        return flag;
    }

    public boolean deleteContactsList(String[] ids) {
        boolean flag = true;

        //删除联系人
        int count = contactsDao.delete(ids);
        if(count!=ids.length){

            flag = false;

        }

        return flag;
    }

    public Customer customerDetail(String id) {
        Customer customer = customerDao.detail(id);

        return customer;
    }

    public Contacts contactsDetail(String id) {
        Contacts contacts = contactsDao.detail(id);
        Customer customer = customerDao.getById(contacts.getCustomerId());
        if (customer!=null){
            contacts.setCustomerId(customer.getName());
        }else {
            //假数据
            contacts.setCustomerId("阿里巴巴");
        }

        return contacts;
    }
}
















