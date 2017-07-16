package demo.todo.jfinal;

import act.Act;
import act.job.OnAppStart;
import act.util.DisableFastJsonCircularReferenceDetect;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import demo.todo.jfinal.model.Account;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.annotation.With;
import org.osgl.mvc.result.RenderJSON;
import org.piaohao.act.jfinal.db.JFinalDao;
import org.piaohao.act.jfinal.db.JFinalTransactional;

import javax.inject.Inject;
import java.util.List;

import static act.controller.Controller.Util.render;
import static act.controller.Controller.Util.renderJson;
import static act.controller.Controller.Util.renderText;

/**
 * A Simple Todo application controller
 */
@SuppressWarnings("unused")
@DisableFastJsonCircularReferenceDetect()
public class Todo {

    @Inject
    private JFinalDao<Integer, Account> accountDao;

    public static String rt() {
        return backendServerError();
    }

    @GetAction("e500")
    public static String backendServerError() {
        // this will trigger a runtime error in the backend
        return Act.crypto().decrypt("bad-crypted-msg");
    }

    @GetAction("/list")
    public void list(String q) {
//        List<Account> accounts = Account.dao.find("select * from account");
        Iterable<Account> accounts = accountDao.findAll();
        render(accounts);
    }

    @GetAction
    public void home() {
        List<Record> accounts = Db.find("select * from account");
        renderJson(accounts);
    }

    @GetAction("/dao")
    public void dao() {
        List<Account> accounts = accountDao.dao().find("SELECT * FROM account LIMIT 2");
        renderJson(accounts);
    }

    @GetAction("/save")
    @With(JFinalTransactional.class)
    public RenderJSON save() {
        new Account().set("name", "act test001").set("password", "123").save();
        if (1 == 1) {
            throw new RuntimeException("不通过!");
        }
        return renderJson("yes");
    }

    public static void main(String[] args) throws Exception {
        Act.start("TODO-JFinal");
    }


}
