package com.example.watsplanning3;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class RandomPlanner implements Planner{
    private ArrayList<Dag> dagen = DagLijst.getInstance().getDagLijst();
    private Tijd beginTijd;
    private Tijd eindTijd;
    @Override
    public void chooseOptie(Scanner scanner) {
        int tijd;
        boolean check = true;
        while (check){
            System.out.println("Vanaf hoe laat wilt u beginnen met de planning?");
            tijd = scanner.nextInt();
            TijdMaker beginTijd = new TijdMaker(tijd);
            if(beginTijd.checkTijd()){
                System.out.println("Tot hoe laat wilt u uw planning maken?");
                tijd = scanner.nextInt();
                TijdMaker eindTijd = new TijdMaker(tijd);
                if(eindTijd.checkTijd()){
                    check = generateDag(beginTijd.getTijd(),eindTijd.getTijd());
                }
            }
        }
        createDag();
    }
    @Override
    public void createDag() {
        int i=0;
        LocalDate time = LocalDate.now().plusDays(1);
        Date date = new Date(2023,time.getMonthValue(),time.getDayOfMonth());
        while(DagLijst.getInstance().checkDag(date)){
            time = time.plusDays(1);
            date = new Date(2023,time.getMonthValue(),time.getDayOfMonth());
        }
        Dag dag = new Dag();
        dag.setDatum(date);
        Tijd tijd = new Tijd(beginTijd.getTijd());
        ActiviteitenLijst.getInstance().shuffle();
        while(tijd.getTijd() < eindTijd.getTijd()){
            if(i>=ActiviteitenLijst.getInstance().getRandomizeList().size()){
                break;
            }
            Tijd tijd2 = new Tijd(tijd.getTijd());
            tijd = dag.uitvoering(tijd2,ActiviteitenLijst.getInstance().getRandomizeList().get(i));
            i++;
        }
        for (Activiteit activiteit : ActiviteitenLijst.getInstance().getActiviteitenLijst()){
            if (activiteit.getRoutine()){
                dag.uitvoering(activiteit.getVasteTijd(), activiteit);
            }
        }
        DagLijst.getInstance().getDagLijst().add(dag);
    }

    public boolean generateDag(Tijd beginTijd, Tijd eindTijd){
        if(checkTijd(beginTijd,eindTijd)==3 && ActiviteitenLijst.getInstance().getActiviteitenLijst().size()!=0){
            this.beginTijd = beginTijd;
            this.eindTijd = eindTijd;
            return false;
        }
        else if(checkTijd(beginTijd,eindTijd)==1){
            System.out.println("Begin tijd moet later zijn dan de eind tijd");
        }
        else if (checkTijd(beginTijd,eindTijd)==2){
            System.out.println("Planning moet minstens 1 uur duren");
        }
        return true;
    }

    public int checkTijd(Tijd beginTijd, Tijd eindTijd){
        if(beginTijd.getTijd()>=eindTijd.getTijd()){
            return 1;
        }
        // minuut naar uur
        int uur = eindTijd.getUur() - beginTijd.getUur();
        int minuut = eindTijd.getMinuut() - beginTijd.getMinuut();
        if(minuut<0){
            uur=uur-1;
            minuut = minuut+60;
        }
        int x = uur * 100 + minuut;
        Tijd verschil = new Tijd(x);

        if(verschil.getUur()<1){
            return 2;
        }
        return 3;
    }
}
