package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.marcd.MonBTPRent.R;

public class Enquete2 extends AppCompatActivity implements View.OnClickListener{

    private Button btSuivant;
    private RatingBar rbAccess, rbInterloc;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquete2);

        this.btSuivant = findViewById(R.id.idSuivant2);
        this.rbAccess = findViewById(R.id.idRbAccess);
        this.rbInterloc = findViewById(R.id.idRbInterloc);

        this.email = this.getIntent().getStringExtra("email").toString();

        this.btSuivant.setOnClickListener(this);
        this.rbAccess.setOnClickListener(this);
        this.rbInterloc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String i = "interloc";
        String a = "access";
        float scoreI = this.rbInterloc.getRating();
        float scoreA = this.rbAccess.getRating();

        Enquete.ajouterUneReponse(this.email,i,scoreI);
        Enquete.ajouterUneReponse(this.email,a,scoreA);

        Intent unIntent = new Intent(this, Enquete3.class);
        unIntent.putExtra("email",this.email);
        this.startActivity(unIntent);
    }
}
