package com.example.marcd.MonBTPRent;

import java.util.HashMap;

/**
 * Created by marcd on 05/03/2018.
 */

public class Client {
    private String nom, prenom,email , mdp;

    private HashMap<String, Float> lesReponses;

    public Client (String email, String mdp, String nom, String prenom)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;

        this.lesReponses = new HashMap<String, Float>();
    }


    public void ajouterReponse (String question, float score)
    {
        this.lesReponses.put(question, score);
    }
    public float getMoyenne ()
    {
        float moyenne = 0;
        for (Float score : this.lesReponses.values())
        {
            moyenne += score.floatValue() ;
        }
        moyenne /= this.lesReponses.size();
        return moyenne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public HashMap<String, Float> getLesReponses() {
        return lesReponses;
    }

    public void setLesReponses(HashMap<String, Float> lesReponses) {
        this.lesReponses = lesReponses;
    }
}
