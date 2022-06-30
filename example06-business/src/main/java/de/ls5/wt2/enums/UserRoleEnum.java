package de.ls5.wt2.enums;

/**
 * @Description: 用户角色枚举
 * @Date: 2022/6/30
 */
public enum UserRoleEnum {

    ADMIN("admin", "管理员"),
    USER("user", "用户");

    /**
     * 角色编码
     */
    public String code;

    /**
     * 角色名称
     */
    public String name;

    UserRoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
