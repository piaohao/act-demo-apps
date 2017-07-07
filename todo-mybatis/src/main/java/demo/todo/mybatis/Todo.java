package demo.todo.mybatis;

import static act.controller.Controller.Util.notFoundIfNull;
import static act.controller.Controller.Util.renderJson;

import javax.inject.Inject;

import act.util.DisableFastJsonCircularReferenceDetect;
import org.osgl.mvc.annotation.GetAction;

import act.Act;
import org.osgl.mvc.result.RenderJSON;

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
        // mapper.all();
        return renderJson(mapper.selectAll());
    }

    public static void main(String[] args) throws Exception {
        Act.start("TODO-MyBatis");
    }


}
