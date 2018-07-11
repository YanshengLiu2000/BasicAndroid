package yanshengliu.testforlab2;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    private TextView txt;
    private ListView buckysListView;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt= (TextView) findViewById(R.id.textView);
        String[] datalist={"A","b","c","d","e","f","g","h","l","q","w","e","r","t","y","u","i"};
        ListAdapter buckysAdapter= new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1,datalist);
        buckysListView = (ListView) findViewById(R.id.buckysListView);
        buckysListView.setAdapter(buckysAdapter);

        buckysListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //AdapterView test=adapterView;
                myClick(adapterView,i);
            }
        });

    }
    public void myClick(AdapterView adapterView, int i){
        //String temp = String.valueOf(adapterView.getItemAtPosition(i));
        String temp1 = String.valueOf(adapterView.getItemAtPosition(i));
        txt.setText(temp1);
        PopUpLoginDialog();

    }

    public void PopUpLoginDialog() {
        //new AlertDialog.Builder(this).setTitle("Login Wifi").setView(new EditText(this)).setPositiveButton("Login",null).setNegativeButton("Cancel",null).show();
        //new AlertDialog.Builder(this).setTitle("Login Wifi").setView(new TextView()).setPositiveButton("Login",null).setNegativeButton("Cancel",null).show();
        builder=new AlertDialog.Builder(this);
        builder.setMessage("zhazha");
        builder.setNegativeButton("zhazha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("zhazha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }


}
