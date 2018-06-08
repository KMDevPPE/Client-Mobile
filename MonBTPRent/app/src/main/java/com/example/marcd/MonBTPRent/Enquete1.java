package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.marcd.MonBTPRent.R;

public class Enquete1 extends AppCompatActivity implements View.OnClickListener{

    private Button btSuivant;
    private RatingBar rbEtat, rbLivraison;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquete1);

        this.btSuivant = findViewById(R.id.idSuivant1);
        this.rbEtat = findViewById(R.id.idRbEtat);
        this.rbLivraison = findViewById(R.id.idRbLiv);

        this.email = this.getIntent().getStringExtra("email").toString();

        this.btSuivant.setOnClickListener(this);
        this.rbEtat.setOnClickListener(this);
        this.rbLivraison.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        String e = "Etat";
        String l = "Livraison";
        float scoreE = this.rbEtat.getRating();
        float scoreL = this.rbLivraison.getRating();

        Enquete.ajouterUneReponse(this.email,e,scoreE);
        Enquete.ajouterUneReponse(this.email,l,scoreL);

        Intent unIntent = new Intent(this, Enquete2.class);
        unIntent.putExtra("email",this.email);
        this.startActivity(unIntent);
    }
}
