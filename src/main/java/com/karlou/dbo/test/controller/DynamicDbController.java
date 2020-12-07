package com.karlou.dbo.test.controller;

import com.karlou.dbo.enums.IndexesTypeEnum;
import com.karlou.dbo.test.mapper.CustomDbMapper;
import com.karlou.dbo.test.mapper.TesMapper;
import com.karlou.dbo.test.pojo.CustomDb;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class DynamicDbController {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDbController.class);
    //    @Autowired
//    com.karlou.dbo.test.dao.TestDao TestDao;
//    @Autowired
//    SqlSessionTemplate sqlSessionTemplate;
//    @GetMapping("/{name}/list")
//    public List<CustomDb> getCustomDb(@PathVariable(name = "name") String name){
//        Configuration configuration = sqlSessionTemplate.getConfiguration();
//
//        MapperRegistry mapperRegistry = configuration.getMapperRegistry();
//        mapperRegistry.addMapper(BaseMapper.class);
//        BaseMapper mapper = sqlSessionTemplate.getMapper(BaseMapper.class);
//
//        logger.info("目标数据源 :::  "+name);
//        if("infocarriers".equals(name)){
//            logger.info("执行方法 :::  selectAll()");
//            return TestDao.selectAll();
//        }else if("karlou".equals(name)){
//            logger.info("执行方法 :::  selectCustAll()");
//            List<Map> maps = TestDao.selectCustAll();
//            maps.stream().forEach(m -> m.size());
//            return new ArrayList<>();
//        }else if("dyinfocarriers".equals(name)){
//            logger.info("执行方法 :::  selectAllInfocarriers()");
//            return TestDao.selectAllInfocarriers();
//        }else{
//            logger.info("未执行任何内容。。。。");
//            return null;
//        }
//    }
//    @Autowired
//    BaseMapper baseMapper;
//    @Autowired
//    CustomDbMapper customDbMapper;
//    /**
//     * 创建表
//     * 根据类的class 创建数据库表
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     *
//     */
//    @PostMapping("/createTable1")
//    String getCreateSqlByClass(){
//        baseMapper.delete();
////        customDbMapper.insertList(null);
//        return "getCreateSqlByClass ok!";
//    }
//
//    /**
//     * 创建表
//     * 自定义BaseEntityProxy对象，生成数据库表
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     * 限制之前已经生成过相应的BaseEntityProxy 才可以使用
//     *
//     * @param tableName 表名
//     */
//    @PostMapping("/createTable2")
//    String getCreateSqlByTableName(String tableName){
//        BaseEntityProxy baseEntityProxy = new BaseEntityProxy();
//        baseEntityProxy.setTableName("k_test_proxy");
//        DbFieldProxy dbFieldProxy = DbFieldProxyFactory.getDbFieldProxy("String", "mzc");
//        DbFieldProxy dbFieldProxy1 = DbFieldProxyFactory.getDbFieldProxy("Double", "age");
//        Map<String,DbFieldProxy> map = new HashMap<>();
//        map.put("mzc_map",dbFieldProxy);
//        map.put("age_map",dbFieldProxy1);
//        baseEntityProxy.setDbFieldMap(map);
//        BasicsProvider.setEntityProxy(baseEntityProxy);
//        baseMapper.getCreateSqlByTableName("k_test_proxy");
//        return "getCreateSqlByTableName ok!";
//    }
//
//    /**
//     * 创建表
//     * 自定义BaseEntityProxy对象，生成数据库表
//     * 无限制
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     *
//     */
//    @PostMapping("/createTable3")
//    String getCreateSqlByProxy(){
//        BaseEntityProxy baseEntityProxy = new BaseEntityProxy();
//        baseEntityProxy.setTableName("k_test_proxy");
//        DbFieldProxy dbFieldProxy = DbFieldProxyFactory.getDbFieldProxy("String", "mzc");
//        DbFieldProxy dbFieldProxy1 = DbFieldProxyFactory.getDbFieldProxy("Double", "age");
//        Map<String,DbFieldProxy> map = new HashMap<>();
//        map.put("mzc_map",dbFieldProxy);
//        map.put("age_map",dbFieldProxy1);
//        baseEntityProxy.setDbFieldMap(map);
//        baseMapper.getCreateSqlByProxy(baseEntityProxy);
//        return "getCreateSqlByProxy ok!";
//    }
//
//    /**
//     * 删除表
//     * 根据表名删除表
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     *
//     */
//    @DeleteMapping("/dropTableByName")
//    String dropTableByName(){
//        baseMapper.dropTableByName("k_test_proxy");
//        return "dropTableByName ok";
//    }
//
//    /**
//     * 删除表
//     * 根据类的class 反射出相应的表名 删除指定表
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     *
//     */
//    @DeleteMapping("/dropTableByClass")
//    String dropTableByClass(){
//        baseMapper.dropTableByClass(CustomDb.class);
//        return "dropTableByClass OK";
//    }
//
//    /**
//     * 删除表
//     * 根据指定的BaseEntityProxy对象，删除指定表
//     * 可通过指定表名为datasource.tablename 指定创建某数据库的表
//     *
//     */
//    @DeleteMapping("/dropTableByProxy")
//    String dropTableByProxy(){
//        BaseEntityProxy baseEntityProxy = new BaseEntityProxy();
//        baseEntityProxy.setTableName("k_test_proxy");
//        baseMapper.dropTableByProxy(baseEntityProxy);
//        return "dropTableByProxy ok!";
//    }
//
//    /**
//     * 增加列
//     * 根据类的class反射表名，通过字段代理实例增加字段
//     *
//     */
//    @DeleteMapping("/addFieldByProxy")
//    String addFieldByProxy(){
//        DbFieldProxy dbFieldProxy = DbFieldProxyFactory.getDbFieldProxy("String", "addFieldByProxy");
//        baseMapper.addFieldByProxy(CustomDb.class,dbFieldProxy);
//        return "addFieldByProxy ok！";
//    }
//
//    /**
//     * 增加列
//     * 根据类的class 反射出表名，并根据相应参数增加字段
//     *
//     */
//    @DeleteMapping("/addFieldByClass")
//    String addFieldByClass(){
//        baseMapper.addFieldByClass(CustomDb.class, "qwe", "Integer", true, "addFieldByClass 方法增加");
//        return "addFieldByClass ok";
//    }
//
//    /**
//     * 增加列
//     * 根据相应参数增加指定表列
//     *
//     */
//    @DeleteMapping("/addFieldByArgs")
//    String addFieldByArgs(){
//        baseMapper.addFieldByArgs("k_test_proxy","asd","Date",true,"addFieldByArgs 方法增加");
//        return "addFieldByArgs ok";
//    }
//
//    /**
//     * 删除列
//     * 根据类的class 反射出表名，并根据相应参数删除对应的列
//     *
//     */
//    @DeleteMapping("/dropFieldByClass")
//    String dropFieldByClass(){
//        baseMapper.dropFieldByClass(CustomDb.class,"qwe");
//        return "dropFieldByClass ok!";
//    }
//
//    /**
//     * 删除列
//     * 根据相应参数，删除指定列
//     *
//     */
//    @DeleteMapping("/dropFieldByArgs")
//    String dropFieldByArgs(){
//        baseMapper.dropFieldByArgs("k_test_proxy","asd");
//        return "dropFieldByArgs ok";
//    }
//
//    /**
//     * 修改列
//     * 根据类的class反射表名，并根据字段代理对象删除指定列
//     *
//     */
//    @DeleteMapping("/modifyFieldByClass")
//    String modifyFieldByClass(){
//        DbFieldProxy fieldProxy = DbFieldProxyFactory.getDbFieldProxy("Integer", "db_pwd");
//        baseMapper.modifyFieldByClass(CustomDb.class,fieldProxy);
//        return "modifyFieldByClass ok";
//    }
//
//    /**
//     * 修改列
//     * 根据相应参数，修改指定列
//     *
//     */
//    @DeleteMapping("/modifyFieldByArgs")
//    String modifyFieldByArgs(){
//        baseMapper.modifyFieldByArgs("k_test_proxy","age_map","varchar(200)",false,"modifyFieldByArgs 方法修改");
//        return "modifyFieldByArgs ok";
//    }
//
//    /**
//     * 创建索引
//     * 根据类的class反射出表名，并根据字段代理对象创建索引
//     * 注意：该方法索引名称与列名称一致
//     *
//     */
//    @DeleteMapping("/indexesFieldByClass")
//    String indexesFieldByClass(){
//        DbFieldProxy fieldProxy = DbFieldProxyFactory.getDbFieldProxy("Integer", "db_pwd");
//        fieldProxy.setIndexesType(IndexesTypeEnum.UNIQUE);
//        baseMapper.indexesFieldByClass(CustomDb.class,fieldProxy);
//        return "indexesFieldByClass ok";
//    }
//
//    /**
//     * 创建索引
//     * 根据相应参数创建，相应的类型的索引
//     *
//     */
//    @DeleteMapping("/indexesFieldByArgs")
//    String indexesFieldByArgs(){
//        baseMapper.indexesFieldByArgs("k_test_proxy",IndexesTypeEnum.FULLTEXT,"pwd","pwd");
//        return "indexesFieldByArgs ok";
//    }
//
//    /**
//     * 删除索引
//     * 根据类的class反射出表名，并根据字段代理对象删除索引
//     *
//     */
//    @DeleteMapping("/dropIndexesByClass")
//    String dropIndexesByClass(){
//        DbFieldProxy fieldProxy = DbFieldProxyFactory.getDbFieldProxy("Integer", "db_pwd");
//        baseMapper.dropIndexesByClass(CustomDb.class,fieldProxy);
//        return "dropIndexesByClass ok";
//    }
//
//    /**
//     * 删除索引
//     * 根据相应的条件，删除相应字段的索引
//     *
//     * @return drop - sql
//     */
//    @DeleteMapping("/dropIndexesByArgs")
//    String dropIndexesByArgs(){
//        baseMapper.dropIndexesByArgs("k_test_proxy","age_map");
//        return "dropIndexesByArgs ok";
//    }
//
//    /**
//     * 插入数据
//     * 根据传入实例保存数据
//     *
//     * @return insert - sql
//     */
//    @PostMapping("getInsertAllSql")
//    String getInsertAllSql(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 2, "mzc", "mzc", 3600.0);
//        int insertAllSql = baseMapper.getInsertAllSql(customDb);
//        return "getInsertAllSql ok "+insertAllSql;
//    }
//
//    /**
//     * 插入数据
//     * 根据baseEntityProxy代理对象，保存实例数据
//     *
//     * @return insert - sql
//     */
//    @PostMapping("getInsertSql")
//    String getInsertSql(){
//        BaseEntityProxy entityProxy = BasicsProvider.getEntityProxy(CustomDb.class);
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 2400.0);
//        baseMapper.getInsertSql(entityProxy,customDb);
//        return "getInsertSql ok";
//    }
//
//    /**
//     * 插入数据
//     * 根据表名，自定义值插入数据
//     *
//     * @return insert - sql
//     */
//    @PostMapping("getInsertSqlByMap")
//    String getInsertSqlByMap(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("mzc_map","ceshi");
//        map.put("age_map","ceshi");
//        baseMapper.getInsertSqlByMap("k_test_proxy",map);
//        return "getInsertSqlByMap ok";
//    }
//
//    /**
//     * 插入数据
//     * 自定义insert-sql
//     *
//     * @return insert - sql
//     */
//    @PostMapping("insertSqlCustmon")
//    String insertSqlCustmon(){
//        baseMapper.insertSqlCustmon("insert into k_test_proxy(mzc_map,age_map) values(\'mzc\',\'cy\')");
//        return "insertSqlCustmon ok";
//    }
//
//    /**
//     * 删除数据
//     * 根据传入实例的主键删除对应数据
//     *
//     * @return delete - sql
//     */
//    @DeleteMapping("/deleteSql")
//    String deleteSql(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 2, "mzc", "mzc", 3600.0);
//        baseMapper.deleteSql(customDb);
//        return "deleteSql ok";
//    }
//
//    /**
//     * 删除数据
//     * 根据map中自定义的条件与值删除符合条件数据
//     * 限制： map key与数据库列名一致
//     *
//     * @return delete - sql
//     */
//    @DeleteMapping("/deleteSqlByDyn")
//    String deleteSqlByDyn(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("mzc_map","ceshi");
//        map.put("age_map","ceshi");
//        baseMapper.deleteSqlByDyn("k_test_proxy",map);
//        return "deleteSqlByDyn ok";
//    }
//
//    /**
//     * 删除数据
//     * 自定义删除sql语句，无条件返回交给mybatis执行
//     *
//     * @return delete - sql
//     */
//    @DeleteMapping("/deleteSqlCustmon")
//    String deleteSqlCustmon(){
//        baseMapper.deleteSqlCustmon("delete from k_test_proxy where mzc_map = \'mzc\' and age_map = \'cy\')");
//        return "deleteSqlCustmon ok";
//    }
//
//    /**
//     * 更新数据
//     * 根据实体更新对象
//     *
//     * @return upadte - sql
//     */
//    @PutMapping("/updateSql")
//    String updateSql(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 7600.0);
//        baseMapper.updateSql(customDb);
//        return "updateSql ok!";
//    }
//
//    /**
//     * 更新数据
//     * 根据自定义更新sql 执行数据库更新
//     *
//     * @return update - sql
//     */
//    @PutMapping("/updateSqlByDyn")
//    String updateSqlByDyn(){
//        baseMapper.updateSqlByDyn("update k_mzc_test set db_alias = \'hahaha\' where id = 5");
//        return "updateSqlByDyn ok";
//    }
//
//    /**
//     * 查询数据
//     * 根据传入实例主键查询数据
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectSql")
//    List<CustomDb> selectSql(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 7600.0);
//        List<CustomDb> customDbs = baseMapper.selectSql(customDb);
//        return customDbs;
//    }
//
//    /**
//     * 查询数据
//     * 根据传入实例，查询所在表的所有数据
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectAll")
//    List<CustomDb> selectAll(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 7600.0);
//        List<CustomDb> customDbs = baseMapper.selectSql(customDb);
//        return customDbs;
//    }
//
//    /**
//     * 查询数据
//     * 根据插入表名，查询当前表所有数据
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectAllSql")
//    Object selectAllSql(){
//        List<CustomDb> k_mzc_test = baseMapper.selectAllSql("k_mzc_test");
//        return k_mzc_test;
//    }
//
//
//    /**
//     * 查询语句
//     * 根据传入实例查询，并根据传入每页条数、传入页数、排序方式查询相应数据
//     * 如果实例中各个字段无对应值，则默认查询全表
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectSqlPage")
//    List<CustomDb> selectSqlPage(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 7600.0);
//        List<CustomDb> customDbs = baseMapper.selectSqlPage(customDb, 10, 1);
//        return customDbs;
//    }
//
//    /**
//     * 查询语句
//     * 根据传入实例查询，并根据传入每页条数、传入页数、排序方式查询相应数据
//     * 如果实例中各个字段无对应值，则默认查询全表
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectSqlPageDyn")
//    List<CustomDb> selectSqlPageDyn(){
//        List<CustomDb> customDbs = baseMapper.selectSqlPageDyn("select * from k_mzc_test ", 10, 1);
//        return customDbs;
//
//    }
//
//    /**
//     * 查询数据
//     * 根据传入实例的相应字段值，查询实例所在表的数据
//     *
//     * @return select - sql
//     */
//    @GetMapping("/selectPartSql")
//    List<CustomDb> selectPartSql(){
//        CustomDb customDb = new CustomDb("mzc", "jdbc:karlou", 5, "cy", "cy", 7600.0);
//        List<CustomDb> customDbs = baseMapper.selectPartSql(customDb);
//        return customDbs;
//    }
//
//    /**
//     * 查询数据
//     * 自定义查询sql进行数据查询
//     *
//     * @return select - sql
//     */
//    @GetMapping("/seleceSqlCustmon")
//    List<CustomDb> seleceSqlCustmon(){
//        List<CustomDb> customDbs = baseMapper.seleceSqlCustmon("select * from k_mzc_test");
//        return customDbs;
//    }
//
//    /**
//     * 查询数据
//     * 根据表面判断当前表是否存在
//     *
//     * @return select - sql
//     */
//    @GetMapping("/isExistTableName")
//    Integer isExistTableName(){
//        Integer k_mzc_test = baseMapper.isExistTableName("k_mzc_test22");
//        return k_mzc_test;
//    }
//
//    /**
//     * 查询数据
//     * 根据表面判断当前表是否存在
//     *
//     * @return select
//     */
//    @GetMapping("/isExistTable")
//    Integer isExistTable(){
//        CustomDb customDb = new CustomDb();
//        Integer existTable = baseMapper.isExistTable(customDb);
//        return existTable;
//    }
//    @Autowired
//    TesMapper tesMapper;
//
//    @GetMapping("/selectAll")
//    public List<CustomDb> testSelectAll() {
//        List<CustomDb> customDbs = tesMapper.selectAll();
//        return customDbs;
//    }
//
//    @GetMapping("/selectKey")
//    public CustomDb testSelectKey() {
//        CustomDb db = new CustomDb();
//        db.setDb_alias("mzc");
//        db.setDburl("jdbc:karlou");
//        db.setId(2);
//        CustomDb customDb = tesMapper.selectKey(db);
//        return customDb;
//    }
//
//    @GetMapping("/selectDyn")
//    public List<CustomDb> testSelectDny() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("db_alias", "mzc");
//        List<CustomDb> customDb = tesMapper.selectDyn(map);
//        return customDb;
//    }
//
//    @DeleteMapping("/deleteAll")
//    public int testdeleteAll() {
//        int i = tesMapper.deleteAll();
//        return i;
//    }
//
//    @DeleteMapping("/deleteKey")
//    public int testdeleteKey() {
//        CustomDb db = new CustomDb();
//        db.setDb_alias("mzc");
//        db.setDburl("jdbc:karlou");
//        db.setId(2);
//        int i = tesMapper.deleteKey(db);
//        return i;
//    }
//
//    @DeleteMapping("/deleteDyn")
//    public int testdeleteDyn() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("db_alias", "mzc");
//        int customDb = tesMapper.deleteDyn(map);
//        return customDb;
//    }
//    //----------开始更新----
//
//    /**
//     * 更新数据
//     * 根据当前entry主键 更新指定数据
//     */
//    @PutMapping("updateKey")
//    int updateKey() {
//        CustomDb customDb = testSelectKey();
//        customDb.setDb_pwd("wy shi xiao gou");
//        int i = tesMapper.updateKey(customDb);
//        return i;
//    }
//
//    ;
//
//    /**
//     * 更新数据
//     * 根据当前class 及 自定义条件 更新指定数据
//     */
//    @PutMapping("updateDyn")
//    int updateDyn() {
//        Map<String, Object> hashMap = new HashMap<>();
//        hashMap.put("db_alias", "mzc");
//        CustomDb db = new CustomDb();
//        db.setDb_wait_time(5000.0);
//        int i = tesMapper.updateDyn(db, hashMap);
//        return i;
//    }
//
//    ;
//
//    //----------开始插入----
//
//    /**
//     * 插入数据
//     * 根据entry  插入指定数据
//     */
//    @PostMapping("insertEnt")
//    int insertEnt() {
//        CustomDb db = new CustomDb();
//        db.setDb_wait_time(2102.0);
//        db.setDburl("karlou");
//        db.setId(8);
//        db.setDb_alias("karlou");
//        db.setDb_user("karlou");
//        db.setDb_pwd("zxca");
//        int i = tesMapper.insertEnt(db);
//        return i;
//    }
//
//    ;
//
//    /**
//     * 插入数据
//     * 根据List<entry> 插入批量数据
//     */
//    @PostMapping("insertList")
//    int insertList() {
//        List<CustomDb> list = new ArrayList<>();
//        for (int o = 10; o < 16; o++) {
//            CustomDb db = new CustomDb();
//            db.setDb_wait_time(2102.0 + o);
//            db.setDburl("karlou" + 0);
//            db.setId(0);
//            db.setDb_alias("karlou" + o);
//            db.setDb_user("karlou" + o);
//            db.setDb_pwd("zxca" + o);
//            list.add(db);
//        }
//        int i = tesMapper.insertList(list);
//        return 0;
//    }
//
//    ;
//
//    /**
//     * 插入数据
//     * 根据自定义条件插入数据
//     */
//    @PostMapping("insertDyn")
//    int insertDyn() {
//        Map<String, Object> hashMap = new HashMap<>();
//        hashMap.put("db_alias", "qiw");
//        hashMap.put("db_wait_time", 3044.0);
//        hashMap.put("db_user", "qiw");
//        hashMap.put("dbpwd", "qiw");
//        hashMap.put("id", 20);
//        hashMap.put("db_url", "12121");
//        int i = tesMapper.insertDyn(hashMap);
//        return i;
//    }
//

}
