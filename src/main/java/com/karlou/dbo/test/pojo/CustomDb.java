package com.karlou.dbo.test.pojo;

import com.karlou.dbo.annotation.FieldAttribute;
import com.karlou.dbo.annotation.TableAttribute;
import com.karlou.dbo.enums.IndexesTypeEnum;
import lombok.Data;

@Data
@TableAttribute(name = "k_mzc_test", datasource = "karlou")
public class CustomDb {
    @FieldAttribute(iskey = true)
    private String db_alias;
    @FieldAttribute(name = "db_url", iskey = true)
    private String dburl;
    @FieldAttribute(iskey = true, isautoIncr = true, indexesType = IndexesTypeEnum.NORMAL)
    private int id;
    @FieldAttribute(indexesType = IndexesTypeEnum.UNIQUE)
    private String db_user;
    @FieldAttribute(name = "dbpwd")
    private String db_pwd;
    @FieldAttribute()
    private Double db_wait_time;

    public CustomDb() {
    }

    public CustomDb(String db_alias, String dburl, int id, String db_user, String db_pwd, Double db_wait_time) {
        this.db_alias = db_alias;
        this.dburl = dburl;
        this.id = id;
        this.db_user = db_user;
        this.db_pwd = db_pwd;
        this.db_wait_time = db_wait_time;
    }
}
