package com.pnode.crm.settings.dao;

import com.pnode.crm.settings.domain.Admit;
import com.pnode.crm.settings.domain.User;
import com.pnode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User login(Map<String, String> map);

    List<User> getUserList();

    int getUserTotalByCondition(Map<String, Object> map);

    List<User> getUserListByCondition(Map<String, Object> map);

    int save(User user);

    int delete(String[] ids);

    User detail(String id);

    int update(User user);

    Admit admitLogin(Map<String, String> map);

    User getById(String id);


    int updatePwd(Map<String, String> map);

    void updatePwdAdmit(Map<String, String> map);
}
