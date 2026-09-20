package com.abonnement.entity;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Abonnement {
    protected String id;
    protected String nomService;
    protected double montantMensuel;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected StatutAbonnement statut;


    public Abonnement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, StatutAbonnement statut){
        this.id = UUID.randomUUID().toString();
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    public String getNomService() {
        return nomService;
    }

    public String getId() {
        return id;
    }

    public double getMontantMensuel() {
        return montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public StatutAbonnement getStatut() {
        return statut;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public void setMontantMensuel(double montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public void setStatut(StatutAbonnement statut) {
        this.statut = statut;
    }
}
