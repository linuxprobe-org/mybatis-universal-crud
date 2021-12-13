package org.rdlinux;

import org.junit.Test;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

public class UpdateTest extends BaseTest {
    @Test
    public void update() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex("女");
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).update(user);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex("女");
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchUpdate(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void replace() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).replace(user);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchReplace() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex("女");
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = BaseTest.sqlSession.getMapper(UserMapper.class).batchReplace(users);
        BaseTest.sqlSession.commit();
        System.out.println(insert);
    }
}
