package com.pnode.crm.settings.web.controller;

import com.pnode.crm.settings.domain.Admit;
import com.pnode.crm.settings.domain.User;
import com.pnode.crm.settings.service.UserService;
import com.pnode.crm.settings.service.impl.UserServiceImpl;
import com.pnode.crm.utils.*;
import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.Tran;
import com.pnode.crm.workbench.service.ActivityService;
import com.pnode.crm.workbench.service.TranService;
import com.pnode.crm.workbench.service.impl.ActivityServiceImp;
import com.pnode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进去控制器");
        String path=request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/.do".equals(path)){
        }else if("/workbench/user/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/user/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/user/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/user/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/user/update.do".equals(path)){
            update(request,response);
        }else if ("/settings/admit/login.do".equals(path)){
            admitLogin(request,response);
        }else if ("/workbench/user/updatePwd.do".equals(path)){
            updatePwd(request,response);
        }
    }

    private void updatePwd(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String id = request.getParameter("userId");
        String oldPwd = MD5Util.getMD5(request.getParameter("oldPwd"));
        String newPwd = MD5Util.getMD5(request.getParameter("newPwd"));
        String confirmPwd = MD5Util.getMD5(request.getParameter("confirmPwd"));
        String admit = request.getParameter("admit");
        System.out.println(admit);
        Map<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("oldPwd",oldPwd);
        map.put("newPwd",newPwd);
        map.put("confirmPwd",confirmPwd);
        map.put("admit",admit);

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = us.updatePwd(map);
        if (flag){
            //修改成功转发到登录页
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }else {
            response.getWriter().print("修改密码错误!");
        }

    }

    //管理员登录
    private void admitLogin(HttpServletRequest request, HttpServletResponse response) {
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        System.out.println("loginAct:"+loginAct+"-----loginPwd:"+loginPwd);
        //密码转密文
        loginPwd= MD5Util.getMD5(loginPwd);

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {

            Admit user = us.admitLogin(loginAct,loginPwd);

            request.getSession().setAttribute("user", user);

            //如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            //表示登录成功
               /*

                {"success":true}

             */
            PrintJson.printJsonFlag(response, true);

        }catch(Exception e){
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*

                {"success":true,"msg":?}

             */
            String msg = e.getMessage();
            /*

                我们现在作为contoller，需要为ajax请求提供多项信息

                可以有两种手段来处理：
                    （1）将多项信息打包成为map，将map解析为json串
                    （2）创建一个Vo
                            private boolean success;
                            private String msg;


                    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                    如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了


             */
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);


        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String loginAct = request.getParameter("loginAct");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String lockState = request.getParameter("lockState");
        String expireTime = request.getParameter("expireTime");
        String allowIps = request.getParameter("allowIps");
        String editTime = DateTimeUtil.getSysTime();

        User user = new User();
        user.setLockState(lockState);
        user.setAllowIps(allowIps);
        user.setEmail(email);
        user.setId(id);
        user.setLoginAct(loginAct);
        user.setName(name);
        user.setExpireTime(expireTime);
        user.setEditTime(editTime);
        UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = as.update(user);

        PrintJson.printJsonFlag(response, flag);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());
        System.out.println("----------"+id);
        User a = as.detail(id);
        request.setAttribute("us", a);

        request.getRequestDispatcher("/settings/qx/user/detail.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        String ids[] = request.getParameterValues("id");

        UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String loginAct = request.getParameter("loginAct");
        String name = request.getParameter("name");
        String loginPwd = request.getParameter("loginPwd");
        String email = request.getParameter("email");
        String lockState = request.getParameter("lockState");
        String expireTime = request.getParameter("expireTime");
        String allowIps = request.getParameter("allowIps");
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        //密码转密文
        loginPwd= MD5Util.getMD5(loginPwd);
        User user = new User();
        user.setLockState(lockState);
        user.setAllowIps(allowIps);
        user.setEmail(email);
        user.setId(id);
        user.setLoginAct(loginAct);
        user.setLoginPwd(loginPwd);
        user.setName(name);
        user.setExpireTime(expireTime);
        user.setCreateTime(createTime);
        UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());

        boolean flag = as.save(user);

        PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name =request.getParameter("name");
        String lockState=request.getParameter("lockState");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", name);
        map.put("lockState",lockState);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        UserService as = (UserService) ServiceFactory.getService(new UserServiceImpl());

        PaginationVO<User> vo = as.pageList(map);

        //vo--> {"total":100,"dataList":[{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

    }

    //普通用户登录
    private void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("进入到验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        System.out.println("loginAct:"+loginAct+"-----loginPwd:"+loginPwd);
        //密码转密文
        loginPwd= MD5Util.getMD5(loginPwd);
        System.out.println("密文loginPwd:"+loginPwd);
        //接收ip地址
        String  ip = request.getRemoteAddr();
        System.out.println("------ip:"+ip);

        //未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        System.out.println("aaaaaaa");
        try {

            User user = us.login(loginAct,loginPwd,ip);

            request.getSession().setAttribute("user", user);

            //如果程序执行到此处，说明业务层没有为controller抛出任何的异常
            //表示登录成功
               /*

                {"success":true}

             */
            PrintJson.printJsonFlag(response, true);

        }catch(Exception e){
            e.printStackTrace();

            //一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛出了异常
            //表示登录失败
            /*

                {"success":true,"msg":?}

             */
            String msg = e.getMessage();
            /*

                我们现在作为contoller，需要为ajax请求提供多项信息

                可以有两种手段来处理：
                    （1）将多项信息打包成为map，将map解析为json串
                    （2）创建一个Vo
                            private boolean success;
                            private String msg;


                    如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
                    如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了


             */
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);


        }


    }

}
