package com.lite.blackdream.business.parameter.user;

import com.lite.blackdream.framework.model.Request;

/**
 * @author LaineyC
 */
public class UserCreateRequest extends Request {

    private String userName;

    private Boolean isDeveloper;
    
    public UserCreateRequest(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(Boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }
}