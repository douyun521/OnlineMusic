package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/findLoveMusic")/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-27
 * Time: 14:54
 */
public class FindLoveMusic extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        String musicName = req.getParameter("loveMusicName");
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();
        MusicDao dao = new MusicDao();
        List<Music> musics = null;
        if (musicName != null) {
            musics = dao.ifMusicLove(musicName,user_id);
        }else  {
            musics = dao.findLoveMusic(user_id);
        }
        for (Music music : musics) {
            System.out.println(music.getUrl());
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),musics);
    }
}
