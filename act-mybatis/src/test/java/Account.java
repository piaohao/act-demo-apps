import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.beetl.sql.core.annotatoin.AutoID;
import org.piaohao.act.db.mybatis.BaseMapper;

import javax.persistence.Entity;
import javax.persistence.Id;

;import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Class<? extends BaseMapper> type = AccountMapper.class;
        BaseMapper mapper = sqlSession.getMapper(type);
        List<Account> accounts = ((AccountMapper) mapper).selectAll();
        System.out.println(JSON.toJSONString(accounts));
    }
}
