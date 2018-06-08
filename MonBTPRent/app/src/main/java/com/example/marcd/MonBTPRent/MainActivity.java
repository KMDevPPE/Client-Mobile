package com.example.marcd.MonBTPRent;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btAnnuler,btSeConnecter ;
    private EditText txtMail,txtMdp;

    private static Client clientConnecte = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btAnnuler = (Button) findViewById(R.id.idAnnuler);
        this.btSeConnecter = (Button) findViewById(R.id.idSeCo);
        this.txtMail = (EditText) findViewById(R.id.idEmail);
        this.txtMdp = (EditText) findViewById(R.id.idMdp);

        this.btSeConnecter.setOnClickListener(this);
        this.btAnnuler.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.idAnnuler :
                this.txtMdp.setText("");
                this.txtMail.setText("");
                break;
            case R.id.idSeCo :
            {
                String mail = this.txtMail.getText().toString();
                String mdp =  this.txtMdp.getText().toString();

                final Client unClient = new Client(mail,mdp,"", ""); // final = constant
                // verification de la connexion du client dans la base

                final MainActivity ma = this;

                //creation d'un processus fils thread
                Thread unT = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // instanciation de la class verifConnexion
                        VerifConnexion uneConnexion = new VerifConnexion();
                        uneConnexion.execute(unClient);
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

                                if(clientConnecte != null)
                                {
                                   Intent unIntent = new Intent(ma, Enquete1.class);
                                    // on prend que l'mail car on se refere a l'mail pour la suite
                                    unIntent.putExtra("email", clientConnecte.getEmail());
                                    if (Enquete.existe(clientConnecte))
                                    {
                                        Toast.makeText(ma, clientConnecte.getNom()+ " Vous avez déjà participé à cette Enquete !", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(ma, "bienvenue "+clientConnecte.getNom() +"  " +clientConnecte.getPrenom(), Toast.LENGTH_SHORT).show();

                                        Enquete.ajouterUser(clientConnecte);
                                        startActivity(unIntent);
                                    }
                                }else{
                                    Toast.makeText(ma, "Veuillez verifier vos identifiants", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
                // lancement du processus
                unT.start();

            }
            break;

        }

    }

    public static Client getClientConnecte() {
        return clientConnecte;
    }

    public static void setClientConnecte(Client clientConnecte) {
        MainActivity.clientConnecte = clientConnecte;
    }
}

/*********************** Classe tache asynchrone ***************/

//                                    entre , progression , sortie
class VerifConnexion extends AsyncTask<Client, Void, Client>
{

    @Override
    protected Client doInBackground(Client... clients) {
        String url = "http://192.168.8.200/android/verifConnexion.php";
        String resultat = null ;
        Client unClient = clients[0];
        Client leClient = null;

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
            String parametres = "mail="+unClient.getEmail()+"&mdp="+unClient.getMdp();
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

            }
            bufferIn.close();
            fichierIn.close();
            resultat = sb.toString();
            Log.e("resultat : ",resultat);

        }catch (IOException exp)
        {
            Log.e("Erreur : ", "Connexion impossible a : "+url);
        }

        if (resultat != null)
        {
            try {
                JSONArray tabJson = new JSONArray(resultat);
                JSONObject unObject  = tabJson.getJSONObject(0);
                int nb = unObject.getInt("nb");
                if (nb>=1)
                {
                    leClient = new Client(unClient.getEmail(),unClient.getMdp(), unObject.getString("NOM_C"), unObject.getString("PRENOM_C"));
                }

            }catch (JSONException exp)
            {
                Log.e("Erreur : ","Impossible de parser le resultat");
            }
        }

        return leClient;
    }

    @Override
    protected void onPostExecute(Client client) {
        MainActivity.setClientConnecte(client);
    }
}

