package com.pnode.crm.settings.domain;

public class Admit {
    private String id;
    private String name;
    private String loginAct;
    private String loginPwd;
    private String admit;
    private String email = "1453631@qq.com";
    private String expireTime = "2021-09-09";
    private String allowIps = "127.0.0.1";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getAdmit() {
        return admit;
    }

    public void setAdmit(String admit) {
        this.admit = admit;
    }
}
