package com.karlou.dbo.enums;

/**
 * 排序类型枚举
 *
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/9 15:29
 */
public enum SortEnum {
    DESC("DESC"),//数据降序
    ASC("ASC");//生序

    SortEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
