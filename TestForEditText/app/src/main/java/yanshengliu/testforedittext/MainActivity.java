package yanshengliu.testforedittext;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText usernamepop;
    private EditText passwordpop;
    private TextView infor;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button =(Button)findViewById(R.id.button);
        infor=(TextView) findViewById(R.id.infor);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        view = inflater.inflate(R.layout.login_layout, null, true);

        usernamepop=(EditText) view.findViewById(R.id.usernametest);
        passwordpop=(EditText) view.findViewById(R.id.passwordtest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int j=0;j<10;j++){
                    System.out.println("Enter Onclick ");
                }
                loginWifi();
            }
        });
    }

    private void loginWifi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


        AlertDialog dialog = builder.setView(view).setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int k = 0; k < 10; k++) {
                    System.out.println("Enter Succeed!!!!!");
                }
                for (int k = 0; k < 10; k++) {
                    System.out.println(usernamepop.getText().toString()+passwordpop.getText().toString());
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Canceled");
            }
        }).create();
        dialog.show();
    }
//        AlertDialog dialog = builder.setView(inflater.inflate(R.layout.login_layout,null)).setPositiveButton("Login", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //onOk.hereisYouText(usernamepop.getText().toString());
//                for(int j=0;j<10;j++){
//                    System.out.println("Here is uu : "+usernamepop.getText().toString());
//                }
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        }).create();
//        dialog.show();
//    }
}
