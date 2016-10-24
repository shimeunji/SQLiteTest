package kr.hs.emirim.kokopink1.sqlitetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    EditText editname,editcount,editNameResult,editCountResult;
    Button butInit,butInput,butSelect;
    MyDBhelper dBhelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dBhelper=new MyDBhelper(getApplicationContext());
        setContentView(R.layout.activity_main);
        editname=(EditText)findViewById(R.id.edit_groupname);
        editcount=(EditText)findViewById(R.id.edit_groupcount);
        editNameResult=(EditText)findViewById(R.id.edit_name_result);
        editCountResult=(EditText)findViewById(R.id.edit_count_result);
        butInit=(Button)findViewById(R.id.but_init);
        butInput=(Button)findViewById(R.id.but_input);
        butSelect=(Button)findViewById(R.id.but_select);

        butInit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                db=dBhelper.getWritableDatabase();
                dBhelper.onUpgrade(db,1,2);
                db.close();
            }
        });

        butInput.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                db=dBhelper.getWritableDatabase();
                db.execSQL("insert into idoltable values("+editname.getText().toString()+","
                        +editcount.getText().toString()
                        +")"); //변경되는거 ㅎㅎ
                db.close();
                Toast.makeText(getApplicationContext(),"정상적으로 입력완료!",Toast.LENGTH_SHORT);
            }
        });
        butSelect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                db=dBhelper.getReadableDatabase();
                Cursor cursor=db.rawQuery("select * from idoltable",null);
                String gname="그룹이름"+"\n"+"========"+"\n";
                String gcount="인원수"+"\n"+"========"+"\n";

                while(cursor.moveToNext())
                {
                    gname+=cursor.getString(0)+"\n";
                    gcount+=cursor.getString(1)+"\n";
                }
                editNameResult.setText(gname);
                editCountResult.setText(gcount);
            }
        });

    }

    public class MyDBhelper extends SQLiteOpenHelper{
        public MyDBhelper(Context context)
        {
            super(context,"idoldb",null,1);

        }
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("create table idoltable(gname char(40) primary key,gcount integer);");
        }
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {
            db.execSQL("drop table if exists idoltable");
            onCreate(db);
        }
    }
}
