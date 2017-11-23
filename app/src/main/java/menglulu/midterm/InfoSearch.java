package menglulu.midterm;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InfoSearch extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Cursor cursor;
    private SimpleCursorAdapter mAdapter;
    private SearchView mSearchView;
    private ListView mListView;
    RoleHelper roleHelper = new RoleHelper(this);
    private List<Role> roleList = new ArrayList<>();
    private ItemListAdapter itemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_info);
        mSearchView = (SearchView) findViewById(R.id.search);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);
        initDataView();
    }

    private void initDataView() {
        //mCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, PROJECTION, null, null, null);
//        cursor = roleHelper.getAll(null,null);
//        mAdapter = new SimpleCursorAdapter(this, R.layout.brief_role, cursor,
//                new String[]{""}, new int[]{}, 0);

        cursor = roleHelper.getAll(null,null);
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
            roleList.add(r);
        }
        itemListAdapter = new ItemListAdapter(this,roleList);
        mListView = (ListView) findViewById(R.id.item);
        mListView.setAdapter(itemListAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (mSearchView != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }
            mSearchView.clearFocus(); // 不获取焦点
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.d("InforSearch", "onQueryTextChange(), newText=" + newText);
        roleList.clear();
        try {
            cursor = roleHelper.getByLikeName(newText);
        }catch (Exception e){
            System.out.println("从数据库获取cursor出错");
        }

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
            roleList.add(r);
        }

        itemListAdapter.notifyDataSetChanged(); // 交换指针，展示新的数据
        return true;
    }

}
