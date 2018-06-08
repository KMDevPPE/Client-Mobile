package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class Enquete4 extends AppCompatActivity implements View.OnClickListener {

    private Button btSuivant;
    private RatingBar rbInterv, rbTech;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquete4);

        this.btSuivant = findViewById(R.id.idSuivant4);
        this.rbTech = findViewById(R.id.idRbTech);
        this.rbInterv = findViewById(R.id.idRbIntervention);

        this.email = this.getIntent().getStringExtra("email").toString();

        this.btSuivant.setOnClickListener(this);
        this.rbTech.setOnClickListener(this);
        this.rbInterv.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        String it = "intervention";
        String t = "Technicien";
        float scoreIt = this.rbInterv.getRating();
        float scoreT = this.rbTech.getRating();

        Enquete.ajouterUneReponse(this.email,t,scoreT);
        Enquete.ajouterUneReponse(this.email,it,scoreIt);

        Intent unIntent = new Intent(this, Resultat.class);
        unIntent.putExtra("email",this.email);
        this.startActivity(unIntent);
    }
}
