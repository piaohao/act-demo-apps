package demo.todo.mybatis;

import static act.controller.Controller.Util.notFoundIfNull;
import static act.controller.Controller.Util.renderJson;
import static act.controller.Controller.Util.renderText;

import javax.inject.Inject;

import act.util.DisableFastJsonCircularReferenceDetect;
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

    @Inject
    private AccountMapper mapper;

    @GetAction
    public void home() {
    }

    @GetAction("/list")
    public RenderJSON list(String q) {
        return renderJson(mapper.selectAll());
    }

    @GetAction("/save")
    public RenderText save() {
        mapper.save();
        return renderText("mapper.save()");
    }

    public static void main(String[] args) throws Exception {
        Act.start("TODO-MyBatis");
    }


}
