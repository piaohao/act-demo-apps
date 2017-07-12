package demo.todo.jfinal;

import static act.controller.Controller.Util.notFoundIfNull;
import static act.controller.Controller.Util.renderJson;
import static act.controller.Controller.Util.renderText;

import javax.inject.Inject;

import act.job.OnAppStart;
import act.util.DisableFastJsonCircularReferenceDetect;
import demo.todo.jfinal.model.Account;
import org.osgl.mvc.annotation.Before;
import org.osgl.mvc.annotation.GetAction;

import act.Act;
import org.osgl.mvc.result.RenderJSON;
import org.osgl.mvc.result.RenderText;

/**
 * A Simple Todo application controller
 */
@SuppressWarnings("unused")
@DisableFastJsonCircularReferenceDetect()
public class Todo {

    @GetAction
    public void home() {
    }

    @GetAction("/list")
    public RenderJSON list(String q) {
        return renderJson(Account.dao.find("select * from account"));
    }

    @GetAction("/save")
    public RenderText save() {
        return renderText("mapper.save()");
    }

    @OnAppStart
    public static void onAppStart() {
        MyConfig.init();
    }

    public static void main(String[] args) throws Exception {
        Act.start("TODO-MyBatis");
    }


}
