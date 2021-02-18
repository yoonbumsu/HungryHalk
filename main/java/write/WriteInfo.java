package write;

import java.util.ArrayList;
import java.util.Date;

public class WriteInfo {
    private String title;
    private String contents;
    private String userid;
    private String imageurl;

    public WriteInfo(String title, String contents, String userid){
        this.title = title;
        this.contents = contents;
        this.userid = userid;
        this.imageurl=imageurl;

    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContents(){
        return this.contents;
    }
    public void setContents(String contents){
        this.contents = contents;
    }
    public String getUserId(){
        return this.userid;
    }
    public void setUserId(String userid){
        this.userid = userid;

    }
    public String getImageurl(){
        return this.imageurl;
    }
    public void setImageurl(String imageurl){
        this.imageurl = imageurl;

    }



}