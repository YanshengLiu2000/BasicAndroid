package yanshengliu.selfprac_onestep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Button clickA=(Button) findViewById(R.id.button1);
        clickA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick();
            }
        });
    }
    int count=0;
    public void myClick(){
        TextView txtA=(TextView) findViewById(R.id.textView1);
        count++;
        txtA.setText("Count times: "+count);
        startActivities(new Intent[]{new Intent(AActivity.this, BActivity.class)});


    }
}
