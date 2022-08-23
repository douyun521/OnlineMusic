package dao;

import entity.User;
import DBUtil.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:有关用户相关的数据库的操作
 * User:douYun
 * Date: 2020-02-26
 * Time: 22:44
 */
public class UserDao {
    public User findUserByUsername (String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        User user = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "select * from user where username = ? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1,name);
            set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setId(set.getInt("id"));
                user.setUserName(set.getString("username"));
                user.setPassword(set.getString("password"));
                user.setAge(set.getInt("age"));
                user.setGender(set.getString("gender"));
                user.setEmail(set.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            JDBCUtil.close(connection,statement,set);
        }
        return user;
    }

    //登录
    public User login(User loginUser) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet set = null;
        User user = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "select * from user where username = ? and password = ?";
            statement = connection.prepareStatement(sql);//对sql语句的预编译
            statement.setString(1,loginUser.getUserName());
            statement.setString(2,loginUser.getPassword());
            //执行sql语句
            set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setId(set.getInt("id"));
                user.setUserName(set.getString("username"));
                user.setPassword(set.getString("password"));
                user.setAge(set.getInt("age"));
                user.setGender(set.getString("gender"));
                user.setEmail(set.getString("email"));
            }


        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            JDBCUtil.close(connection,statement,set);
        }
        return user;
    }
    //注册
    public int register (User user) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        int ret = 0;
        try {
            String sql = "insert into user values (null,?,?,?,?,?) ";
            statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUserName());
            statement.setString(2,user.getPassword());
            statement.setInt(3,user.getAge());
            statement.setString(4,user.getGender());
            statement.setString(5,user.getEmail());
            ret = statement.executeUpdate();
            if (ret != 1) {
                System.out.println("注册失败");
                return 0;
            }
            System.out.println("注册成功");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,null);
        }
        return ret;
    }

    /*public static void main(String[] args) {
        UserDao dao = new UserDao();
        User user = new User();
        user.setUserName("bit");
        user.setPassword("123");
        User s = dao.login(user);
        System.out.println(s);
    }*/
}