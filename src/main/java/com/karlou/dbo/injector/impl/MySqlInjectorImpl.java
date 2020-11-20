package com.karlou.dbo.injector.impl;

import com.karlou.dbo.annotation.FieldAttribute;
import com.karlou.dbo.annotation.TableAttribute;
import com.karlou.dbo.base.Constants;
import com.karlou.dbo.enums.SqlMethodEnum;
import com.karlou.dbo.enums.SqlRelationEnum;
import com.karlou.dbo.injector.SqlInjector;
import com.karlou.dbo.util.SqlScriptUtil;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.karlou.dbo.base.Constants.*;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/25 08:57
 */
public class MySqlInjectorImpl implements SqlInjector {
    @Override
    public void selectClassAll(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class c) {
        SqlMethodEnum classAll = SqlMethodEnum.SELECT_CLASS_ALL;
        String sql = String.format(classAll.getSql(),
                getFieldSql(c),
                getTableName(c));
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, sql, mapperType);
        assistant.addMappedStatement(classAll.getMethod(), sqlSource, StatementType.STATEMENT, SqlCommandType.SELECT,
                null, null, null, null, null, c,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void selectEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum selectEntryKey = SqlMethodEnum.SELECT_ENTRY_KEY;
        String sql = String.format(selectEntryKey.getSql(),
                getFieldSql(entityClass),
                getTableName(entityClass),
                getKeySql(entityClass)
        );
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, sql, mapperType);
        assistant.addMappedStatement(selectEntryKey.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
                null, null, null, entityClass, null, entityClass,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void selectClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum selectClassDyn = SqlMethodEnum.SELECT_CLASS_DYN;
        String sql = String.format(selectClassDyn.getSql(),
                getFieldSql(entityClass),
                getTableName(entityClass),
                getDynSql());
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, sql, mapperType);
        assistant.addMappedStatement(selectClassDyn.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.SELECT,
                null, null, null, null, null, entityClass,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void selectClassDynPage(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {

    }

    @Override
    public void selectClassAllPage(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {

    }

    @Override
    public void deleteClassAll(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum deleteClassAll = SqlMethodEnum.DELETE_CLASS_ALL;
        String deleteSql = String.format(deleteClassAll.getSql(),
                getTableName(entityClass));
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, deleteSql, mapperType);
        assistant.addMappedStatement(deleteClassAll.getMethod(), sqlSource, StatementType.STATEMENT, SqlCommandType.DELETE,
                null, null, null, null, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void deleteEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum deleteEntryKey = SqlMethodEnum.DELETE_ENTRY_KEY;
        String deleteSql = String.format(deleteEntryKey.getSql(),
                getTableName(entityClass),
                getKeySql(entityClass));
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, deleteSql, mapperType);
        assistant.addMappedStatement(deleteEntryKey.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.DELETE,
                null, null, null, entityClass, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void deleteClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum deleteClassDyn = SqlMethodEnum.DELETE_CLASS_DYN;
        String deleteSql = String.format(deleteClassDyn.getSql(),
                getTableName(entityClass),
                getDynSql());
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, deleteSql, mapperType);
        assistant.addMappedStatement(deleteClassDyn.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.DELETE,
                null, null, null, null, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void updateEntryKey(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum updateEntryKey = SqlMethodEnum.UPDATE_ENTRY_KEY;
        String updateSql = String.format(updateEntryKey.getSql(),
                getTableName(entityClass),
                getSetSql(entityClass),
                getKeySql(entityClass)
        );
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, updateSql, mapperType);
        assistant.addMappedStatement(updateEntryKey.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.UPDATE,
                null, null, null, entityClass, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void updateClassDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum updateClassDyn = SqlMethodEnum.UPDATE_CLASS_DYN;
        String updateSql = String.format(updateClassDyn.getSql(),
                getTableName(entityClass),
                getSetSql(entityClass),
                getDynSql()
        );
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, updateSql, mapperType);
        assistant.addMappedStatement(updateClassDyn.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.UPDATE,
                null, null, null, null, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void insertEntry(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum insertEntry = SqlMethodEnum.INSERT_ENTRY;
        String insertSql = String.format(insertEntry.getSql(),
                getTableName(entityClass),
                getInsFiledSql(entityClass),
                getValuesSql(entityClass)
        );
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, insertSql, mapperType);
        assistant.addMappedStatement(insertEntry.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.INSERT,
                null, null, null, entityClass, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);

    }

    @Override
    public void insertListEntry(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum insertListEntry = SqlMethodEnum.INSERT_LIST_ENTRY;
        String insertSql = String.format(insertListEntry.getSql(),
                getTableName(entityClass),
                getInsAllFiledSql(entityClass),
                getAllValuesSql(entityClass));
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, insertSql, mapperType);
        assistant.addMappedStatement(insertListEntry.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.INSERT,
                null, null, null, null, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }

    @Override
    public void insertEntryDyn(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        SqlMethodEnum insertEntryDyn = SqlMethodEnum.INSERT_ENTRY_DYN;
        String insertSql = String.format(insertEntryDyn.getSql(),
                getTableName(entityClass),
                getInsFieldDynSql(),
                getValuesDynSql()
        );
        SqlSource sqlSource = configuration.getDefaultScriptingLanguageInstance().createSqlSource(configuration, insertSql, mapperType);
        assistant.addMappedStatement(insertEntryDyn.getMethod(), sqlSource, StatementType.PREPARED, SqlCommandType.INSERT,
                null, null, null, null, null, Integer.class,
                null, false, false, false, new NoKeyGenerator(), null, "",
                configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), null);
    }


    @Override
    public void injector(Configuration configuration, MapperBuilderAssistant assistant, Class mapperType, Class entityClass) {
        //----查询所有数据
        selectClassAll(configuration, assistant, mapperType, entityClass);
        //----根据对象主键查询数据
        selectEntryKey(configuration, assistant, mapperType, entityClass);
        //----自定义条件查询数据
        selectClassDyn(configuration, assistant, mapperType, entityClass);

        //----删除当前表所有数据
        deleteClassAll(configuration, assistant, mapperType, entityClass);
        //----根据实体删除当前数据
        deleteEntryKey(configuration, assistant, mapperType, entityClass);
        //----自定义删除数据
        deleteClassDyn(configuration, assistant, mapperType, entityClass);
        //----根据当前entry主键 更新指定数据
        updateEntryKey(configuration, assistant, mapperType, entityClass);
        //----根据当前class 及 自定义条件 更新指定数据
        updateClassDyn(configuration, assistant, mapperType, entityClass);
        //----根据entry  插入指定数据
        insertEntry(configuration, assistant, mapperType, entityClass);
        //----根据List<entry> 插入批量数据
        insertListEntry(configuration, assistant, mapperType, entityClass);
        //----根据自定义条件插入数据
        insertEntryDyn(configuration, assistant, mapperType, entityClass);
    }

    private void addDynMappedStatement(String methodId, String sql, Configuration configuration, Class type) {

    }

    private String getTableName(Class c) {
        TableAttribute tableAttribute = (TableAttribute) c.getAnnotation(TableAttribute.class);
        String tableName = StringUtils.isEmpty(tableAttribute.datasource()) ? tableAttribute.name() : tableAttribute.datasource() + "." + tableAttribute.name();
        return tableName;
    }

    private String getKeySql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null
                &&
                d.getAnnotation(FieldAttribute.class).iskey()).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = (StringUtils.isEmpty(fieldAttribute.name()) ? f.getName() : fieldAttribute.name()) +
                    EQ + HASH_LEFT_BRACE + ENTITY + DOT + f.getName() + RIGHT_BRACE + CONJ;
            String ifTest = ENTITY + DOT + f.getName() + NOEQUALS + EMPTY + CONJ + ENTITY + DOT + f.getName() + NOEQUALS + "''";
            String convertIf = SqlScriptUtil.convertIf(sqlScript, ifTest, true);
            buffer.append(convertIf);
        });
        if (buffer.length() == 0)
            return NOFIELD;
        else return SqlScriptUtil.convertTrim(buffer.toString(), "", "", "", CONJ);
    }

    private String getDynSql() {
        String sqlScript = SqlScriptUtil.convertChoose("v == null", " ${k} IS NULL ",
                " ${k} = #{v} ");
        sqlScript = SqlScriptUtil.convertForeach(sqlScript, Constants.COLUMN_MAP, "k", "v", "AND");
        sqlScript = SqlScriptUtil.convertWhere(sqlScript);
        sqlScript = SqlScriptUtil.convertIf(sqlScript, String.format("%s != null and !%s", Constants.COLUMN_MAP,
                Constants.COLUMN_MAP_IS_EMPTY), true);
        return sqlScript;
    }

    private String getSetSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = (StringUtils.isEmpty(fieldAttribute.name()) ? f.getName() : fieldAttribute.name()) +
                    EQ + HASH_LEFT_BRACE + ENTITY + DOT + f.getName() + RIGHT_BRACE + ",";
            String ifTest = ENTITY + DOT + f.getName() + NOEQUALS + EMPTY + CONJ + ENTITY + DOT + f.getName() + NOEQUALS + "''";
            String convertIf = SqlScriptUtil.convertIf(sqlScript, ifTest, true);
            buffer.append(convertIf);
        });
        if (buffer.length() == 0)
            return NOFIELD;
        else return SqlScriptUtil.convertSet(SqlScriptUtil.convertTrim(buffer.toString(), "", "", "", ","));
    }

    private String getFieldSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            buffer.append(StringUtils.isEmpty(fieldAttribute.name()) ? f.getName() : fieldAttribute.name())
                    .append(PREP)
                    .append(f.getName())
                    .append(",");
        });
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    /**
     * @return
     */
    private String getInsFieldDynSql() {
        String sqlScript = "${k}";
        sqlScript = SqlScriptUtil.convertForeach(sqlScript, Constants.COLUMN_MAP, "k", "v", ",");
        sqlScript = SqlScriptUtil.convertTrim(sqlScript, "(", ")", "", ",");
        sqlScript = SqlScriptUtil.convertIf(sqlScript, String.format("%s != null and !%s", Constants.COLUMN_MAP,
                Constants.COLUMN_MAP_IS_EMPTY), true);
        return sqlScript;
    }

    private String getInsFiledSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null
        ).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = (StringUtils.isEmpty(fieldAttribute.name()) ? f.getName() : fieldAttribute.name()) + ",";
            String ifTest = ENTITY + DOT + f.getName() + NOEQUALS + EMPTY + CONJ + ENTITY + DOT + f.getName() + NOEQUALS + "''";
            String convertIf = SqlScriptUtil.convertIf(sqlScript, ifTest, true);
            buffer.append(convertIf);
        });
        return SqlScriptUtil.convertTrim(buffer.toString(), "(", ")", "", ",");
    }

    private String getInsAllFiledSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = (StringUtils.isEmpty(fieldAttribute.name()) ? f.getName() : fieldAttribute.name()) + ",";
            buffer.append(sqlScript);
        });
        return SqlScriptUtil.convertTrim(buffer.toString(), "(", ")", "", ",");
    }

    private String getValuesSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = HASH_LEFT_BRACE + ENTITY + DOT + f.getName() + RIGHT_BRACE + ",";
            String ifTest = ENTITY + DOT + f.getName() + NOEQUALS + EMPTY + CONJ + ENTITY + DOT + f.getName() + NOEQUALS + "''";
            String convertIf = SqlScriptUtil.convertIf(sqlScript, ifTest, true);
            buffer.append(convertIf);
        });
        if (buffer.length() == 0)
            return NOFIELD;
        else return SqlScriptUtil.convertTrim(buffer.toString(), "(", ")", "", ",");
    }

    private String getValuesDynSql() {
        String sqlScript = "#{v}";
        sqlScript = SqlScriptUtil.convertForeach(sqlScript, Constants.COLUMN_MAP, "k", "v", ",");
        sqlScript = SqlScriptUtil.convertIf(sqlScript, String.format("%s != null and !%s", Constants.COLUMN_MAP,
                Constants.COLUMN_MAP_IS_EMPTY), true);
        sqlScript = SqlScriptUtil.convertTrim(sqlScript, "(", ")", "", ",");
        return sqlScript;
    }

    private String getAllValuesSql(Class c) {
        Field[] declaredFields = c.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();
        Arrays.stream(declaredFields).filter(d -> d.getAnnotation(FieldAttribute.class) != null).forEach(f -> {
            FieldAttribute fieldAttribute = (FieldAttribute) f.getAnnotation(FieldAttribute.class);
            String sqlScript = HASH_LEFT_BRACE + ENTITY + DOT + f.getName() + RIGHT_BRACE + ",";
            String whenTest = ENTITY + DOT + f.getName() + NOEQUALS + EMPTY + CONJ + ENTITY + DOT + f.getName() + NOEQUALS + "''";
            String convertWhen = SqlScriptUtil.convertWhen(whenTest, sqlScript);
            String convertOtherwise = SqlScriptUtil.convertOtherwise("null,");
            String convertChoose = SqlScriptUtil.convertChoose(convertWhen + convertOtherwise);
            buffer.append(convertChoose);
        });
        String convertTrim = SqlScriptUtil.convertTrim(buffer.toString(), "(", ")", "", ",");
        return SqlScriptUtil.convertForeach(convertTrim, "list", "i", "et", ",");
    }
}
