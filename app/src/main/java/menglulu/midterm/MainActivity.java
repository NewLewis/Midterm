package menglulu.midterm;


import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.annotation.IdRes;
import android.support.annotation.InterpolatorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.util.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static android.graphics.Color.GREEN;
import static android.hardware.camera2.params.RggbChannelVector.RED;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    CommonAdapter commonAdapter;
    List<Map<String,Object>> listItems;
    List<Role> data;
    RoleHelper roleHelper = new RoleHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase database = roleHelper.getWritableDatabase();

        data=new ArrayList<>();
        listItems=new ArrayList<>();

        Cursor cursor = roleHelper.getAll(null,null);
        int i_t=0;
        while(cursor.moveToNext()){
            int id = roleHelper.getId(cursor);
            Role r = new Role(id,
                    roleHelper.getName(cursor),
                    roleHelper.getSex(cursor),
                    roleHelper.getTime(cursor),
                    roleHelper.getBornplace(cursor),
                    roleHelper.getForce(cursor),
                    roleHelper.getOtherinfo(cursor),
                    roleHelper.getPicid(cursor),
                    roleHelper.getBgid(cursor)
            );
            data.add(r);
            Map<String,Object> tem=new LinkedHashMap<>();
            tem.put("name",roleHelper.getName(cursor));
            tem.put("src",roleHelper.getBgid(cursor));
            tem.put("index",i_t);
            listItems.add(tem);
            i_t++;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight()*3);
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        commonAdapter = new CommonAdapter<Map<String, Object>>(this, R.layout.brief_role,listItems) {
            @Override
            protected void convert(ViewHolder holder, Map<String, Object> o) {
                TextView name = holder.getView(R.id.name);
                name.setText(o.get("name").toString());
                name.setBackgroundResource(Integer.parseInt(o.get("src").toString()));
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, RoleInfo.class);
                Bundle bundle = new Bundle();
                int i = Integer.parseInt(listItems.get(position).get("index").toString());
                bundle.putSerializable("Role",data.get(i));
                bundle.putInt("Index",i);

                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
            @Override
            public void onLongClick(int position){
                Toast.makeText(getApplicationContext(),"移除角色"+data.get(position).getName(),Toast.LENGTH_SHORT).show();
                commonAdapter.Remove(position);
                roleHelper.delete(Integer.toString(data.get(position).getId()));

            }
        });

//        ImageButton addRole = (ImageButton)findViewById(R.id.addrole);
//        addRole.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                //create a new role
//
//                Role newRole = new Role(1,"角色名","性别","生卒年月","出生地","主效势力","其他信息",R.mipmap.c_caocao,R.mipmap.b_caocao);
//                roleHelper.insert("角色名","性别","生卒年月","出生地","主效势力","其他信息",R.mipmap.c_caocao,R.mipmap.b_caocao);
//                Cursor c = roleHelper.getByName("角色名");
//                while(c.moveToNext()){
//                    newRole.setId(roleHelper.getId(c));
//                }
//
//                int i=data.size();
//                data.add(newRole);
//                Map<String,Object> tem=new LinkedHashMap<>();
//                tem.put("name",newRole.getName());
//                tem.put("src",newRole.getBgid());
//                tem.put("index",i);
//                listItems.add(tem);
//
//                Intent intent = new Intent(MainActivity.this, RoleInfo.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Role",newRole);
//                bundle.putInt("Index",i);
//                bundle.putInt("Add",1);
//                intent.putExtras(bundle);
//                startActivityForResult(intent,1);
//            }
//        });

