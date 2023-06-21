package com.example.watsplanning3;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Scanner;

public class ActiviteitManager implements Optie{

    @Override
    public void chooseOptie(Scanner scanner){
        int optionA = 1;
        while(optionA!=0) {
            System.out.println("Kies een optie:");
            System.out.println("1: Bekijk alle activiteiten");
            System.out.println("2: Creëer activiteit");
            System.out.println("3: Verwijder activiteit");
            System.out.println("0: Vorige scherm");
            optionA = scanner.nextInt();
            switch (optionA){
                case 0:
                    break;
                case 1:
                    ActiviteitenLijst.getInstance().printActiviteiten();
                    break;
                case 2:
                    createActiviteit(scanner);
                    break;
                case 3:
                    System.out.println("Selecteer een activeit om te verwijderen");
                    ActiviteitenLijst.getInstance().printActiviteiten();
                    int activiteit = scanner.nextInt();
                    deleteActiviteit(activiteit);
                    break;
                default:
                    System.out.println("Probeer het opnieuw");
                    break;
            }
        }
    }
    public void createActiviteit(Scanner scanner){
        System.out.println("Naam van activiteit");
        scanner.nextLine();
        String naam = scanner.nextLine();
        System.out.println("Wat voor soort activiteit is het?");
        int i = 1;
        for (Activiteit activiteit : ActiviteitenLijst.getInstance().getCategorien()){
            System.out.println(i+ ": " + activiteit.getCategorie());
            i++;
        }
        i=-1;
        int categorie = 0;
        while(i==-1){
            categorie = scanner.nextInt();
            switch (categorie){
                case 0:
                    i = 0;
                    break;
                case 1, 2:
                    i=1;
                    break;
                default:
                    System.out.println("Ongeldig soort activiteit");
                    break;
            }
        }
        if(i==0){}
        else{
            System.out.println("Duratie van activiteit in minuten");
            int duratie = scanner.nextInt();
            System.out.println("Wil je het een routine maken? \nZo ja, hoe laat wil je dat het begint?\nZo niet, toets 0");
            while(true){
                Tijd vasteTijd = new Tijd(scanner.nextInt());
                if(vasteTijd.getMinuut() < 59 && vasteTijd.getUur() < 23){
                    createFactory(categorie, naam, duratie, null,vasteTijd);
                    break;
                }
                System.out.println("Ongeldig tijdstip");
            }
        }
    }

    public void createFactory(int categorie, String naam, int duratie, Image afbeelding, Tijd vasteTijd){
        if(categorie==1){
            ActiviteitFactory factory = new NormaalActiviteitFactory();
            ActiviteitenLijst.getInstance().getActiviteitenLijst().add(factory.maakActiviteit(naam,duratie,afbeelding,vasteTijd));
        }
        else if(categorie==2){
            ActiviteitFactory factory = new SportActiviteitFactory();
            ActiviteitenLijst.getInstance().getActiviteitenLijst().add(factory.maakActiviteit(naam,duratie,afbeelding,vasteTijd));
        }
    }

    public void deleteActiviteit(int activiteit){
        if (activiteit <= ActiviteitenLijst.getInstance().getActiviteitenLijst().size()){
            System.out.println(ActiviteitenLijst.getInstance().getActiviteitenLijst().get(activiteit-1).getNaam() + " is verwijderd");
            ActiviteitenLijst.getInstance().getActiviteitenLijst().remove(activiteit-1);
        }
        else{
            System.out.println("Ongeldige invoer");
        }
    }
}
