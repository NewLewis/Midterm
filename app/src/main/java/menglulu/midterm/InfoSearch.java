package menglulu.midterm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_info);
        final CheckBox name_cbx = (CheckBox)findViewById(R.id.name_check);
        final EditText name_s =(EditText)findViewById(R.id.name_search);
        Button enter = (Button)findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InfoSearch.this, MainActivity.class);
                Bundle bundle = new Bundle();
                boolean name_checked=name_cbx.isChecked();
                if(name_checked){
                    bundle.putInt("check",1);
                    String value=name_s.getText().toString();
                    bundle.putString("value",value);
                }
                else{
                    bundle.putInt("check",0);
                }
                intent.putExtras(bundle);
                setResult(1,intent);
                finish();
            }
        });
    }

}
