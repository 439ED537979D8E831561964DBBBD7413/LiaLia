package cn.chono.yopper.Service.Http.UserSync;

import cn.chono.yopper.Service.Http.ParameterBean;

/**
 * Created by zxb on 2015/11/20.
 */
public class UserSyncBean extends ParameterBean {

    private int userid;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}