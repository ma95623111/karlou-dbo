/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.karlou.dbo.util;

import com.karlou.dbo.base.Constants;

/**
 * <p>
 * sql 脚本工具类
 * </p>
 *
 * @author miemie
 * @since 2018-08-15
 */
public final class SqlScriptUtil implements Constants {

    private SqlScriptUtil() {
        // ignore
    }

    /**
     * <p>
     * 获取 带 if 标签的脚本
     * </p>
     *
     * @param sqlScript sql 脚本片段
     * @return if 脚本
     */
    public static String convertIf(final String sqlScript, final String ifTest, boolean newLine) {
        String newSqlScript = sqlScript;
        if (newLine) {
            newSqlScript = NEWLINE + newSqlScript + NEWLINE;
        }
        return String.format(NEWLINE + "<if test=\"%s\">%s</if>" + NEWLINE, ifTest, newSqlScript);
    }

    /**
     * <p>
     * 获取 带 trim 标签的脚本
     * </p>
     *
     * @param sqlScript       sql 脚本片段
     * @param prefix          以...开头
     * @param suffix          以...结尾
     * @param prefixOverrides 干掉最前一个...
     * @param suffixOverrides 干掉最后一个...
     * @return trim 脚本
     */
    public static String convertTrim(final String sqlScript, final String prefix, final String suffix,
                                     final String prefixOverrides, final String suffixOverrides) {
        StringBuilder sb = new StringBuilder("<trim");
        if (KarlouUtil.isNotBlank(prefix)) {
            sb.append(" prefix=\"").append(prefix).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(suffix)) {
            sb.append(" suffix=\"").append(suffix).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(prefixOverrides)) {
            sb.append(" prefixOverrides=\"").append(prefixOverrides).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(suffixOverrides)) {
            sb.append(" suffixOverrides=\"").append(suffixOverrides).append(QUOTE);
        }
        return sb.append(RIGHT_CHEV).append(NEWLINE).append(sqlScript).append(NEWLINE).append("</trim>").toString();
    }

    /**
     * <p>
     * 生成 choose 标签的脚本
     * </p>
     *
     * @param whenTest  when 内 test 的内容
     * @param otherwise otherwise 内容
     * @return choose 脚本
     */
    public static String convertChoose(final String whenTest, final String whenSqlScript, final String otherwise) {
        return "<choose>" + NEWLINE
                + "<when test=\"" + whenTest + QUOTE + RIGHT_CHEV + NEWLINE
                + whenSqlScript + NEWLINE + "</when>" + NEWLINE
                + "<otherwise>" + otherwise + "</otherwise>" + NEWLINE
                + "</choose>" + NEWLINE;
    }

    /**
     * <p>
     * 生成 单choose 标签的脚本
     * </p>
     *
     * @param sqlScript 单choose 内 内容
     * @return choose 脚本
     */
    public static String convertChoose(final String sqlScript) {
        return "<choose>" + sqlScript + "</choose>";
    }

    /**
     * <p>
     * 生成 when 标签的脚本
     * </p>
     *
     * @param whenTest when 内 test 的内容
     * @return when 脚本
     */
    public static String convertWhen(final String whenTest, final String whenSqlScript) {
        return NEWLINE
                + "<when test=\"" + whenTest + QUOTE + RIGHT_CHEV + NEWLINE
                + whenSqlScript + NEWLINE + "</when>" + NEWLINE;
    }

    /**
     * <p>
     * 生成 otherwise 标签的脚本
     * </p>
     *
     * @param otherwise otherwise 内容
     * @return otherwise 脚本
     */
    public static String convertOtherwise(final String otherwise) {
        return NEWLINE
                + "<otherwise>" + otherwise + "</otherwise>" + NEWLINE;
    }

    /**
     * <p>
     * 生成 foreach 标签的脚本
     * </p>
     *
     * @param sqlScript  foreach 内部的 sql 脚本
     * @param collection collection
     * @param index      index
     * @param item       item
     * @param separator  separator
     * @return foreach 脚本
     */
    public static String convertForeach(final String sqlScript, final String collection, final String index,
                                        final String item, final String separator) {
        StringBuilder sb = new StringBuilder("<foreach");
        if (KarlouUtil.isNotBlank(collection)) {
            sb.append(" collection=\"").append(collection).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(index)) {
            sb.append(" index=\"").append(index).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(item)) {
            sb.append(" item=\"").append(item).append(QUOTE);
        }
        if (KarlouUtil.isNotBlank(separator)) {
            sb.append(" separator=\"").append(separator).append(QUOTE);
        }
        return sb.append(RIGHT_CHEV).append(NEWLINE).append(sqlScript).append(NEWLINE).append("</foreach>").toString()
                + NEWLINE;
    }

    /**
     * <p>
     * 生成 where 标签的脚本
     * </p>
     *
     * @param sqlScript where 内部的 sql 脚本
     * @return where 脚本
     */
    public static String convertWhere(final String sqlScript) {
        return NEWLINE + "<where>" + NEWLINE + sqlScript + NEWLINE + "</where>" + NEWLINE;
    }

    /**
     * <p>
     * 生成 set 标签的脚本
     * </p>
     *
     * @param sqlScript set 内部的 sql 脚本
     * @return set 脚本
     */
    public static String convertSet(final String sqlScript) {
        return NEWLINE + "<set>" + NEWLINE + sqlScript + NEWLINE + "</set>" + NEWLINE;
    }

    /**
     * <p>
     * 安全入参:  #{入参}
     * </p>
     *
     * @param param 入参
     * @return 脚本
     */
    public static String safeParam(final String param) {
        return HASH_LEFT_BRACE + param + RIGHT_BRACE;
    }

    /**
     * <p>
     * 非安全入参:  ${入参}
     * </p>
     *
     * @param param 入参
     * @return 脚本
     */
    public static String unSafeParam(final String param) {
        return DOLLAR_LEFT_BRACE + param + RIGHT_BRACE;
    }


    //自定义结果集ResultMap resultMap = new ResultMap.Builder(configuration, id, entityType, resultMappings).build();
}
