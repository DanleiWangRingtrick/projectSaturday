package de.ls5.wt2.model;


//@ApiModel(value = "用户注册入参模型")
public class AddUserDto {

//    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @ApiModelProperty(value = "密码", required = true)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
