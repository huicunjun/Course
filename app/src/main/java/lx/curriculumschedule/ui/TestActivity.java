package lx.curriculumschedule.ui;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lx.curriculumschedule.R;
import lx.curriculumschedule.db.DBAdapter;
import lx.curriculumschedule.db.DBHelper;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEd;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();

/*        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper() {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        sqLiteOpenHelper.getWritableDatabase().insert("",爱人法涉非法);*/




        final DBAdapter 梁安华专用工具类 = new DBAdapter(this);

      //  梁安华专用工具类.insert(435,编辑框内容);
      //  梁安华专用工具类.insert(2,"撒发生污染的思维");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String 编辑框内容 = mEd.getText().toString();
             //   Toast.makeText(TestActivity.this, name+"已添加", Toast.LENGTH_SHORT).show();
                梁安华专用工具类.insert(435,编辑框内容);
            }
        });
    }

    private void initView() {
        mEd = (EditText) findViewById(R.id.ed);
        mButton = (Button) findViewById(R.id.button);
      //  mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.button:
                break;
        }
    }
}
