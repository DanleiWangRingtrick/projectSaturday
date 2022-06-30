package de.ls5.wt2.enums;

/**
 * @Description: 用户角色枚举
 * @Date: 2022/6/30
 */
public enum UserRoleEnum {

    ADMIN(1, "管理员"),
    USER(2, "用户");

    /**
     * 角色Id
     */
    public Integer id;

    /**
     * 角色名称
     */
    public String name;

    UserRoleEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
