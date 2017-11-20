package menglulu.midterm;

import java.io.Serializable;

public class Role implements Serializable{
    private int id;
    private String name;
    private String sex;
    private String time;
    private String bornplace;
    private String force;
    private String otherinfo;
    private int picid;
    private int bgid;
    public Role(int Id, String Name, String Sex, String Time, String Bornplace, String Force, String Otherinfo, int Picid, int Bgid){
        id = Id;
        name = Name;
        sex = Sex;
        time = Time;
        bornplace = Bornplace;
        force = Force;
        otherinfo = Otherinfo;
        picid = Picid;
        bgid = Bgid;
    }
    public int getId(){return id;}
    public String getName(){return name;}
    public String getSex(){return sex;}
    public String getTime(){return time;}
    public String getBornplace(){return bornplace;}
    public String getForce(){return force;}
    public String getOtherinfo(){return otherinfo;}

    public void setId(int Id){
        id = Id;
    }

    public void setName(String Name){
        name = Name;
    }

    public void setRole(String Name, String Sex, String Time, String Bornplace, String Force, String Otherinfo){
        name = Name;
        sex = Sex;
        time = Time;
        bornplace = Bornplace;
        force = Force;
        otherinfo = Otherinfo;
    }

    public int getPicid(){return picid;}
    public int getBgid(){return bgid;}
}