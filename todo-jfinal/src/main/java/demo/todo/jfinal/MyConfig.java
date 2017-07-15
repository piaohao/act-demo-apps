package demo.todo.jfinal;

import act.job.OnAppStart;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
//import com.alibaba.dubbo.config.ApplicationConfig;
//import com.alibaba.dubbo.config.ProtocolConfig;
//import com.alibaba.dubbo.config.RegistryConfig;
//import com.alibaba.dubbo.config.ServiceConfig;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import demo.todo.jfinal.model.Account;

public class MyConfig {

//    @OnAppStart
    public static void init() {
        PropKit.use("common.properties");
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
        // 所有配置在 MappingKit 中搞定
        arp.addMapping("account", "id", Account.class);
        dp.start();
        arp.start();
    }
//
//    @OnAppStart
//    public static void initDubbo() {
//        // 服务实现
//        HelloService helloService = new HelloServiceImpl();
//
//        // 当前应用配置
//        ApplicationConfig application = new ApplicationConfig();
//        application.setName("act-dubbo");
//
//        // 连接注册中心配置
//        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress("192.168.6.132:9090");
//        registry.setProtocol("redis");
//
//        // 服务提供者协议配置
//        ProtocolConfig protocol = new ProtocolConfig();
//        protocol.setName("redis");
//        protocol.setPort(6379);
//        protocol.setThreads(200);
//
//        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
//
//        // 服务提供者暴露服务配置
//        ServiceConfig<HelloService> service = new ServiceConfig<HelloService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
//        service.setApplication(application);
//        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
//        service.setProtocol(protocol); // 多个协议可以用setProtocols()
//        service.setInterface(HelloService.class);
//        service.setRef(helloService);
//        service.setVersion("1.0.0");
//
//        // 暴露及注册服务
//        service.export();
//    }
}
