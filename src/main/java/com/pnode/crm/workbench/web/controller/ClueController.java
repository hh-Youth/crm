package com.pnode.crm.workbench.web.controller;

import com.pnode.crm.settings.domain.User;
import com.pnode.crm.settings.service.UserService;
import com.pnode.crm.settings.service.impl.UserServiceImpl;
import com.pnode.crm.utils.DateTimeUtil;
import com.pnode.crm.utils.PrintJson;
import com.pnode.crm.utils.ServiceFactory;
import com.pnode.crm.utils.UUIDUtil;
import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.*;
import com.pnode.crm.workbench.service.ActivityService;
import com.pnode.crm.workbench.service.ClueService;
import com.pnode.crm.workbench.service.CustomerService;
import com.pnode.crm.workbench.service.impl.ActivityServiceImp;
import com.pnode.crm.workbench.service.impl.ClueServiceImpl;
import com.pnode.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if("/workbench/clue/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/clue/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(request,response);

        }else if("/workbench/clue/unbund.do".equals(path)){

            unbund(request,response);

        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){

            getActivityListByNameAndNotByClueId(request,response);

        }else if("/workbench/clue/bund.do".equals(path)){

            bund(request,response);

        }else if("/workbench/clue/getActivityListByName.do".equals(path)){

            getActivityListByName(request,response);

        }else if("/workbench/clue/convert.do".equals(path)){

            convert(request,response);

        }else if ("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/clue/delete.do".equals(path)){
            delete(request,response);
        }
        else if ("/workbench/clue/getUserListAndClue.do".equals(path)){
            getUserListAndClue(request,response);
        }else if ("/workbench/clue/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/contacts/pageList.do".equals(path)){
            contactspageList(request,response);
        }else if ("/workbench/contacts/save.do".equals(path)){
             contactsave(request,response);
        }else if ("/workbench/contacts/getUserListAndContacts.do".equals(path)){
            getUserListAndContacts(request,response);
        }else if ("/workbench/contacts/update.do".equals(path)){
            contactUpdate(request,response);
        }else if ("/workbench/contacts/delete.do".equals(path)){
            contactsdelete(request,response);
        }else if ("/workbench/contacts/detail.do".equals(path)){
            contactsDetail(request,response);
        }
        else if ("/workbench/customer/pageList.do".equals(path)){
            customerpageList(request,response);
        }else if ("/workbench/customer/save.do".equals(path)){
            customersave(request,response);
        }else if ("/workbench/customer/getUserListAndCustomer.do".equals(path)){
            getUserListAndCustomer(request,response);
        }else if ("/workbench/customer/update.do".equals(path)){
            customerUpdate(request,response);
        }else if ("/workbench/customer/delete.do".equals(path)){
            customerdelete(request,response);
        }else if ("/workbench/customer/detail.do".equals(path)){
            customerDetail(request,response);
        }

    }

    private void customerDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到客户详细信息页");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Customer c = cs.customerDetail(id);
        request.setAttribute("customer", c);
        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request, response);

    }

    private void contactsDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到联系人详细信息页");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Contacts c = cs.contactsDetail(id);
        request.setAttribute("contacts", c);
        request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request, response);

    }

    private void customerdelete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行客户的删除操作");

        String ids[] = request.getParameterValues("id");

        CustomerService as = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = as.deleteCustomerList(ids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void contactsdelete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行联系人的删除操作");

        String ids[] = request.getParameterValues("id");

        CustomerService as = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = as.deleteContactsList(ids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void contactUpdate(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行联系人修改操作");

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String birth = request.getParameter("birth");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String source = request.getParameter("source");
        String customerName = request.getParameter("customerName");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setAddress(address);
        c.setSource(source);
        c.setMphone(mphone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setCustomerId(customerName);
        c.setAppellation(appellation);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setBirth(birth);
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.contactUpdate(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndContacts(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        CustomerService as = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        /*

            总结：
                controller调用service的方法，返回值应该是什么
                你得想一想前端要什么，就要从service层取什么

            前端需要的，管业务层去要
            uList
            a

            以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
            map

         */
        Map<String,Object> map = as.getUserListAndContacts(id);

        PrintJson.printJsonObj(response, map);

    }

    private void customerUpdate(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行客户修改操作");

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String contactSummary = request.getParameter("contactSummary");
        String description = request.getParameter("description");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Customer c = new Customer();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setDescription(description);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setContactSummary(contactSummary);
        c.setName(name);
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.customerupdate(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndCustomer(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        CustomerService as = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        /*

            总结：
                controller调用service的方法，返回值应该是什么
                你得想一想前端要什么，就要从service层取什么

            前端需要的，管业务层去要
            uList
            a

            以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
            map

         */
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);

    }

    private void customersave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行客户添加操作");

        String id = UUIDUtil.getUUID();
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address1");

        Customer c = new Customer();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setName(name);
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.customersave(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void contactsave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行联系人添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String birth = request.getParameter("birth");
        String source = request.getParameter("source");
        String customerName = request.getParameter("customerName");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setAddress(address);
        c.setSource(source);
        c.setMphone(mphone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setCustomerId(customerName);
        c.setAppellation(appellation);
        c.setBirth(birth);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.contactsave(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void customerpageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到客户信息列表的操作（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        CustomerService as = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        PaginationVO<Customer> vo = as.customerpageList(map);

        PrintJson.printJsonObj(response, vo);
    }

    private void contactspageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到联系人信息列表的操作（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String custname = request.getParameter("custname");
        String source = request.getParameter("source");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("custname",custname);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ClueService as = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PaginationVO<Contacts> vo = as.contactspageList(map);

        PrintJson.printJsonObj(response, vo);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索修改操作");

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);



        ClueService as = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = as.update(c);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据线索id查询单条记录的操作");

        String id = request.getParameter("id");

        ClueService as = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*

            总结：
                controller调用service的方法，返回值应该是什么
                你得想一想前端要什么，就要从service层取什么

            前端需要的，管业务层去要
            uList
            a

            以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
            map

         */
        Map<String,Object> map = as.getUserListAndClue(id);

        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索的删除操作");

        String ids[] = request.getParameterValues("id");

        ClueService as = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = as.deleteList(ids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到线索信息列表的操作（结合条件查询+分页查询）");

        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String source = request.getParameter("source");
        String pageNoStr = request.getParameter("pageNo");
        String state = request.getParameter("state");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("fullname", fullname);
        map.put("owner", owner);
        map.put("company",company);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("state",state);

        ClueService as = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*

            前端要： 市场活动信息列表
                    查询的总条数

                    业务层拿到了以上两项信息之后，如果做返回
                    map
                    map.put("dataList":dataList)
                    map.put("total":total)
                    PrintJSON map --> json
                    {"total":100,"dataList":[{市场活动1},{2},{3}]}


                    vo
                    PaginationVO<T>
                        private int total;
                        private List<T> dataList;

                    PaginationVO<Activity> vo = new PaginationVO<>;
                    vo.setTotal(total);
                    vo.setDataList(dataList);
                    PrintJSON vo --> json
                    {"total":100,"dataList":[{市场活动1},{2},{3}]}


                    将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来比较方便




         */
        PaginationVO<Clue> vo = as.pageList(map);

        //vo--> {"total":100,"dataList":[{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);



    }

    private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行线索转换的操作");

        String clueId = request.getParameter("clueId");

        //接收是否需要创建交易的标记
        String flag = request.getParameter("flag");

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = null;

        //如果需要创建交易
        if("a".equals(flag)){

            t = new Tran();

            //接收交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        /*

            为业务层传递的参数：

            1.必须传递的参数clueId，有了这个clueId之后我们才知道要转换哪条记录
            2.必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收的t也有可能是个null）

         */
        boolean flag1 = cs.convert(clueId,t,createBy);



        if(flag1){

            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");

        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联指定线索的列表）");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response, aList);



    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据线索id查询关联的市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> aList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到线索详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);
        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }

}




































