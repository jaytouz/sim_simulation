package dev2;

import java.util.Random;

public class Simulation {

    Random RND = new Random();

    PQ<Event> eventQ;
    Population population;

    AgeModel model = new AgeModel();

    double rRate = 2 / model.expectedParenthoodSpan(Sim.MIN_MATING_AGE_F, Sim.MAX_MATING_AGE_F);

    public void simulate(int n, double Tmax) {
        eventQ = new PQ<Event>(); // file de priorité
        population = new Population();


        for (int i = 0; i < n; i++) {
            Sim fondateur = new Sim(model.randomSex(RND)); // sexe au hasard, naissance à 0.0
            Event E = new Event(Event.TypeE.NAISSANCE, fondateur, 0.0); //nouvel événement de naissance pour fondateur à 0.0
            eventQ.insert(E); // insertion dans la file de priorité
        }

        while (!eventQ.isEmpty()) {
            Event E = (Event) eventQ.deleteMin(); // prochain événement
            if (E.time > Tmax) break; // arrêter à Tmax
            if (E.subject.getDeathTime() > E.time) {  //traiter événement E
                if (E.type == Event.TypeE.NAISSANCE) {
                    naissance(E);
                } else if (E.type == Event.TypeE.REPRODUCTION) {
                    reproduction(E);
                }
            } else {
                mort(E);
            }
        }
    }

    private void naissance(Event E) {
        double lifeLength = model.randomAge(RND);       //D
        E.subject.setDeathTime(E.time + lifeLength);   //n[1]

        if (E.subject.getSex() == Sim.Sex.F) {         //n[2]
            double A = model.randomWaitingTime(RND, rRate);
            Event newEvent = new Event(Event.TypeE.REPRODUCTION, E.subject, E.time + A);
            eventQ.insert(newEvent);
        }
        population.insert(E.subject);
        System.out.println("Naissance");
    }

    private void mort(Event E) {
        population.deleteAt(E.subject);
        System.out.println("Mort");
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
            System.out.println("Reproduction");
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

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.simulate(100, 1000);
        System.out.println(simulation.population.size);
    }
}
