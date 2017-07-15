package demo.todo.jfinal;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import demo.todo.jfinal.model.Account;

/**
 * Created by buhao on 2017/7/15.
 */
public class MappingKit {

    public static void init(ActiveRecordPlugin arp) {
        arp.addMapping("account", "id", Account.class);
    }
}
