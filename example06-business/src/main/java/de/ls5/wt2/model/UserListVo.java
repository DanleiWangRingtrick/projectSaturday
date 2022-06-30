package de.ls5.wt2.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "用户列表")
public class UserListVo {

    @ApiModelProperty(value = "可见用户集合")
    private List<String> usernameOn;

    @ApiModelProperty(value = "所有用户集合")
    private List<String> usernameAll;

    public List<String> getUsernameOn() {
        return usernameOn;
    }

    public void setUsernameOn(List<String> usernameOn) {
        this.usernameOn = usernameOn;
    }

    public List<String> getUsernameAll() {
        return usernameAll;
    }

    public void setUsernameAll(List<String> usernameAll) {
        this.usernameAll = usernameAll;
    }

}
