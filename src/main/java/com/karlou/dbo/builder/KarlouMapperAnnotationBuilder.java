package com.karlou.dbo.builder;

import com.karlou.dbo.base.BaseMapper;
import com.karlou.dbo.util.KarlouUtil;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.builder.annotation.MethodResolver;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.PropertyParser;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/21 15:53
 */
public class KarlouMapperAnnotationBuilder extends MapperAnnotationBuilder {
    private static final Set<Class<? extends Annotation>> statementAnnotationTypes =
            (Set) Stream.of(Select.class, Update.class, Insert.class, Delete.class, SelectProvider.class,
                    UpdateProvider.class, InsertProvider.class, DeleteProvider.class).collect(Collectors.toSet());
    private final Configuration configuration;
    private final MapperBuilderAssistant assistant;
    private final Class<?> type;

    public KarlouMapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        super(configuration, type);
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;
    }

    @Override
    public void parse() {
        String resource = this.type.toString();
        if (!this.configuration.isResourceLoaded(resource)) {
            this.loadXmlResource();
            this.configuration.addLoadedResource(resource);
            this.assistant.setCurrentNamespace(this.type.getName());
            this.parseCache();
            this.parseCacheRef();
            Method[] var2 = this.type.getMethods();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Method method = var2[var4];
                if (this.canHaveStatement(method)) {
                    if (this.getAnnotationWrapper(method, false, Select.class, SelectProvider.class).isPresent() && method.getAnnotation(ResultMap.class) == null) {
                        this.parseResultMap(method);
                    }

                    try {
                        this.parseStatement(method);
                    } catch (IncompleteElementException var7) {
                        this.configuration.addIncompleteMethod(new MethodResolver(this, method));
                    }
                }
            }
            // TODO 注入 CURD 动态 SQL , 放在在最后, because 可能会有人会用注解重写sql
            if (KarlouUtil.isBaseMapperSub(type)) {
                //注入
                KarlouMapperBuilder.newInstance(type).injector(configuration, assistant);
            }
        }

        this.parsePendingMethods();
    }

    private boolean canHaveStatement(Method method) {
        return !method.isBridge() && !method.isDefault();
    }

    private void parsePendingMethods() {
        Collection<MethodResolver> incompleteMethods = this.configuration.getIncompleteMethods();
        synchronized (incompleteMethods) {
            Iterator iter = incompleteMethods.iterator();

            while (iter.hasNext()) {
                try {
                    ((MethodResolver) iter.next()).resolve();
                    iter.remove();
                } catch (IncompleteElementException var6) {
                }
            }

        }
    }

    private void loadXmlResource() {
        if (!this.configuration.isResourceLoaded("namespace:" + this.type.getName())) {
            String xmlResource = this.type.getName().replace('.', '/') + ".xml";
            InputStream inputStream = this.type.getResourceAsStream("/" + xmlResource);
            if (inputStream == null) {
                try {
                    inputStream = Resources.getResourceAsStream(this.type.getClassLoader(), xmlResource);
                } catch (IOException var4) {
                }
            }

            if (inputStream != null) {
                XMLMapperBuilder xmlParser = new XMLMapperBuilder(inputStream, this.assistant.getConfiguration(), xmlResource, this.configuration.getSqlFragments(), this.type.getName());
                xmlParser.parse();
            }
        }

    }

    private void parseCache() {
        CacheNamespace cacheDomain = (CacheNamespace) this.type.getAnnotation(CacheNamespace.class);
        if (cacheDomain != null) {
            Integer size = cacheDomain.size() == 0 ? null : cacheDomain.size();
            Long flushInterval = cacheDomain.flushInterval() == 0L ? null : cacheDomain.flushInterval();
            Properties props = this.convertToProperties(cacheDomain.properties());
            this.assistant.useNewCache(cacheDomain.implementation(), cacheDomain.eviction(), flushInterval, size, cacheDomain.readWrite(), cacheDomain.blocking(), props);
        }

    }

    private Properties convertToProperties(Property[] properties) {
        if (properties.length == 0) {
            return null;
        } else {
            Properties props = new Properties();
            Property[] var3 = properties;
            int var4 = properties.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Property property = var3[var5];
                props.setProperty(property.name(), PropertyParser.parse(property.value(), this.configuration.getVariables()));
            }

            return props;
        }
    }

    private void parseCacheRef() {
        CacheNamespaceRef cacheDomainRef = (CacheNamespaceRef) this.type.getAnnotation(CacheNamespaceRef.class);
        if (cacheDomainRef != null) {
            Class<?> refType = cacheDomainRef.value();
            String refName = cacheDomainRef.name();
            if (refType == Void.TYPE && refName.isEmpty()) {
                throw new BuilderException("Should be specified either value() or name() attribute in the @CacheNamespaceRef");
            }

            if (refType != Void.TYPE && !refName.isEmpty()) {
                throw new BuilderException("Cannot use both value() and name() attribute in the @CacheNamespaceRef");
            }

            String namespace = refType != Void.TYPE ? refType.getName() : refName;

            try {
                this.assistant.useCacheRef(namespace);
            } catch (IncompleteElementException var6) {
                this.configuration.addIncompleteCacheRef(new CacheRefResolver(this.assistant, namespace));
            }
        }

    }

    private String parseResultMap(Method method) {
        Class<?> returnType = this.getReturnType(method);
        Arg[] args = (Arg[]) method.getAnnotationsByType(Arg.class);
        Result[] results = (Result[]) method.getAnnotationsByType(Result.class);
        TypeDiscriminator typeDiscriminator = (TypeDiscriminator) method.getAnnotation(TypeDiscriminator.class);
        String resultMapId = this.generateResultMapName(method);
        this.applyResultMap(resultMapId, returnType, args, results, typeDiscriminator);
        return resultMapId;
    }

    private String generateResultMapName(Method method) {
        Results results = (Results) method.getAnnotation(Results.class);
        if (results != null && !results.id().isEmpty()) {
            return this.type.getName() + "." + results.id();
        } else {
            StringBuilder suffix = new StringBuilder();
            Class[] var4 = method.getParameterTypes();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Class<?> c = var4[var6];
                suffix.append("-");
                suffix.append(c.getSimpleName());
            }

            if (suffix.length() < 1) {
                suffix.append("-void");
            }

            return this.type.getName() + "." + method.getName() + suffix;
        }
    }

    private void applyResultMap(String resultMapId, Class<?> returnType, Arg[] args, Result[] results, TypeDiscriminator discriminator) {
        List<ResultMapping> resultMappings = new ArrayList();
        this.applyConstructorArgs(args, returnType, resultMappings);
        this.applyResults(results, returnType, resultMappings);
        Discriminator disc = this.applyDiscriminator(resultMapId, returnType, discriminator);
        this.assistant.addResultMap(resultMapId, returnType, (String) null, disc, resultMappings, (Boolean) null);
        this.createDiscriminatorResultMaps(resultMapId, returnType, discriminator);
    }

    private void createDiscriminatorResultMaps(String resultMapId, Class<?> resultType, TypeDiscriminator discriminator) {
        if (discriminator != null) {
            Case[] var4 = discriminator.cases();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Case c = var4[var6];
                String caseResultMapId = resultMapId + "-" + c.value();
                List<ResultMapping> resultMappings = new ArrayList();
                this.applyConstructorArgs(c.constructArgs(), resultType, resultMappings);
                this.applyResults(c.results(), resultType, resultMappings);
                this.assistant.addResultMap(caseResultMapId, c.type(), resultMapId, (Discriminator) null, resultMappings, (Boolean) null);
            }
        }

    }

    private Discriminator applyDiscriminator(String resultMapId, Class<?> resultType, TypeDiscriminator discriminator) {
        if (discriminator != null) {
            String column = discriminator.column();
            Class<?> javaType = discriminator.javaType() == void.class ? String.class : discriminator.javaType();
            JdbcType jdbcType = discriminator.jdbcType() == JdbcType.UNDEFINED ? null : discriminator.jdbcType();
            @SuppressWarnings("unchecked")
            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>)
                    (discriminator.typeHandler() == UnknownTypeHandler.class ? null : discriminator.typeHandler());
            Case[] cases = discriminator.cases();
            Map<String, String> discriminatorMap = new HashMap<>();
            for (Case c : cases) {
                String value = c.value();
                String caseResultMapId = resultMapId + "-" + value;
                discriminatorMap.put(value, caseResultMapId);
            }
            return assistant.buildDiscriminator(resultType, column, javaType, jdbcType, typeHandler, discriminatorMap);
        }
        return null;
    }

    void parseStatement(Method method) {
        final Class<?> parameterTypeClass = getParameterType(method);
        final LanguageDriver languageDriver = getLanguageDriver(method);

        getAnnotationWrapper(method, true, statementAnnotationTypes).ifPresent(statementAnnotation -> {
            final SqlSource sqlSource = buildSqlSource(statementAnnotation.getAnnotation(), parameterTypeClass, languageDriver, method);
            final SqlCommandType sqlCommandType = statementAnnotation.getSqlCommandType();
            final Options options = getAnnotationWrapper(method, false, Options.class).map(x -> (Options) x.getAnnotation()).orElse(null);
            final String mappedStatementId = type.getName() + "." + method.getName();

            final KeyGenerator keyGenerator;
            String keyProperty = null;
            String keyColumn = null;
            if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
                // first check for SelectKey annotation - that overrides everything else
                SelectKey selectKey = getAnnotationWrapper(method, false, SelectKey.class).map(x -> (SelectKey) x.getAnnotation()).orElse(null);
                if (selectKey != null) {
                    keyGenerator = handleSelectKeyAnnotation(selectKey, mappedStatementId, getParameterType(method), languageDriver);
                    keyProperty = selectKey.keyProperty();
                } else if (options == null) {
                    keyGenerator = configuration.isUseGeneratedKeys() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
                } else {
                    keyGenerator = options.useGeneratedKeys() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
                    keyProperty = options.keyProperty();
                    keyColumn = options.keyColumn();
                }
            } else {
                keyGenerator = NoKeyGenerator.INSTANCE;
            }

            Integer fetchSize = null;
            Integer timeout = null;
            StatementType statementType = StatementType.PREPARED;
            ResultSetType resultSetType = configuration.getDefaultResultSetType();
            boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
            boolean flushCache = !isSelect;
            boolean useCache = isSelect;
            if (options != null) {
                if (Options.FlushCachePolicy.TRUE.equals(options.flushCache())) {
                    flushCache = true;
                } else if (Options.FlushCachePolicy.FALSE.equals(options.flushCache())) {
                    flushCache = false;
                }
                useCache = options.useCache();
                fetchSize = options.fetchSize() > -1 || options.fetchSize() == Integer.MIN_VALUE ? options.fetchSize() : null; //issue #348
                timeout = options.timeout() > -1 ? options.timeout() : null;
                statementType = options.statementType();
                if (options.resultSetType() != ResultSetType.DEFAULT) {
                    resultSetType = options.resultSetType();
                }
            }

            String resultMapId = null;
            if (isSelect) {
                ResultMap resultMapAnnotation = method.getAnnotation(ResultMap.class);
                if (resultMapAnnotation != null) {
                    resultMapId = String.join(",", resultMapAnnotation.value());
                } else {
                    resultMapId = generateResultMapName(method);
                }
            }

            assistant.addMappedStatement(
                    mappedStatementId,
                    sqlSource,
                    statementType,
                    sqlCommandType,
                    fetchSize,
                    timeout,
                    // ParameterMapID
                    null,
                    parameterTypeClass,
                    resultMapId,
                    getReturnType(method),
                    resultSetType,
                    flushCache,
                    useCache,
                    // TODO gcode issue #577
                    false,
                    keyGenerator,
                    keyProperty,
                    keyColumn,
                    statementAnnotation.getDatabaseId(),
                    languageDriver,
                    // ResultSets
                    options != null ? nullOrEmpty(options.resultSets()) : null);
        });
    }

    private LanguageDriver getLanguageDriver(Method method) {
        Lang lang = (Lang) method.getAnnotation(Lang.class);
        Class<? extends LanguageDriver> langClass = null;
        if (lang != null) {
            langClass = lang.value();
        }

        return this.configuration.getLanguageDriver(langClass);
    }

    private Class<?> getParameterType(Method method) {
        Class<?> parameterType = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class[] var4 = parameterTypes;
        int var5 = parameterTypes.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Class<?> currentParameterType = var4[var6];
            if (!RowBounds.class.isAssignableFrom(currentParameterType) && !ResultHandler.class.isAssignableFrom(currentParameterType)) {
                if (parameterType == null) {
                    parameterType = currentParameterType;
                } else {
                    parameterType = MapperMethod.ParamMap.class;
                }
            }
        }

        return parameterType;
    }

    private Class<?> getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, this.type);
        if (resolvedReturnType instanceof Class) {
            returnType = (Class) resolvedReturnType;
            if (returnType.isArray()) {
                returnType = returnType.getComponentType();
            }

            if (Void.TYPE.equals(returnType)) {
                ResultType rt = (ResultType) method.getAnnotation(ResultType.class);
                if (rt != null) {
                    returnType = rt.value();
                }
            }
        } else if (resolvedReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) resolvedReturnType;
            Class<?> rawType = (Class) parameterizedType.getRawType();
            Type[] actualTypeArguments;
            Type returnTypeParameter;
            if (!Collection.class.isAssignableFrom(rawType) && !Cursor.class.isAssignableFrom(rawType)) {
                if (method.isAnnotationPresent(MapKey.class) && Map.class.isAssignableFrom(rawType)) {
                    actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                        returnTypeParameter = actualTypeArguments[1];
                        if (returnTypeParameter instanceof Class) {
                            returnType = (Class) returnTypeParameter;
                        } else if (returnTypeParameter instanceof ParameterizedType) {
                            returnType = (Class) ((ParameterizedType) returnTypeParameter).getRawType();
                        }
                    }
                } else if (Optional.class.equals(rawType)) {
                    actualTypeArguments = parameterizedType.getActualTypeArguments();
                    returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class) returnTypeParameter;
                    }
                }
            } else {
                actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnType = (Class) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class) ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            }
        }

        return returnType;
    }

    private void applyResults(Result[] results, Class<?> resultType, List<ResultMapping> resultMappings) {
        Result[] var4 = results;
        int var5 = results.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Result result = var4[var6];
            List<ResultFlag> flags = new ArrayList();
            if (result.id()) {
                flags.add(ResultFlag.ID);
            }

            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (result.typeHandler() == UnknownTypeHandler.class ? null : result.typeHandler());
            boolean hasNestedResultMap = this.hasNestedResultMap(result);
            ResultMapping resultMapping = this.assistant.buildResultMapping(resultType, this.nullOrEmpty(result.property()), this.nullOrEmpty(result.column()), result.javaType() == Void.TYPE ? null : result.javaType(), result.jdbcType() == JdbcType.UNDEFINED ? null : result.jdbcType(), this.hasNestedSelect(result) ? this.nestedSelectId(result) : null, hasNestedResultMap ? this.nestedResultMapId(result) : null, (String) null, hasNestedResultMap ? this.findColumnPrefix(result) : null, typeHandler, flags, (String) null, (String) null, this.isLazy(result));
            resultMappings.add(resultMapping);
        }

    }

    private String findColumnPrefix(Result result) {
        String columnPrefix = result.one().columnPrefix();
        if (columnPrefix.length() < 1) {
            columnPrefix = result.many().columnPrefix();
        }

        return columnPrefix;
    }

    private String nestedResultMapId(Result result) {
        String resultMapId = result.one().resultMap();
        if (resultMapId.length() < 1) {
            resultMapId = result.many().resultMap();
        }

        if (!resultMapId.contains(".")) {
            resultMapId = this.type.getName() + "." + resultMapId;
        }

        return resultMapId;
    }

    private boolean hasNestedResultMap(Result result) {
        if (result.one().resultMap().length() > 0 && result.many().resultMap().length() > 0) {
            throw new BuilderException("Cannot use both @One and @Many annotations in the same @Result");
        } else {
            return result.one().resultMap().length() > 0 || result.many().resultMap().length() > 0;
        }
    }

    private String nestedSelectId(Result result) {
        String nestedSelect = result.one().select();
        if (nestedSelect.length() < 1) {
            nestedSelect = result.many().select();
        }

        if (!nestedSelect.contains(".")) {
            nestedSelect = this.type.getName() + "." + nestedSelect;
        }

        return nestedSelect;
    }

    private boolean isLazy(Result result) {
        boolean isLazy = this.configuration.isLazyLoadingEnabled();
        if (result.one().select().length() > 0 && FetchType.DEFAULT != result.one().fetchType()) {
            isLazy = result.one().fetchType() == FetchType.LAZY;
        } else if (result.many().select().length() > 0 && FetchType.DEFAULT != result.many().fetchType()) {
            isLazy = result.many().fetchType() == FetchType.LAZY;
        }

        return isLazy;
    }

    private boolean hasNestedSelect(Result result) {
        if (result.one().select().length() > 0 && result.many().select().length() > 0) {
            throw new BuilderException("Cannot use both @One and @Many annotations in the same @Result");
        } else {
            return result.one().select().length() > 0 || result.many().select().length() > 0;
        }
    }

    private void applyConstructorArgs(Arg[] args, Class<?> resultType, List<ResultMapping> resultMappings) {
        Arg[] var4 = args;
        int var5 = args.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Arg arg = var4[var6];
            List<ResultFlag> flags = new ArrayList();
            flags.add(ResultFlag.CONSTRUCTOR);
            if (arg.id()) {
                flags.add(ResultFlag.ID);
            }

            Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (arg.typeHandler() == UnknownTypeHandler.class ? null : arg.typeHandler());
            ResultMapping resultMapping = this.assistant.buildResultMapping(resultType, this.nullOrEmpty(arg.name()), this.nullOrEmpty(arg.column()), arg.javaType() == Void.TYPE ? null : arg.javaType(), arg.jdbcType() == JdbcType.UNDEFINED ? null : arg.jdbcType(), this.nullOrEmpty(arg.select()), this.nullOrEmpty(arg.resultMap()), (String) null, this.nullOrEmpty(arg.columnPrefix()), typeHandler, flags, (String) null, (String) null, false);
            resultMappings.add(resultMapping);
        }

    }

    private String nullOrEmpty(String value) {
        return value != null && value.trim().length() != 0 ? value : null;
    }

    private KeyGenerator handleSelectKeyAnnotation(SelectKey selectKeyAnnotation, String baseStatementId, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        String id = baseStatementId + "!selectKey";
        Class<?> resultTypeClass = selectKeyAnnotation.resultType();
        StatementType statementType = selectKeyAnnotation.statementType();
        String keyProperty = selectKeyAnnotation.keyProperty();
        String keyColumn = selectKeyAnnotation.keyColumn();
        boolean executeBefore = selectKeyAnnotation.before();
        boolean useCache = false;
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        Integer fetchSize = null;
        Integer timeout = null;
        boolean flushCache = false;
        String parameterMap = null;
        String resultMap = null;
        ResultSetType resultSetTypeEnum = null;
        String databaseId = selectKeyAnnotation.databaseId().isEmpty() ? null : selectKeyAnnotation.databaseId();
        SqlSource sqlSource = this.buildSqlSource(selectKeyAnnotation, parameterTypeClass, languageDriver, (Method) null);
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;
        this.assistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, (Integer) fetchSize, (Integer) timeout, (String) parameterMap, parameterTypeClass, (String) resultMap, resultTypeClass, (ResultSetType) resultSetTypeEnum, flushCache, useCache, false, keyGenerator, keyProperty, keyColumn, databaseId, languageDriver, (String) null);
        id = this.assistant.applyCurrentNamespace(id, false);
        MappedStatement keyStatement = this.configuration.getMappedStatement(id, false);
        SelectKeyGenerator answer = new SelectKeyGenerator(keyStatement, executeBefore);
        this.configuration.addKeyGenerator(id, answer);
        return answer;
    }

    private SqlSource buildSqlSource(Annotation annotation, Class<?> parameterType, LanguageDriver languageDriver, Method method) {
        if (annotation instanceof Select) {
            return this.buildSqlSourceFromStrings(((Select) annotation).value(), parameterType, languageDriver);
        } else if (annotation instanceof Update) {
            return this.buildSqlSourceFromStrings(((Update) annotation).value(), parameterType, languageDriver);
        } else if (annotation instanceof Insert) {
            return this.buildSqlSourceFromStrings(((Insert) annotation).value(), parameterType, languageDriver);
        } else if (annotation instanceof Delete) {
            return this.buildSqlSourceFromStrings(((Delete) annotation).value(), parameterType, languageDriver);
        } else {
            return (SqlSource) (annotation instanceof SelectKey ? this.buildSqlSourceFromStrings(((SelectKey) annotation).statement(), parameterType, languageDriver) : new ProviderSqlSource(this.assistant.getConfiguration(), annotation, this.type, method));
        }
    }

    private SqlSource buildSqlSourceFromStrings(String[] strings, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        return languageDriver.createSqlSource(this.configuration, String.join(" ", strings).trim(), parameterTypeClass);
    }

    @SafeVarargs
    private final Optional<KarlouMapperAnnotationBuilder.AnnotationWrapper> getAnnotationWrapper(Method method, boolean errorIfNoMatch, Class<? extends Annotation>... targetTypes) {
        return this.getAnnotationWrapper(method, errorIfNoMatch, (Collection) Arrays.asList(targetTypes));
    }

    private Optional<KarlouMapperAnnotationBuilder.AnnotationWrapper> getAnnotationWrapper(Method method, boolean errorIfNoMatch, Collection<Class<? extends Annotation>> targetTypes) {
        String databaseId = this.configuration.getDatabaseId();
        Map<String, KarlouMapperAnnotationBuilder.AnnotationWrapper> statementAnnotations = (Map) targetTypes.stream().flatMap((x) -> {
            return Arrays.stream(method.getAnnotationsByType(x));
        }).map((x$0) -> {
            return new KarlouMapperAnnotationBuilder.AnnotationWrapper(x$0);
        }).collect(Collectors.toMap(KarlouMapperAnnotationBuilder.AnnotationWrapper::getDatabaseId, (x) -> {
            return x;
        }, (existing, duplicate) -> {
            throw new BuilderException(String.format("Detected conflicting annotations '%s' and '%s' on '%s'.", existing.getAnnotation(), duplicate.getAnnotation(), method.getDeclaringClass().getName() + "." + method.getName()));
        }));
        KarlouMapperAnnotationBuilder.AnnotationWrapper annotationWrapper = null;
        if (databaseId != null) {
            annotationWrapper = (KarlouMapperAnnotationBuilder.AnnotationWrapper) statementAnnotations.get(databaseId);
        }

        if (annotationWrapper == null) {
            annotationWrapper = (KarlouMapperAnnotationBuilder.AnnotationWrapper) statementAnnotations.get("");
        }

        if (errorIfNoMatch && annotationWrapper == null && !statementAnnotations.isEmpty()) {
            throw new BuilderException(String.format("Could not find a statement annotation that correspond a current database or default statement on method '%s.%s'. Current database id is [%s].", method.getDeclaringClass().getName(), method.getName(), databaseId));
        } else {
            return Optional.ofNullable(annotationWrapper);
        }
    }

    private class AnnotationWrapper {
        private final Annotation annotation;
        private final String databaseId;
        private final SqlCommandType sqlCommandType;

        AnnotationWrapper(Annotation annotation) {
            this.annotation = annotation;
            if (annotation instanceof Select) {
                this.databaseId = ((Select) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.SELECT;
            } else if (annotation instanceof Update) {
                this.databaseId = ((Update) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.UPDATE;
            } else if (annotation instanceof Insert) {
                this.databaseId = ((Insert) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.INSERT;
            } else if (annotation instanceof Delete) {
                this.databaseId = ((Delete) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.DELETE;
            } else if (annotation instanceof SelectProvider) {
                this.databaseId = ((SelectProvider) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.SELECT;
            } else if (annotation instanceof UpdateProvider) {
                this.databaseId = ((UpdateProvider) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.UPDATE;
            } else if (annotation instanceof InsertProvider) {
                this.databaseId = ((InsertProvider) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.INSERT;
            } else if (annotation instanceof DeleteProvider) {
                this.databaseId = ((DeleteProvider) annotation).databaseId();
                this.sqlCommandType = SqlCommandType.DELETE;
            } else {
                this.sqlCommandType = SqlCommandType.UNKNOWN;
                if (annotation instanceof Options) {
                    this.databaseId = ((Options) annotation).databaseId();
                } else if (annotation instanceof SelectKey) {
                    this.databaseId = ((SelectKey) annotation).databaseId();
                } else {
                    this.databaseId = "";
                }
            }

        }

        Annotation getAnnotation() {
            return this.annotation;
        }

        SqlCommandType getSqlCommandType() {
            return this.sqlCommandType;
        }

        String getDatabaseId() {
            return this.databaseId;
        }
    }
}
