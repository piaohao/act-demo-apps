package demo.todo.mybatis;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Account {

    @Id
    private Long id;

    private String name;

    public Account() {
    }

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
