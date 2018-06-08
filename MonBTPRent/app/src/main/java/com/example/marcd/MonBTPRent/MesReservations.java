package com.example.marcd.MonBTPRent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcd.MonBTPRent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MesReservations extends AppCompatActivity {

    private ListView lesResa ;
    private static ArrayList<Reservation> mesReservations = null ;

    private String email ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_reservations);

        this.lesResa = (ListView) findViewById(R.id.idList);

        // recuperation de l'email de la page mainActivity
        this.email = this.getIntent().getStringExtra("email");

        final MesReservations mr = this;

        // affichage des reservations dans la liste view
        Thread unT = new Thread(new Runnable() {
            @Override
            public void run() {
                GetReservations uneAction = new GetReservations();
                uneAction.execute(email);
                try{
                    Thread.sleep(10000);
                }catch(InterruptedException e){
                    Log.e("erreur : ","sleep ");
                }

                //affichage des données
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (mesReservations != null)
                        {
                            // on defini une arraylist en string
                            ArrayList<String> contenu = new ArrayList<String>();
                            for (Reservation uneResa : mesReservations)
                            {
                                contenu.add(uneResa.toString());
                            }
                            ArrayAdapter unAdapter = new ArrayAdapter(mr,android.R.layout.simple_list_item_1,contenu);
                            lesResa.setAdapter(unAdapter);
                        }
                    }
                });
            }
        });
        unT.start();

    }

    public static ArrayList<Reservation> getMesReservations() {
        return mesReservations;
    }

    public static void setMesReservations(ArrayList<Reservation> mesReservations) {
        MesReservations.mesReservations = mesReservations;
    }
}

/*********************** Classe tache asynchrone ***************/

//                                    entre , progression , sortie
class GetReservations extends AsyncTask<String, Void, ArrayList<Reservation>>
{

    @Override
    protected ArrayList<Reservation> doInBackground(String... emails) {
        String url = "http://192.168.1.248/android/mesReservations.php";
        String resultat = null ;
        String email = emails[0];
        ArrayList<Reservation> lesReservations = new ArrayList<Reservation>();

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


            // envoyer les données email et mdp via un fichier de sortie
            OutputStream fichierOut = uneUrlConnexion.getOutputStream();
            BufferedWriter bufferOut = new BufferedWriter(new OutputStreamWriter(fichierOut,"UTF-8"));
            String parametres = "email="+email;
            bufferOut.write(parametres);
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
                bufferIn.close();
                fichierIn.close();
                resultat = sb.toString();
                Log.e("resultat : ",resultat);
            }

        }catch (IOException exp)
        {
            Log.e("Erreur : ", "Connexion impossible a : "+url);
        }

        if (resultat != null)
        {
            try {
                JSONArray tabJson = new JSONArray(resultat);
                for (int i = 0 ; i<tabJson.length();i++) {
                    JSONObject unObject = tabJson.getJSONObject(i);
                    Reservation uneResa = new Reservation(
                            unObject.getString("nom"),
                            unObject.getString("prenom"),
                            unObject.getString("email"),
                            unObject.getString("adresse"),
                            unObject.getString("dateResaDebut"),
                            unObject.getString("dateResaFin"),
                            unObject.getString("confirmation"),
                            unObject.getInt("idAppart"),
                            unObject.getInt("idResa"),
                            (float)unObject.getDouble("superficie"),
                            (float)unObject.getDouble("montant")
                    );
                    lesReservations.add(uneResa);
                }

            }catch (JSONException exp)
            {
                Log.e("Erreur : ","Impossible de parser le resultat");
            }
        }

        return lesReservations;
    }

    @Override
    protected void onPostExecute(ArrayList<Reservation> reservations) {
        MesReservations.setMesReservations(reservations);
    }
}

