package yanshengliu.activitytest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_item:
                Toast.makeText(FirstActivity.this,"YOU CLICK Add!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(FirstActivity.this,"YOU CLICK Remove!",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1=(Button)findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirstActivity.this,"YOU CLICK BUTTON_1!",Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent("yanshengliu.activitytest.ACTION_START");
                //intent.addCategory("yanshengliu.activitytest.My_CATEGORY");

                //Intent intent =new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse("http://www.google.com"));

                //Intent intent = new Intent(Intent.ACTION_DIAL);
                //intent.setData(Uri.parse("tel:10086"));

                String data="THis is First Activity! Hello SecondActivity!";
                Intent intent=new Intent(FirstActivity.this,SecondActivity.class);
                intent.putExtra("extra_data",data);
                startActivityForResult(intent,1);

                //startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1 :
                if( resultCode == RESULT_OK){
                    String returnedData =data.getStringExtra("data_return");
                    for (int i=0;i<10;i++){
                        Log.d("SecondActivity", returnedData);
                    }
                }
                break;
            default:
        }
    }
}
