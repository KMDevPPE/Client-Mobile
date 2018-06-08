package com.example.marcd.MonBTPRent;

/**
 * Created by marcd on 16/04/2018.
 */

public class Reservation {

    private String nom, prenom, email, adresse, dateResaDebut, dateResaFin, confirmation;
    private int idAppart, idResa;
    private float superficie, montant;

    public Reservation(String nom, String prenom, String email, String adresse, String dateResaDebut, String dateResaFin, String confirmation, int idAppart, int idResa, float superficie, float montant) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.dateResaDebut = dateResaDebut;
        this.dateResaFin = dateResaFin;
        this.confirmation = confirmation;
        this.idAppart = idAppart;
        this.idResa = idResa;
        this.superficie = superficie;
        this.montant = montant;
    }

    public String toString()
    {
        return adresse + " " + dateResaDebut + " " + dateResaFin + " " + confirmation;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateResaDebut() {
        return dateResaDebut;
    }

    public void setDateResaDebut(String dateResaDebut) {
        this.dateResaDebut = dateResaDebut;
    }

    public String getDateResaFin() {
        return dateResaFin;
    }

    public void setDateResaFin(String dateResaFin) {
        this.dateResaFin = dateResaFin;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public int getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(int idAppart) {
        this.idAppart = idAppart;
    }

    public int getIdResa() {
        return idResa;
    }

    public void setIdResa(int idResa) {
        this.idResa = idResa;
    }

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

}
