package demo.todo.jfinal;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import demo.todo.jfinal.model.Account;

public class MainConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        PropKit.use("common.properties");//加载全局属性
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/",IndexController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin dp = new DruidPlugin(PropKit.get("jdbc.master.url"), PropKit.get(
                "jdbc.master.username"), PropKit.get("jdbc.master.password"));
        dp.setTestOnBorrow(true);
        dp.setTestWhileIdle(true);
        dp.setTestOnReturn(true);
        dp.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType("mysql");
        dp.addFilter(wall);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(PropKit.getBoolean("devMode", true));
        arp.addMapping("account", "id", Account.class);
        me.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
