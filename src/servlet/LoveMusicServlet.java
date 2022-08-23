package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-27
 * Time: 14:54
 */
//添加喜欢的音乐到喜欢音乐列表
@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String idStr = req.getParameter("id");
        int musicId = Integer.parseInt(idStr);
        Map<String,Object> return_map = new HashMap<>();
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();
        MusicDao dao = new MusicDao();
        if (dao.findMusicByMusicId(user_id,musicId)) {
            System.out.println("歌曲已经存在");
            return_map.put("msg",false);
        } else {
            boolean flg = dao.insertLoveMusic(user_id,musicId);
            if (flg) {
                System.out.println("添加到喜欢成功");
                return_map.put("msg",true);
            }else {
                System.out.println("添加到喜欢失败");
                return_map.put("msg", false);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }
}
