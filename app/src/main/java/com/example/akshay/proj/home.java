package com.example.akshay.proj;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class home extends Activity {
    Button b,h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button_list();
    }
    public void button_list(){
        b=(Button)findViewById(R.id.b1);
        b.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i = new Intent("com.example.akshay.proj.Page2");
                        startActivity(i);
                    }
                }
        );
        h=(Button)findViewById(R.id.help);
        h.getLayoutParams().width= 80;
        h.getLayoutParams().height=80;
        h.setBackgroundResource(R.drawable.help_img);
        h.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent i=new Intent("com.example.akshay.proj.help");
                        startActivity(i);
                    }
                }
        );
    }
}
