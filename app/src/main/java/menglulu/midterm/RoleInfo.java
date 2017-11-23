package menglulu.midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoleInfo extends AppCompatActivity{

    Role role;
    int index;
    int add_f;

    int edit_f;

    private TextView name;
    private TextView time;
    private TextView sex;
    private TextView bornplace;
    private TextView force;
    private TextView otherinfo;

    private EditText name_e;
    private EditText time_e;
    private EditText sex_e;
    private EditText bornplace_e;
    private EditText force_e;
    private EditText otherinfo_e;


    ImageButton pic;
    ImageButton edit;


    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;
    public static final int REQUEST_CROP = 3;

    private File mImageFile;

    RoleHelper roleHelper = new RoleHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_role);

        name = (TextView)findViewById(R.id.name_text);
        time = (TextView)findViewById(R.id.time_text);
        sex = (TextView)findViewById(R.id.sex_text);
        bornplace = (TextView)findViewById(R.id.bornplace_text);
        force = (TextView)findViewById(R.id.force_text);
        otherinfo = (TextView)findViewById(R.id.otherinfo_text);

        name_e = (EditText)findViewById(R.id.name_edit);
        time_e = (EditText)findViewById(R.id.time_edit);
        sex_e = (EditText)findViewById(R.id.sex_edit);
        bornplace_e = (EditText)findViewById(R.id.bornplace_edit);
        force_e = (EditText)findViewById(R.id.force_edit);
        otherinfo_e = (EditText)findViewById(R.id.otherinfo_edit);

        pic = (ImageButton) findViewById(R.id.imageView);
        edit = (ImageButton)findViewById(R.id.edit);

        edit_f=0; //初始化为0,用于判断edit的状态

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        role = (Role) bundle.get("Role");
        index = bundle.getInt("Index");

        if(bundle.containsKey("Add")) {
            add_f = bundle.getInt("Add");
            edit_f=1;
            name.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);
            sex.setVisibility(View.INVISIBLE);
            bornplace.setVisibility(View.INVISIBLE);
            force.setVisibility(View.INVISIBLE);
            otherinfo.setVisibility(View.INVISIBLE);

            name_e.setVisibility(View.VISIBLE);
            time_e.setVisibility(View.VISIBLE);
            sex_e.setVisibility(View.VISIBLE);
            bornplace_e.setVisibility(View.VISIBLE);
            force_e.setVisibility(View.VISIBLE);
            otherinfo_e.setVisibility(View.VISIBLE);
        }


        name.setText(role.getName());
        time.setText(role.getTime());
        sex.setText(role.getSex());
        bornplace.setText(role.getBornplace());
        force.setText(role.getForce());
        otherinfo.setText(role.getOtherinfo());
        pic.setBackgroundResource(role.getPicid());

        name_e.setText(role.getName());
        time_e.setText(role.getTime());
        sex_e.setText(role.getSex());
        bornplace_e.setText(role.getBornplace());
        force_e.setText(role.getForce());
        otherinfo_e.setText(role.getOtherinfo());




        //action: edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_f==0){
                    name.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.INVISIBLE);
                    sex.setVisibility(View.INVISIBLE);
                    bornplace.setVisibility(View.INVISIBLE);
                    force.setVisibility(View.INVISIBLE);
                    otherinfo.setVisibility(View.INVISIBLE);

                    name_e.setVisibility(View.VISIBLE);
                    time_e.setVisibility(View.VISIBLE);
                    sex_e.setVisibility(View.VISIBLE);
                    bornplace_e.setVisibility(View.VISIBLE);
                    force_e.setVisibility(View.VISIBLE);
                    otherinfo_e.setVisibility(View.VISIBLE);


                    edit_f=1;
                }
                else{
                    //点击人物头像选择图片
                    pic.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            selectAlbum();
                        }
                    });
                    role.setRole(name_e.getText().toString(),
                            time_e.getText().toString(),
                            sex_e.getText().toString(),
                            bornplace_e.getText().toString(),
                            force_e.getText().toString(),
                            otherinfo_e.getText().toString());

                    name.setText(role.getName());
                    time.setText(role.getTime());
                    sex.setText(role.getSex());
                    bornplace.setText(role.getBornplace());
                    force.setText(role.getForce());
                    otherinfo.setText(role.getOtherinfo());

                    name.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    sex.setVisibility(View.VISIBLE);
                    bornplace.setVisibility(View.VISIBLE);
                    force.setVisibility(View.VISIBLE);
                    otherinfo.setVisibility(View.VISIBLE);

                    name_e.setVisibility(View.INVISIBLE);
                    time_e.setVisibility(View.INVISIBLE);
                    sex_e.setVisibility(View.INVISIBLE);
                    bornplace_e.setVisibility(View.INVISIBLE);
                    force_e.setVisibility(View.INVISIBLE);
                    otherinfo_e.setVisibility(View.INVISIBLE);

                    edit_f=0;

                    roleHelper.insert(role.getName(),role.getSex(),role.getTime(),role.getBornplace(),role.getForce(),role.getOtherinfo(),role.getPicid(),role.getBgid());
                    Cursor c = roleHelper.getByName(role.getName());
                    while(c.moveToNext()){
                        role.setId(roleHelper.getId(c));
                    }

                }
            }
        });
    }


    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }
    private void cropImage(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(intent, REQUEST_CROP);
    }

    private void createImageFile() {
        mImageFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        try {
            if(mImageFile.exists()){
                mImageFile.delete();
            }
            mImageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "出错啦", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                cropImage(Uri.fromFile(mImageFile));
                break;
            case REQUEST_ALBUM:
                createImageFile();
                if (!mImageFile.exists()) {
                    return;
                }
                Uri uri = data.getData();
                if (uri != null) {
                    cropImage(uri);
                }
                break;
            case REQUEST_CROP:
                pic.setImageURI(Uri.fromFile(mImageFile));
                break;
        }
    }

}


//name
//time
//sex
//bornplace
//force
//otherinfo