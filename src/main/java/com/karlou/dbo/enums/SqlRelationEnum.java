package com.karlou.dbo.enums;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/14 11:16
 */
public enum SqlRelationEnum {
    EQUAL("="),
    IN("IN"),
    NOTIN("NOT IN"),
    UNEQUAL("!="),
    LIKE("LIKE"),
    NOTLIKE("NOT LIKE"),
    GREATER(">"),
    LESS("<"),
    BETWEEN("BETWEEN");
    private String name;

    SqlRelationEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
