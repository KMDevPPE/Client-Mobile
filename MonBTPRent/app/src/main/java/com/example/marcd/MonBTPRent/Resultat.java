package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marcd.MonBTPRent.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Resultat extends AppCompatActivity implements View.OnClickListener{

    private ListView lvListe;
    private String email;
    private Button btRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        this.email = this.getIntent().getStringExtra("email").toString();
        this.lvListe = findViewById(R.id.idListe);
        this.btRetour = (Button)findViewById(R.id.idRetour) ;

        ArrayList<String> lesResultats = Enquete.getLesResultats(); // table des participant
        String tabP[] = new String [lesResultats.size()];
        int i=0;
        for (String res : lesResultats)
        {
            tabP[i] = res;
            i++;
        }
        ArrayList<Integer> lesSmiley = Enquete.getLesSmileys(); // table des smiley
        int tabI[] = new int [lesResultats.size()];
        i=0;
        for (Integer smiley : lesSmiley)
        {
            tabI[i] = smiley;
            i++;
        }
        AdapterList unAdapter = new AdapterList(this,tabP,tabI);
        this.lvListe.setAdapter(unAdapter);

        // on recupere email, moyenne, date reponse (now();
        float moyenne = Enquete.getLaMoyenne(this.email);

        final String envoi = "email="+this.email+"&moyenne="+moyenne;

        final Resultat re = this;

        //creation d'un processus fils thread
        Thread unT = new Thread(new Runnable() {
            @Override
            public void run() {
                // instanciation de la class verifConnexion
                Insertion uneInsertion = new Insertion();
                uneInsertion.execute(envoi);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    Log.e("erreur : ","sleep ");
                }

                // pour modifier/afficher le toast avec le processus fils, il faut une synchronisation
                // pour executer le toast(fils) en même temps que le processus père
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                                Toast.makeText(re, " Insertion reussie en BDD de votre réponse !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // lancement du processus
        unT.start();

        this.btRetour.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent unIntent = new Intent(this,MainActivity.class);
        this.startActivity(unIntent);
    }


}
/*********************** Classe tache asynchrone ***************/

//                                    entre , progression , sortie
class Insertion extends AsyncTask<String, Void, Void>
{

    @Override
    protected Void doInBackground(String... strings) {
        String url = "http://192.168.1.248/android/insertReponse.php";
        String resultat = null ;
        String chaine = strings[0];

        try {
            URL uneUrl = new URL(url);
            // instanciation de la connexion
            HttpURLConnection uneUrlConnexion = (HttpURLConnection) uneUrl.openConnection();

            // specification de la methode
            uneUrlConnexion.setRequestMethod("GET");

            // ouverture de l'envoi et la reception des données
            uneUrlConnexion.setDoOutput(true);
            uneUrlConnexion.setDoInput(true);

            // on fixe le temps de connexion et d'attente
            uneUrlConnexion.setReadTimeout(10000);
            uneUrlConnexion.setConnectTimeout(10000);

            // on se connect
            uneUrlConnexion.connect();


            // envoyer les données mail et mdp via un fichier de sortie
            OutputStream fichierOut = uneUrlConnexion.getOutputStream();
            BufferedWriter bufferOut = new BufferedWriter(new OutputStreamWriter(fichierOut,"UTF-8"));
            Log.e("envoi : ", chaine);
            bufferOut.write(chaine);
            bufferOut.flush();
            bufferOut.close();
            fichierOut.close();

            // lecture du resultat dans une chaine de caractere
            InputStream fichierIn = uneUrlConnexion.getInputStream();
            BufferedReader bufferIn = new BufferedReader(new InputStreamReader(fichierIn,"UTF-8"));
            // lecture des chaines contenue dans la page php
            StringBuilder sb = new StringBuilder(); // chaine dynamique a longueur variable
            String ligne = null;
            while ((ligne = bufferIn.readLine()) != null)
            {
                sb.append(ligne);

            }
            bufferIn.close();
            fichierIn.close();
            resultat = sb.toString();
            Log.e("resultat : ",resultat);

        }catch (IOException exp)
        {
            Log.e("Erreur : ", "Connexion impossible a : "+url);
        }
        return null;
    }


}