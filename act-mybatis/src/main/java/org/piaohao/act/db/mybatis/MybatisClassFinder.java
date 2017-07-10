package org.piaohao.act.db.mybatis;

import act.app.App;
import act.app.DbServiceManager;
import act.app.event.AppEventId;
import act.db.DbService;
import act.db.EntityClassRepository;
import act.util.AnnotatedClassFinder;
import act.util.SubClassFinder;
import org.apache.ibatis.annotations.Mapper;
import org.beetl.sql.core.annotatoin.Table;
import org.osgl.$;
import org.osgl.exception.UnexpectedException;
import org.osgl.logging.LogManager;
import org.osgl.logging.Logger;
import org.osgl.util.Generics;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import java.lang.reflect.Type;
import java.util.List;

import static act.app.DbServiceManager.dbId;

/**
 * Find classes with annotation `Table`
 */
@Singleton
public class MybatisClassFinder {

    private static final Logger LOGGER = LogManager.get(MybatisClassFinder.class);

    private final EntityClassRepository repo;
    private final App app;

    @Inject
    public MybatisClassFinder(EntityClassRepository repo, App app) {
        this.repo = $.notNull(repo);
        this.app = $.notNull(app);
    }

    @AnnotatedClassFinder(Table.class)
    public void foundEntity(Class<?> modelClass) {
        repo.registerModelClass(modelClass);
    }

    @AnnotatedClassFinder(Entity.class)
    public void foundEntity2(Class<?> modelClass) {
        repo.registerModelClass(modelClass);
    }

    //@AnnotatedClassFinder(value = Mapper.class, noAbstract = false, callOn = AppEventId.PRE_START)
    @SubClassFinder(value = BaseMapper.class, noAbstract = false, callOn = AppEventId.PRE_START)
    public void foundMapper(Class<? extends BaseMapper> mapperClass) {
        DbServiceManager dbServiceManager = app.dbServiceManager();
        DbService dbService = dbServiceManager.dbService("mybatis");
        ((MybatisService) dbService).prepareMapperClass(mapperClass);
//        try {
////            Class<?> modelClass = modelClass(mapperClass);
////            DbService dbService = dbServiceManager.dbService(dbId(modelClass));
//            if (dbService instanceof MybatisService) {
//                ((MybatisService) dbService).prepareMapperClass(mapperClass, modelClass);
//            } else {
//                throw new UnexpectedException("mapper class cannot be landed to a BeetlSqlService");
//            }
//        } catch (RuntimeException e) {
//            LOGGER.warn(e, "Error registering mapping class: %s", mapperClass);
//        }
    }

//    static Class<?> modelClass(Class<? extends BaseMapper> mapperClass) {
//        List<Type> paramTypes = Generics.typeParamImplementations(mapperClass, BaseMapper.class);
//        if (paramTypes.size() != 1) {
//            throw new UnexpectedException("Cannot determine parameter type of %s", mapperClass);
//        }
//        Type type = paramTypes.get(0);
//        if (!(type instanceof Class)) {
//            throw new UnexpectedException("Cannot determine parameter type of %s", mapperClass);
//        }
//        return $.cast(type);
//    }

}
