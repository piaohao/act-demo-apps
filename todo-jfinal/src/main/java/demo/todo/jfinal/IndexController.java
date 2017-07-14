package demo.todo.jfinal;

import com.jfinal.core.Controller;
import demo.todo.jfinal.model.Account;

public class IndexController extends Controller {

    public void list() {
        renderJson(Account.dao.find("select * from account"));
    }
}
