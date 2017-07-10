package org.piaohao.act.db.mybatis;

import act.Act;
import act.app.App;
import act.db.Dao;
import act.db.sql.DataSourceConfig;
import act.db.sql.SqlDbService;
import act.db.sql.util.NamingConvention;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.mapper.DefaultMapperBuilder;
import org.beetl.sql.core.mapper.MapperJavaProxy;
import org.beetl.sql.ext.DebugInterceptor;
import org.osgl.$;
import org.osgl.inject.Genie;
import org.osgl.util.E;
import org.osgl.util.S;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implement `act.db.DbService` using BeetlSql
 */
public class MybatisService extends SqlDbService {

    public static final String DEF_LOADER_PATH = "/sql";

    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private ConcurrentMap<Class, Object> mapperMap = new ConcurrentHashMap<>();
    //private ConnectionSource connectionSource;

    public MybatisService(String dbId, App app, Map<String, String> config) {
        super(dbId, app, config);
    }

//    public SQLManager beetlSql() {
//        return beetlSql;
//    }

    @Override
    protected void dataSourceProvided(DataSource dataSource, DataSourceConfig dsConfig) {
//        connectionSource = ConnectionSourceHelper.getSingle(dataSource);
//        DBStyle style = configureDbStyle(dsConfig);
//        SQLLoader loader = configureLoader();
//        NameConversion nm = configureNamingConvention();
//        Interceptor[] ins = configureInterceptor();
//        beetlSql = new SQLManager(style, loader, connectionSource, nm, ins);

        //DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        Configuration configuration = new Configuration(environment);
//        configuration.addMapper(BlogMapper.class);
//
//        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Override
    protected DataSource createDataSource() {
        throw E.unsupport("External datasource solution must be provided. E.g. hikaricp");
    }

    @Override
    protected boolean supportDdl() {
        return false;
    }

    @Override
    public <DAO extends Dao> DAO defaultDao(Class<?> aClass) {
        throw E.unsupport("BeetlSql does not support DAO. Please use mapper instead");
    }

    @Override
    public <DAO extends Dao> DAO newDaoInstance(Class<DAO> aClass) {
        throw E.unsupport("BeetlSql does not support DAO. Please use mapper instead");
    }

    @Override
    public Class<? extends Annotation> entityAnnotationType() {
        return Table.class;
    }

    @Override
    protected void releaseResources() {
        if (_logger.isDebugEnabled()) {
            _logger.debug("beetsql shutdown: %s", id());
        }
        super.releaseResources();
    }

    Object mapper(Class mapperClass) {
        return mapperMap.get(mapperClass);
    }

    public void prepareMapperClass(Class<? extends BaseMapper> mapperClass) {
//        Object o = Proxy.newProxyInstance(mapperClass.getClassLoader(),
//                new Class<?>[]{mapperClass},
//                new MapperJavaProxy(new DefaultMapperBuilder(beetlSql), beetlSql, mapperClass));
//        final BaseMapper mapper = $.cast(o);
//        mapperMap.put(mapperClass, mapper);
//        mapperMap.put(modelClass, mapper);
//        Genie genie = Act.getInstance(Genie.class);
//        genie.registerProvider(mapperClass, new Provider() {
//            @Override
//            public Object get() {
//                return mapper;
//            }
//        });
        if (sqlSession == null) {
            sqlSession = sqlSessionFactory.openSession();
        }
        String canonicalName = mapperClass.getCanonicalName();
        BaseMapper mapperBean = null;
        try {
            //mapperBean = $.cast(sqlSession.getMapper(Class.forName(canonicalName)));
            MapperProxy mapperProxy = new MapperProxy(sqlSession, mapperClass, new ConcurrentHashMap<Method, MapperMethod>());
            mapperBean = $.cast(Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, mapperProxy));
        } catch (Exception e) {
            return;
        }
        mapperMap.put(mapperClass, mapperBean);


//        Configuration configuration = sqlSession.getConfiguration();
//        //configuration.addMapper();
//        MapperRegistry mapperRegistry = configuration.getMapperRegistry();
//        Collection<Class<?>> mappers = mapperRegistry.getMappers();
////        MapperProxyFactory mapperBean = null;
//        BaseMapper mapperBean = null;
//        for (Class<?> mapper : mappers) {
//            String canonicalName = mapper.getCanonicalName();
//            if (mapperClass.getCanonicalName().equals(canonicalName)) {
//                //MapperProxyFactory factory = $.cast(sqlSession.getMapper(mapper));
//                //Object mapper1 = mapperRegistry.getMapper(mapper, sqlSession);
//                try {
//                    mapperBean = $.cast(sqlSession.getMapper(Class.forName(canonicalName)));
//                } catch (ClassNotFoundException e) {
//                    return;
//                }
////                mapperBean = $.cast(mapper1);
////                mapperBean = sqlSession.getMapper(mapper);
//                mapperMap.put(mapperClass, mapperBean);
//                break;
//            }
//        }
//        Object mapper = sqlSession.getMapper(mapperClass);
//        mapperMap.put(mapperClass, mapper);
        if (mapperBean != null) {
            Genie genie = Act.getInstance(Genie.class);
            BaseMapper finalMapperBean = $.cast(mapperBean);
            genie.registerProvider(mapperClass, (Provider) () -> finalMapperBean);
        }
    }

//    private NameConversion configureNamingConvention() {
//        String s = this.config.rawConf.get("beetlsql.nc");
//        if (null != s) {
//            return Act.getInstance(s);
//        }
//
//        if (NamingConvention.Default.UNDERSCORE == this.config.tableNamingConvention) {
//            return new UnderlinedNameConversion();
//        }
//        return new DefaultNameConversion();
//    }
//
//    private SQLLoader configureLoader() {
//        String loaderPath = this.config.rawConf.get("loader.path");
//        if (null == loaderPath) {
//            loaderPath = DEF_LOADER_PATH;
//        }
//        return new ClasspathLoader(loaderPath);
//    }
//
//    private Interceptor[] configureInterceptor() {
//        String debug = this.config.rawConf.get("interceptor.debug");
//        if (null == debug) {
//            return new Interceptor[0];
//        }
//        boolean isDebug = Boolean.parseBoolean(debug);
//        return isDebug ? new Interceptor[]{new DebugInterceptor()} : new Interceptor[0];
//    }
//
//    private DBStyle configureDbStyle(DataSourceConfig dsConfig) {
//        Map<String, String> conf = this.config.rawConf;
//        String style = conf.get("platform");
//        if (null == style) {
//            style = conf.get("style");
//        }
//        if (null == style) {
//            style = dsConfig.url;
//        }
//        if (S.notBlank(style)) {
//            style = style.trim().toLowerCase();
//            if (style.contains("oracle")) {
//                return new OracleStyle();
//            } else if (style.contains("mysql") || style.contains("maria")) {
//                return new MySqlStyle();
//            } else if (style.contains("postgres") || style.contains("pgsql")) {
//                return new PostgresStyle();
//            } else if (style.contains("h2")) {
//                return new H2Style();
//            } else if (style.contains("sqlserver")) {
//                return new SqlServerStyle();
//            } else if (style.contains("db2")) {
//                return new DB2SqlStyle();
//            } else if (style.contains("sqlite")) {
//                return new SQLiteStyle();
//            }
//        }
//        throw new UnsupportedOperationException("Unknown database style: " + style);
//    }

}
