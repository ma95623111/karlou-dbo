package com.karlou.dbo.builder;

import com.karlou.dbo.annotation.TableAttribute;
import com.karlou.dbo.injector.SqlInjector;
import com.karlou.dbo.util.KarlouUtil;
import com.karlou.dbo.util.SpringContextUtil;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * BaseMapper 方法构造
 *
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/25 08:37
 */
public class KarlouMapperBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(KarlouMapperBuilder.class);
    //具体方法处理sql模版
    private SqlInjector sqlInjector;
    private Class type;

    private KarlouMapperBuilder(Class type) {
        this.type = type;
    }

    public void injector(Configuration configuration, MapperBuilderAssistant assistant) {
        Class entityClass = getEntityClass();
        TableAttribute tableAnnotation = (TableAttribute) entityClass.getAnnotation(TableAttribute.class);
        SqlInjector bean = (SqlInjector) SpringContextUtil.getBean(tableAnnotation.sqlInjector());
        bean.injector(configuration, assistant, type, entityClass);
    }


    public static KarlouMapperBuilder newInstance(Class type) {
        return new KarlouMapperBuilder(type);
    }

    public Class getEntityClass() {
        Type[] genericInterfaces = this.type.getGenericInterfaces();
        for (Type t :
                genericInterfaces) {
            ParameterizedType pt = (ParameterizedType) t;
            if (KarlouUtil.isBaseMapper(pt.getRawType().getTypeName())) {
                Type[] actualTypeArguments = pt.getActualTypeArguments();
                Assert.state(actualTypeArguments.length == 1, "Basemapper does not set the corresponding entity type！");
                String typeName = actualTypeArguments[0].getTypeName();
                try {
                    Class<?> forNameClass = Class.forName(typeName);
                    return forNameClass;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
