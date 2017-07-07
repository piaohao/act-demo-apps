package org.piaohao.act.db.mybatis;

import act.Act;
import act.app.DbServiceManager;
import act.db.DbService;
import org.osgl.$;
import org.osgl.inject.BeanSpec;
import org.osgl.inject.GenericTypedBeanLoader;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Responsible for inject BaseMapper instance
 */
public class MapperLoader implements GenericTypedBeanLoader<BaseMapper> {

    @Override
    public BaseMapper load(BeanSpec beanSpec) {
        List<Type> typeList = beanSpec.typeParams();
        int sz = typeList.size();
        if (sz > 0) {
            Class<?> modelType = BeanSpec.rawTypeOf(typeList.get(0));
            String dbId = DbServiceManager.dbId(modelType);
            DbService service = Act.app().dbServiceManager().dbService(dbId);
            if (service instanceof MybatisService) {
                MybatisService beetl = $.cast(service);
                return $.cast(beetl.mapper(modelType));
            }
        }
        return null;
    }
}
