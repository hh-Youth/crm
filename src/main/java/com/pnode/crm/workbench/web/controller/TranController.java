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
import com.pnode.crm.workbench.service.TranService;
import com.pnode.crm.workbench.service.impl.ActivityServiceImp;
import com.pnode.crm.workbench.service.impl.ClueServiceImpl;
import com.pnode.crm.workbench.service.impl.CustomerServiceImpl;
import com.pnode.crm.workbench.service.impl.TranServiceImpl;
import com.pnode.crm.workbench.service.TranService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author 北京动力节点
 */
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = request.getServletPath();

        if("/workbench/transaction/add.do".equals(path)){

            add(request,response);

        }else if("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(request,response);

        }else if("/workbench/transaction/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/transaction/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){

            getHistoryListByTranId(request,response);

        }else if("/workbench/transaction/changeStage.do".equals(path)){

            changeStage(request,response);

        }else if("/workbench/transaction/getCharts.do".equals(path)){

            getCharts(request,response);

        }else if ("/workbench/tran/getContactsListByName.do".equals(path)){
            getContacts(request,response);
        }else if ("/workbench/tran/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/transaction/delete.do".equals(path)){
            deleteTran(request,response);
        }else if ("/workbench/transaction/edit.do".equals(path)){
            editTran(request,response);
        }else if ("/workbench/transaction/update.do".equals(path)){
            update(request,response);
        }

    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行更新交易的操作");
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName"); //此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setEditTime(editTime);
        t.setEditBy(editBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.update(t,customerName);
//        if (t.getActivityId()==null || t.getContactsId()==null || t.getCustomerId()==null || t.getContactSummary()==null || t.getDescription()==null){
//            flag=false;
//            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
//        }else {
//            flag=ts.update(t,customerName);
//        }
//
//        System.out.println(customerName);

        if(flag){

            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }
    }

    private void editTran(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到更新信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //处理可能性
        /*

            阶段 t
            阶段和可能性之间的对应关系 pMap

         */

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);


        t.setPossibility(possibility);

        request.setAttribute("t1", t);

        request.getRequestDispatcher("/workbench/transaction/edit.jsp").forward(request,response);

    }

    private void deleteTran(HttpServletRequest request, HttpServletResponse response) {
            String ids[] = request.getParameterValues("id");
            TranService as = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到交易信息列表的操作（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String custname = request.getParameter("custname");
        String source = request.getParameter("source");
        String state = request.getParameter("state");
        String contactsName = request.getParameter("contactsName");
        String type = request.getParameter("type");
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
        map.put("state",state);
        map.put("contactsName",contactsName);
        map.put("type",type);

//        Set<String> kes=map.keySet();
//        for (String k:kes
//             ) {
//            System.out.println(k+":"+map.get(k));
//        }


        TranService as = (TranService) ServiceFactory.getService(new TranServiceImpl());

        PaginationVO<Tran> vo = as.pageList(map);

        //vo--> {"total":100,"dataList":[{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

    }

    private void getContacts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询联系人列表（根据名称模糊查）");

        String cname = request.getParameter("cname");

        TranService cs = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<Contacts> cList = cs.getContactsListByNames(cname);

        PrintJson.printJsonObj(response, cList);
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段数量统计图表的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*

            业务层为我们返回
                total
                dataList

                通过map打包以上两项进行返回


         */
        Map<String,Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行改变阶段的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);


    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易id取得相应的历史列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList= ts.getHistoryListByTranId(tranId);

        //阶段和可能性之间的对应关系
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");

        //将交易历史列表遍历
        for(TranHistory th : thList){

            //根据每条交易历史，取出每一个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }


        PrintJson.printJsonObj(response, thList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //处理可能性
        /*

            阶段 t
            阶段和可能性之间的对应关系 pMap

         */

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);


        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行添加交易的操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName"); //此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);

        if(flag){

            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }


    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得 客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("进入到跳转到交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }


}




