//        ImageButton searchRole=(ImageButton)findViewById(R.id.search);
//        searchRole.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(MainActivity.this, InfoSearch.class);
//                startActivityForResult(intent,1);
//            }
//        });

        database.close();

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(commonAdapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(true);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAdapter.setFirstOnly(true);
        scaleAdapter.setDuration(1000);
        mRecyclerView.setAdapter(scaleAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
    }

    @Override
    public boolean onMenuOpened(int featured,Menu menu){
        setOverflowIconVisible(featured,menu);
        return super.onMenuOpened(featured,menu);
    }

    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.newOne:
                newRole();
                break;
            case R.id.search:
                searchRole();
                break;
            case R.id.flash:
                commonAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void newRole(){
        Role newRole = new Role(1,"角色名","性别","生卒年月","出生地","主效势力","其他信息",R.mipmap.c_caocao,R.mipmap.b_caocao);
        roleHelper.insert("角色名","性别","生卒年月","出生地","主效势力","其他信息",R.mipmap.c_caocao,R.mipmap.b_caocao);
        Cursor c = roleHelper.getByName("角色名");
        while(c.moveToNext()){
            newRole.setId(roleHelper.getId(c));
        }

        int i=data.size();
        data.add(newRole);
        Map<String,Object> tem=new LinkedHashMap<>();
        tem.put("name",newRole.getName());
        tem.put("src",newRole.getBgid());
        tem.put("index",i);
        listItems.add(tem);

        Intent intent = new Intent(MainActivity.this, RoleInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Role",newRole);
        bundle.putInt("Index",i);
        bundle.putInt("Add",1);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    private void searchRole(){
        Intent intent = new Intent(MainActivity.this, InfoSearch.class);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int request, int result, Intent intent){
        if(request==1){
            if(result==1){
                Bundle bundle = intent.getExtras();
                if(bundle!=null) {
                    if(bundle.containsKey("check")){
                        if(bundle.getInt("check")!=0)
                        {
                            data.clear();
                            listItems.clear();
                            String search_value = bundle.getString("value");
                            Cursor cursor = roleHelper.getByName(search_value);
                            int i_t=0;
                            while(cursor.moveToNext()){
                                int id = roleHelper.getId(cursor);
                                Role r = new Role(id,
                                        roleHelper.getName(cursor),
                                        roleHelper.getSex(cursor),
                                        roleHelper.getTime(cursor),
                                        roleHelper.getBornplace(cursor),
                                        roleHelper.getForce(cursor),
                                        roleHelper.getOtherinfo(cursor),
                                        roleHelper.getPicid(cursor),
                                        roleHelper.getBgid(cursor)
                                );
                                data.add(r);
                                Map<String,Object> tem=new LinkedHashMap<>();
                                tem.put("name",roleHelper.getName(cursor));
                                tem.put("src",roleHelper.getBgid(cursor));
                                tem.put("index",i_t);
                                listItems.add(tem);
                                i_t++;
                            }
                            commonAdapter.notifyDataSetChanged();
                        }
                        else{
                            data.clear();
                            listItems.clear();
                            String search_value = bundle.getString("value");
                            Cursor cursor = roleHelper.getAll(null,null);
                            int i_t=0;
                            while(cursor.moveToNext()){
                                int id = roleHelper.getId(cursor);
                                Role r = new Role(id,
                                        roleHelper.getName(cursor),
                                        roleHelper.getSex(cursor),
                                        roleHelper.getTime(cursor),
                                        roleHelper.getBornplace(cursor),
                                        roleHelper.getForce(cursor),
                                        roleHelper.getOtherinfo(cursor),
                                        roleHelper.getPicid(cursor),
                                        roleHelper.getBgid(cursor)
                                );
                                data.add(r);
                                Map<String,Object> tem=new LinkedHashMap<>();
                                tem.put("name",roleHelper.getName(cursor));
                                tem.put("src",roleHelper.getBgid(cursor));
                                tem.put("index",i_t);
                                listItems.add(tem);
                                i_t++;
                            }
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                    else if(bundle.containsKey("Role")) {
                        Role role = (Role) bundle.get("Role");
                        int index = bundle.getInt("Index");
                        data.set(index, role);
                        Map<String, Object> tem = new LinkedHashMap<>();
                        tem.put("name", role.getName());
                        tem.put("src", role.getBgid());
                        tem.put("index", index);
                        listItems.set(index, tem);
                        commonAdapter.notifyDataSetChanged();

                        SQLiteDatabase database = roleHelper.getWritableDatabase();
                        roleHelper.update(Integer.toString(role.getId()),
                                role.getName(),
                                role.getSex(),
                                role.getTime(),
                                role.getBornplace(),
                                role.getForce(),
                                role.getOtherinfo(),
                                role.getPicid(),
                                role.getBgid());
                        database.close();
                    }
                }

            }
        }
    }
}
