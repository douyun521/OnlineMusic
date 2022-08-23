package servlet;

import dao.MusicDao;
import entity.Music;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created with IntelliJ IDEA.
 * Description:将音乐信息写入数据库
 * User: douYun
 * Date: 2021-03-1
 * Time: 11:23
 */

@WebServlet("/uploadsucess")
public class Uploadsucess extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");

        String string = (String)req.getSession().getAttribute("fileName");
        String[] strings = string.split("\\.");
        String title = strings[0];
        System.out.println("title:" + title);
        String url = "music/" + title;
        String singer = req.getParameter("singer");

        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time =  sdf.format(new Date());

        MusicDao dao = new MusicDao();
        int num = dao.insert(title,singer,time,url,user_id);
        if (num != 0) {
            resp.sendRedirect("list.html");
        }
    }
}