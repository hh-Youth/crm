package com.pnode.crm.settings.service;

import com.pnode.crm.exception.LoginException;
import com.pnode.crm.settings.domain.Admit;
import com.pnode.crm.settings.domain.User;
import com.pnode.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
    List<User> getUserList();

    PaginationVO<User> pageList(Map<String, Object> map);

    boolean save(User user);

    boolean delete(String[] ids);

    User detail(String id);

    boolean update(User user);

    Admit admitLogin(String loginAct, String loginPwd) throws LoginException;

    boolean updatePwd(Map<String, String> map);
}
