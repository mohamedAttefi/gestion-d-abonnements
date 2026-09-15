package com.abonnement;

import com.abonnement.databaseSetup.DatabaseSetup;

public class Main {

    public static void main(String[] args){
        try{
            DatabaseSetup.getConnection();
            System.out.println("connection established");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
