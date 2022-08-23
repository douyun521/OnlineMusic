package servlet;

import entity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-28
 * Time: 18:25
 */
//音乐上传分为两步
//将音乐放到服务器上
//将音乐信息存入数据库中
@WebServlet("/upload")
public class UpLoad extends HttpServlet {
    private final String SAVEPATH="/root/apache-tomcat-8.5.57/webapps/onlinemusic/music";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html; charset=utf-8");
        User user = (User)req.getSession().getAttribute("user");
        if (user == null) {
            req.setAttribute("mag","请登录后上传");
        } else {
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(req);
            }catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("items:"+items );
            FileItem item = items.get(0);
            System.out.println("item： "+item);
            String fileName = item.getName();
            System.out.println("fileName"+fileName);
            req.getSession().setAttribute("fileName", fileName);
            try {
                item.write(new File(SAVEPATH, fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect("uploadsucess.html");
        }
    }
}