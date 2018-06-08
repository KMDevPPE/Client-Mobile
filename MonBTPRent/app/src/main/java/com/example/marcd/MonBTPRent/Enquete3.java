package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.marcd.MonBTPRent.R;

public class Enquete3 extends AppCompatActivity implements View.OnClickListener{

    private Button btOui,btNon;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquete3);

        this.btOui = findViewById(R.id.idOui);
        this.btNon = findViewById(R.id.idNon);

        this.email = getIntent().getStringExtra("email").toString();

        this.btOui.setOnClickListener(this);
        this.btNon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent unIntent = new Intent(this, Enquete4.class);
        unIntent.putExtra("email",this.email);
        this.startActivity(unIntent);
    }
}
