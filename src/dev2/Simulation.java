package dev2;

import java.util.Random;

public class Simulation {

    PQ eventQ;

    public void simulate(int n, double Tmax) {
        eventQ = new PQ(); // file de priorité

        for (int i = 0; i < n; i++) {
            Sim fondateur = new Sim(randomSex()); // sexe au hasard, naissance à 0.0
            Event E = new Event(Event.TypeE.Naissance, fondateur, 0.0); //nouvel événement de naissance pour fondateur à 0.0
            eventQ.insert(E); // insertion dans la file de priorité
        }

        while (!eventQ.isEmpty()) {
            Event E = eventQ.deleteMin(); // prochain événement
            if (E.time > Tmax) break; // arrêter à Tmax
            if (E.subject.getDeathTime() > E.time) {
                //traiter événement E
                if (E.type == Event.TypeE.Naissance){
                    System.out.println("TRAITER NAISSANCE");
                }else if (E.type == Event.TypeE.Reproduction){
                    System.out.println("FAIRE DES BEUBÉS");
                }
            }else{
                // else rien à faire avec E car son sujet est mort
                System.out.println("EST-CE QU'ON DOIT LE SUPPRIMER D'UNE DES STRUCTURES");
            }
        }
    }
    /**created par JT
     * generate random int btw 0 and 1 corresponding to Sex enum idx to return.
     * @return
     */private Sim.Sex randomSex(){
        Random RND = new Random();
        int rnd_sexe_id = RND.nextInt(2);
        return Sim.Sex.values()[rnd_sexe_id];
    }
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
    }
}
