package dev2;

import java.util.Random;

public class  Population<K extends Comparable<K>> extends PQ<K> { //TODO Make sure it works with the living array + dead priority q

    public Sim randomSim() {
        Random RND = new Random();
        int rnd_sim = RND.nextInt(size - 1) + 1;   //+1 pour éviter le null à la position 0, -1 pour celui à la pos size
        return (Sim) T[rnd_sim];
    }

    public boolean contains(Sim sim) {
        boolean hasSim = false;
        for (int i = 0; i < super.T.length; i++) {
            Sim i_sim = (Sim) super.T[i];
            if(i_sim == null) break;
            if (i_sim.getSim_ident() == sim.getSim_ident()) hasSim = true;
        }
        return hasSim;
    }

    public static Population[] splitMaleFemaleSurvivors(Population survivors) {
        Population[] male_female_pop= new Population[2];
        Population male = new Population();
        Population female = new Population();
        while(!survivors.isEmpty()){
            Sim sim = (Sim) survivors.deleteMin();
            if (sim.getSex() == Sim.Sex.F){
                female.insert(sim);
            }else{
                male.insert(sim);
            }
        }
        male_female_pop[0] = male;
        male_female_pop[1] = female;

        return male_female_pop;
    }

    public boolean onlyFondateur() {
        boolean onlyFond = true;
        for (int i = 1; i < super.T.length; i++) {
            Sim i_sim = (Sim) super.T[i];
            if (i_sim == null) break;
            if (!i_sim.isFounder()) {
                onlyFond = false;
                break;
            }
        }
        return onlyFond;
    }
}
