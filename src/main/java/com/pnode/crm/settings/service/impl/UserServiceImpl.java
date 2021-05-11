package com.pnode.crm.settings.service.impl;


import com.pnode.crm.exception.LoginException;
import com.pnode.crm.settings.dao.UserDao;
import com.pnode.crm.settings.domain.Admit;
import com.pnode.crm.settings.domain.User;
import com.pnode.crm.settings.service.UserService;
import com.pnode.crm.utils.DateTimeUtil;
import com.pnode.crm.utils.SqlSessionUtil;
import com.pnode.crm.vo.PaginationVO;
import com.pnode.crm.workbench.domain.Activity;
import com.pnode.crm.workbench.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UserServiceImpl implements UserService {

    public UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if(user==null){

            throw new LoginException("账号密码错误");

        }

        //如果程序能够成功的执行到该行，说明账号密码正确
        //需要继续向下验证其他3项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){

            throw new LoginException("账号已失效");

        }

        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){

            throw new LoginException("账号已锁定");

        }

        //判断ip地址
        String allowIps = user.getAllowIps();

        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限");

        }


        return user;
    }

    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }

    public PaginationVO<User> pageList(Map<String, Object> map) {
        //取得total
        int total = userDao.getUserTotalByCondition(map);
        //取得dataList
        List<User> dataList = userDao.getUserListByCondition(map);

        for (int i=0 ;i<dataList.size();i++) {
            if ("1".equals(dataList.get(i).getLockState())){
                dataList.get(i).setLockState("启动");
            }else {
                dataList.get(i).setLockState("锁定");
            }
        }

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<User> vo = new PaginationVO<User>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public boolean save(User user) {
        boolean flag = true;

        int count = userDao.save(user);
        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;

        //删除用户
        int count3 = userDao.delete(ids);
        if(count3<1){

            flag = false;

        }

        return flag;
    }

    public User detail(String id) {
        User user = userDao.detail(id);
        if ("1".equals(user.getLockState())){
            user.setLockState("启动");
        }else {
            user.setLockState("锁定");
        }
        return user;
    }

    public boolean update(User user) {
        boolean flag = true;
        if ("启动".equals(user.getLockState())){
            user.setLockState("1");
        }else {
            user.setLockState("0");
        }
        //修改用户
        int count = userDao.update(user);
        if(count<1){

            flag = false;

        }

        return flag;
    }

    public Admit admitLogin(String loginAct, String loginPwd) throws LoginException{
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        Admit user = userDao.admitLogin(map);

        if(user==null){

            throw new LoginException("账号密码错误");

        }




        return user;
    }

    public boolean updatePwd(Map<String, String> map) {

        System.out.println(map.toString());
        boolean flag = true;

        String id = map.get("id");

        //如果是管理员就修改管理员  yes
        //如果是普通用户就修改普通用户  no
        if (map.get("admit").equals("yes")){
           userDao.updatePwdAdmit(map);
        }else {
            //先验证旧密码是否正确
            User user = userDao.getById(id);
            user.toString();
            Map<String,String> map1 = new HashMap<String, String>();
            map1.put("id",map.get("id"));
            map1.put("newPwd",map.get("newPwd"));

            if (map.get("newPwd")!=null && map.get("confirmPwd")!=null && map.get("oldPwd")!=null){


                if(user.getLoginPwd().equals(map.get("oldPwd")) && map.get("newPwd").equals(map.get("confirmPwd"))){

                    //旧密码正确 且 确认密码相同----则直接更改为新的密码
                    int count = userDao.updatePwd(map1);
                    if (count==1){
                        flag=true;
                    }
                }else {
                    flag = false;
                }
            }else {
                flag = false;
            }

        }



        return flag;
    }
}
