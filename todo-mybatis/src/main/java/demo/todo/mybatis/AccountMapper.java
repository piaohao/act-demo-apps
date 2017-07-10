package demo.todo.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.piaohao.act.db.mybatis.BaseMapper;

import java.util.List;

public interface AccountMapper extends BaseMapper {
    List<Account> selectAll();

    void save();
}
