package dev2;

import com.sun.source.tree.Tree;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Simulation {

    Random RND = new Random();

    PQ<Event> eventQ;
    Population population;

    AgeModel model = new AgeModel();

    TreeMap<Double, Integer> popSizeTotal;
    TreeMap<Double, Integer> aieux;
    TreeMap<Double, Integer> aieules;


    double rRate = 2 / model.expectedParenthoodSpan(Sim.MIN_MATING_AGE_F, Sim.MAX_MATING_AGE_F);

    public void simulate(int n, double Tmax) {
        eventQ = new PQ<Event>(); // file de priorité
        population = new Population();
        popSizeTotal = new TreeMap<Double, Integer>();


        for (int i = 0; i < n; i++) {
            Sim fondateur = new Sim(model.randomSex(RND)); // sexe au hasard, naissance à 0.0
            Event E = new Event(Event.TypeE.NAISSANCE, fondateur, 0.0); //nouvel événement de naissance pour fondateur à 0.0
            eventQ.insert(E); // insertion dans la file de priorité
        }

        while (!eventQ.isEmpty()) {
            Event E = (Event) eventQ.deleteMin(); // prochain événement
            popSizeTotal.put(E.time, population.size);
            if (E.time > Tmax) break; // arrêter à Tmax
            if (E.type == Event.TypeE.NAISSANCE) {
                naissance(E);
            } else if (E.type == Event.TypeE.REPRODUCTION) {
                reproduction(E);
            } else if (E.type == Event.TypeE.MORT) {
                mort(E);
            }

            System.out.println("YEAR : + " + E.time + "TYPE " + E.type.toString());

        }
    }

    private void naissance(Event E) {
        double lifeLength = model.randomAge(RND);       //D
        E.subject.setDeathTime(E.time + lifeLength);   //n[1]
        eventQ.insert(new Event(Event.TypeE.MORT, E.subject, E.time + lifeLength));

        if (E.subject.getSex() == Sim.Sex.F) {         //n[2]
            double A = model.randomWaitingTime(RND, rRate);
            Event newEvent = new Event(Event.TypeE.REPRODUCTION, E.subject, E.time + A);
            eventQ.insert(newEvent);
        }
        population.insert(E.subject);
//        System.out.println("Naissance");
    }

    private void mort(Event E) {
        System.out.println(population.size);
        population.deleteMin();
//        System.out.println("Mort");
    }

    private void reproduction(Event E) {
        Sim maman = E.subject;
        double time = E.time;
        if (maman.isMatingAge(time)) {
            Sim papa = choixPere(time, maman);
            Sim baby = new Sim(maman, papa, time, model.randomSex(RND));
            Event naissance = new Event(Event.TypeE.NAISSANCE, baby, time);
            eventQ.insert(naissance);
            maman.setMate(papa);
            papa.setMate(maman);
//            System.out.println("Reproduction");
        }
        double A = model.randomWaitingTime(RND, rRate);
        Event repro = new Event(Event.TypeE.REPRODUCTION, maman, time + A);
        eventQ.insert(repro);
    }

    private Sim choixPere(double time, Sim maman) {
        Sim partenaire = null;
        double fidelite = model.getFIDELITE();
        if (!maman.isInARelationship(time) || RND.nextDouble() > fidelite) { // partenaire au hasard
            do {
                Sim z = population.randomSim();
                if (z.getSex() != maman.getSex() && z.isMatingAge(time)) // isMatingAge() vérifie si z est de l'age adéquat
                {
                    if (maman.isInARelationship(time) || !z.isInARelationship(time) || RND.nextDouble() > fidelite) {
                        partenaire = z;
                    }
                }
            } while (partenaire == null);
        } else {
            partenaire = maman.getMate(); //P1.1 et P1.2
        }
        return partenaire;
    }
    public void extractAieux(Population maleSurvivors){
        aieux = new TreeMap<Double, Integer>();
        Population hommePA = maleSurvivors;
        while(!hommePA.isEmpty() || !hommePA.onlyFondateur()){
            Sim sim =(Sim) hommePA.deleteMin();
            Sim father = sim.getFather();
            if (father != null && !hommePA.contains(father)){
                hommePA.insert(father);
            }
            aieux.put(sim.getBirthTime(), hommePA.size);
        }
    }

    public void extractAieule(Population femaleSurvivors){
        aieules = new TreeMap<Double, Integer>();
        Population femmePA = femaleSurvivors;
        while(!femmePA.isEmpty() || !femmePA.onlyFondateur()){
            Sim sim =(Sim) femmePA.deleteMin();
            Sim mother = sim.getMother();
            if (mother != null && !femmePA.contains(mother)){
                femmePA.insert(mother);
            }
            aieules.put(sim.getBirthTime(), femmePA.size);
        }
    }
    public void setCoalescence(){
        Population[] male_female = Population.splitMaleFemaleSurvivors(population);
        extractAieux(male_female[0]);
        extractAieule(male_female[1]);
    }

    public void saveAieuxAileulesToFile(int initialPopSize, int tMax) throws IOException {
        Date date = new Date();
        String basePath = new File("").getAbsolutePath();
        basePath += "/out/production/dev2_sim/data/";
        String fileName = String.format("simulation_%d_%d_%d.txt", initialPopSize, tMax, date.getTime());
        File file = new File(basePath + fileName);

        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        try {
            out.write("time,size,sex\n" );
            for (Double time : aieux.keySet()) {
                out.write(time + "," + aieux.get(time) + "," + "M" + "," + popSizeTotal.get(time) +"\n");
            }
            for (Double time : aieules.keySet()) {
                out.write(time + "," + aieules.get(time) + "," + "F" + "," + popSizeTotal.get(time) + "\n");
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }finally {
            out.close();
        }
    }

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.simulate(1000, 2000);
        System.out.println(simulation.population.size + " Sims encore en vie");
        simulation.setCoalescence();
        try{
            simulation.saveAieuxAileulesToFile(1000, 2000);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
