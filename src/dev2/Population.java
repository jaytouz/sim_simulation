package dev2;

import java.util.Random;

public class Population<K extends Comparable<K>> extends PQ<K> { //TODO Make sure it works with the living array + dead priority q

    public Sim randomSim() {
        Random RND = new Random();
        int rnd_sim = RND.nextInt(size - 1) + 1;   //+1 pour éviter le null à la position 0, -1 pour celui à la pos size
        return (Sim) T[rnd_sim];
    }
}
