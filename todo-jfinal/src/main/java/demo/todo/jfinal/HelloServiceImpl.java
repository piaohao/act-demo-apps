package demo.todo.jfinal;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "hello act-dubbo!";
    }
}
