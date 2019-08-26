package com.phenix.fileshare.cmis.model;

import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;


public class UserModel {
    private String userName;
    private String userPwd;

    public UserModel(){
        userName = userPwd = "";
    }
    public UserModel(JSONObject object){
        userName = (String)object.get("userName");
        userPwd = (String)object.get("userPwd");
    }
    public UserModel(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
