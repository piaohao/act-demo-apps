package demo.todo.jfinal;

import act.Act;
import act.job.OnAppStart;
import act.util.DisableFastJsonCircularReferenceDetect;
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
        Act.start("TODO-MyBatis");
    }


}
