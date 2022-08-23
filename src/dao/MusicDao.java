package dao;

import entity.Music;
import DBUtil.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:有关音乐的数据库操作
 * User:douYun
 * Date: 2020-02-26  str歌曲名
 * Time: 22:44
 */
public class MusicDao {
    //查询全部歌单
    public List<Music> findMusic() {
        List<Music> list = new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select *from music";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserId(rs.getInt("userid"));
                list.add(music);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,rs);
        }
    }

    //根据id查找音乐
    public Music findMusicById(int id) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        Music music = null;
        try {
            String sql = "select * from music where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            rs = statement.executeQuery();
            if (rs.next()) {
                music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserId(rs.getInt("userid"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,rs);
        }
        return music;
    }

    //根据关键字查询歌单包括模糊查询
    public List<Music> ifMusiclove(String str){
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Music> list = new ArrayList<>();
        try {
            String sql = "select*from music where title like '%"+str+"%'";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserId(rs.getInt("userid"));
                list.add(music);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,rs);
        }
        return list;
    }

    //上传音乐  可以不给这么多参数，其实可以给一个music对象
    //a：上传文件本身给服务器 b：讲音乐信息，插入到数据库当中 我们此时做的就是这步
    public int insert(String title, String singer, String time, String url,
                      int userid) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        try{
            String sql = "insert into music(title,singer,time,url,userid) values(?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,title);
            statement.setString(2,singer);
            statement.setString(3,time);
            statement.setString(4,url);
            statement.setInt(5,userid);
            int ret = statement.executeUpdate();
            //返回值代表当前受影响的行数
            if (ret != 1) {
                System.out.println("上传失败");
                return 0;
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,null);
        }
    }

    //删除歌曲
    public int deleteMusicById(int id) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "delete from music where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            int ret = statement.executeUpdate();
            if(ret == 1) {
                //删除music中的数据 同时删除中间表(lovemusic)中的数据
                // 1、看中间表是否有数据，如果有删除
                if(findLoveMusicOnDel(id)) {
                    int ret2 = removeLoveMusicOnDelete(id);
                    if(ret2 == 1){
                        return 1;
                    }
                }
                return 1;//如果没有找到，说明这首歌，没有被添加到喜欢的列表
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;//没有删除成功
    }


    //判断这首歌有没有被添加为喜欢
    public boolean findLoveMusicOnDel(int id) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "select * from lovemusic where id = ? ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,rs);
        }
    }



    //移除这首歌lovemusic
    public int removeLoveMusicOnDelete(int id) {
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "delete from lovemusic where id = ? ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            int ret = statement.executeUpdate();
            if (ret != 1) {
                return 0;
            }
            return ret;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(connection,statement,rs);
        }
    }
    //移除当前用户喜欢的这首音乐，因为同一首音乐可能多个用户喜欢

    public int removeLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            String sql = "delete from lovemusic where user_id=? and music_id=?";
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,musicId);
            int ret = preparedStatement.executeUpdate();
            if(ret == 1) {
                return ret;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(connection,preparedStatement,null);
        }
        return 0;
    }


    // 添加喜欢的音乐的时候，需要先判断该音乐是否存在

    public boolean findMusicByMusicId(int user_id,int musicID) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement("select * from lovemusic where music_id=? and user_id=?");
            ps.setInt(1,musicID);
            ps.setInt(2,user_id);
            rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            JDBCUtil.close(conn, ps, rs);
        }
        return false;
    }
    //查询用户喜欢的所有音乐

    public List<Music> findLoveMusic(int user_id){
        List<Music> musics = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            ps = conn.prepareStatement("select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=?");
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while(rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserId(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            JDBCUtil.close(conn, ps, rs);
        }
        return musics;
    }
    //根据关键字查询喜欢的歌单
    public List<Music> ifMusicLove(String str,int user_id){
        List<Music> musics = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            //ps = conn.prepareStatement("select*from music where title like '%"+str+"%'");
            ps = conn.prepareStatement("select m.id as music_id,title,singer,time,url,userid from lovemusic lm,music m where lm.music_id=m.id and user_id=? and  title like '%"+str+"%'");
            ps.setInt(1,user_id);
            rs = ps.executeQuery();
            while(rs.next()) {
                Music music = new Music();
                music.setId(rs.getInt("music_id"));
                music.setTitle(rs.getString("title"));
                music.setSinger(rs.getString("singer"));
                music.setTime(rs.getDate("time"));
                music.setUrl(rs.getString("url"));
                music.setUserId(rs.getInt("userid"));
                musics.add(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            JDBCUtil.close(conn, ps, rs);
        }
        return musics;
    }
    //添加音乐到喜欢的列表
    public boolean insertLoveMusic(int userId,int musicId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into lovemusic(user_id, music_id) VALUES (?,?)";
            connection = JDBCUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,musicId);
            int ret = preparedStatement.executeUpdate();
            if (ret == 1) {
                return true;
            }
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(connection,preparedStatement,null);
        }
        return false;
    }

}