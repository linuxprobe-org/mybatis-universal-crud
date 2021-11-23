package org.rdlinux;

import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlpart.EzTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SelectTest extends BaseTest {
    @Test
    public void selectById() {
        User user = BaseTest.sqlSession.getMapper(UserMapper.class).selectById("980e1f193035494198f90d24e01d6706");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("980e1f193035494198f90d24e01d6706");
        ids.add("1s");
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectBySql() {
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).selectBySql("select * from \"user\"");
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectOneBySql() {
        User user = BaseTest.sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from \"user\" " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        Map<String, Object> user = BaseTest.sqlSession.getMapper(UserMapper.class).selectOneMapBySql("select * from \"user\" " +
                "where id = '1s'");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectMapBySql() {
        List<Map<String, Object>> users = BaseTest.sqlSession.getMapper(UserMapper.class)
                .selectMapBySql("select * from \"user\"");
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void queryTest() {
        EzQuery query = EzQuery.builder().from(EzTable.of(User.class)).orderBy().add("name").done().page(2, 5).build();
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        int i = BaseTest.sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }
}