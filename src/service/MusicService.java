package service;

import dao.MusicDao;
import entity.Music;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: douYun
 * Date: 2021-02-28
 * Time: 11:23
 */

public class MusicService {
    public List<Music> findMusic(){
        MusicDao musicDao=new MusicDao();
        List<Music>musicList=musicDao.findMusic();
        return musicList;
    }
}
