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
@WebServlet("/removeLoveServlet")
public class RemoveLoveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        Map<String,Object> return_map = new HashMap<>();
        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();
        String idStr = req.getParameter("id");
        int music_id = Integer.parseInt(idStr);
        MusicDao dao = new MusicDao();
        int delete = dao.removeLoveMusic(user_id,music_id);
        if (delete == 1) {
            System.out.println("删除喜欢音乐成功");
            return_map.put("msg",true);
        }else {
            System.out.println("删除喜欢音乐失败");
            return_map.put("msg",false);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),return_map);
    }

}
