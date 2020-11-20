package com.karlou.dbo.enums;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/10 16:17
 */
public enum IndexesTypeEnum {
    //不需要索引
    NOTINDEXES("NOTINDEXES"),
    //唯一索引
    UNIQUE("UNIQUE"),
    //全文索引
    FULLTEXT("FULLTEXT"),
    //普通索引
    NORMAL(""),
    //组合索引
//    GROUPINDEX("GROUPINDEX")
    ;

    IndexesTypeEnum(String name) {
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
