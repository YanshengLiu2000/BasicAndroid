package comp9336lab1.clickcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import	android.app.Activity;
import	android.view.Menu;
import	android.view.View;
import	android.widget.Button;
import	android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clickMeBtn =(Button) findViewById(R.id.button1);
        clickMeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                myClick(v);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    int i=0;

    public void myClick(View v){
        //zhazha
        TextView txCounter=(TextView) findViewById(R.id.textView1);
        i++;
        txCounter.setText("count: "+i);
    }
}




