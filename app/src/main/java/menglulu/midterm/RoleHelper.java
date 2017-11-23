package menglulu.midterm;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

class RoleHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Rolelist.db";//数据库名称
    private static final int SCHEMA_VERSION=1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    //创建数据库的语句
    final private String  CREATE_CATEGORY = "CREATE TABLE roles (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, sex TEXT, time TEXT, bornplace TEXT, force TEXT, otherinfo TEXT, picid INTEGER, bgid INTEGER);";
    String[] Name=new String[]{"曹操","曹丕","刘备","关羽","张飞","诸葛亮","赵云","周瑜","张角","司马懿"};
    String[] Sex=new String[]{"男","男","男","男","男","男","男","男","男","男"};
    String[] Time=new String[]{"155年－220年3月15日","187年冬—226年6月29日","161年－223年6月10日","？－220年","？－221年","181年-234年10月8日","？－229年","175年-210年","？－184年","179年—251年9月7日"};
    String[] Bornplace=new String[]{"沛国谯县（今安徽亳州）","沛国谯县（今安徽亳州）","幽州涿郡涿县（今河北涿州）","河东郡解县（今山西运城）","幽州涿郡（今河北省保定市涿州市）","徐州琅琊阳都（今山东临沂市沂南县）","常山真定（今河北省正定）","庐江舒（今合肥庐江舒县）","钜鹿（治今河北省邢台市巨鹿县）","河内郡温县孝敬里（今河南省焦作市温县）"};
    String[] Force=new String[]{"魏","魏","蜀","蜀","蜀","蜀","蜀","吴","其他","晋"};

    int []BgImageId=new int[]{R.mipmap.b_caocao, R.mipmap.b_caopi,R.mipmap.b_liubei,R.mipmap.b_guanyu,R.mipmap.b_zhangfei,R.mipmap.b_zhugeliang,R.mipmap.b_zhaoyun,R.mipmap.b_zhouyu,R.mipmap.b_zhangjiao,R.mipmap.b_simayi};
    int []CImageId=new int[]{R.mipmap.c_caocao, R.mipmap.c_caopi,R.mipmap.c_liubei,R.mipmap.c_guanyu,R.mipmap.c_zhangfei,R.mipmap.c_zhugeliang,R.mipmap.c_zhaoyun,R.mipmap.c_zhouyu,R.mipmap.c_zhangjiao,R.mipmap.c_simayi};

    public RoleHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY);
        for(int i=0; i<10; i++){
            ContentValues cv=new ContentValues();
            cv.put("name", Name[i]);
            cv.put("sex", Sex[i]);
            cv.put("time", Time[i]);
            cv.put("bornplace", Bornplace[i]);
            cv.put("force", Force[i]);
            cv.put("otherinfo", "Otherinfo");
            cv.put("picid", CImageId[i]);
            cv.put("bgid", BgImageId[i]);
            db.insert("roles","null",cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //检测升级方法
        switch(oldVersion){
            case 1:
                db.execSQL(CREATE_CATEGORY);
            default:
        }
    }

    public Cursor getAll(String where, String orderBy) {//返回表中的数据,where是调用时候传进来的搜索内容,orderby是设置中传进来的列表排序类型
        StringBuilder buf=new StringBuilder("SELECT _id, name, sex, time, bornplace, force, otherinfo, picid, bgid FROM roles");

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }

        return(getReadableDatabase().rawQuery(buf.toString(), null));
    }

    public Cursor getById(String id) {//根据点击事件获取id,查询数据库
        String[] args={id};

        return(getReadableDatabase()
                .rawQuery("SELECT _id, name, sex, time, bornplace, force, otherinfo, picid, bgid FROM roles WHERE _ID=?",
                        args));
    }

    public Cursor getByName(String Name) {//根据点击事件获取id,查询数据库
        String[] args={Name};

        return(getReadableDatabase()
                .rawQuery("SELECT _id, name, sex, time, bornplace, force, otherinfo, picid, bgid FROM roles WHERE name = ?",
                        args));
    }

    public Cursor getByLikeName(String name) {//根据点击事件获取id,查询数据库
        name = "%" + name + "%";
        String[] args={name};

        return(getReadableDatabase()
                .rawQuery("SELECT _id, name, sex, time, bornplace, force, otherinfo, picid, bgid FROM roles WHERE name like ?", args));
    }

    public void insert(String Name, String Sex, String Time, String Bornplace, String Force, String Otherinfo, int Picid, int Bgid) {
        ContentValues cv=new ContentValues();
        cv.put("name", Name);
        cv.put("sex", Sex);
        cv.put("time", Time);
        cv.put("bornplace", Bornplace);
        cv.put("force", Force);
        cv.put("otherinfo", Otherinfo);
        cv.put("picid", Picid);
        cv.put("bgid", Bgid);
        getWritableDatabase().insert("roles", "null", cv);
    }

    public void update(String id, String Name, String Sex, String Time, String Bornplace, String Force, String Otherinfo, int Picid, int Bgid) {
        ContentValues cv=new ContentValues();
        String[] args={id};
        cv.put("name", Name);
        cv.put("sex", Sex);
        cv.put("time", Time);
        cv.put("bornplace", Bornplace);
        cv.put("force", Force);
        cv.put("otherinfo", Otherinfo);
        cv.put("picid", Picid);
        cv.put("bgid", Bgid);
        getWritableDatabase().update("roles", cv, "_ID=?", args);
    }

    public void delete(String id){
        String[] args={id};
        getWritableDatabase().delete("roles", "_ID=?", args);
    }

    public int getId(Cursor c) {
        return(c.getInt(0));
    }

    public String getName(Cursor c) {
        return(c.getString(1));
    }

    public String getSex(Cursor c) {
        return(c.getString(2));
    }

    public String getTime(Cursor c) {
        return(c.getString(3));
    }

    public String getBornplace(Cursor c) {
        return(c.getString(4));
    }

    public String getForce(Cursor c) {
        return(c.getString(5));
    }

    public String getOtherinfo(Cursor c) {
        return(c.getString(6));
    }

    public int getPicid(Cursor c) {
        return(c.getInt(7));
    }

    public int getBgid(Cursor c) {
        return(c.getInt(8));
    }
}