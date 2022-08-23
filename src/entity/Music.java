package entity;


import java.util.Date;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-28
 * Time: 16:13
 */


public class Music {
    private int id;
    private String title;
    private String singer;
    private String url;
    private Date time;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time =  time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Music{" +
                "musicId=" + id +
                ", title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", url='" + url + '\'' +
                ", time='" + time + '\'' +
                ", userId=" + userId +
                '}';
    }
}


