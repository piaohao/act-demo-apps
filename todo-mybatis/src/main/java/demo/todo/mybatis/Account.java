package demo.todo.mybatis;


import javax.persistence.Entity;
import javax.persistence.Id;

import org.beetl.sql.core.annotatoin.AutoID;;

@Entity
public class Account {

    @Id
    private Long id;

    private String name;

    public Account() {
    }

    @AutoID //beetlsql 注解
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
