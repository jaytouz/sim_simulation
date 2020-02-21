package dev2;

import java.util.Random;

public class PQ {
    Event[] T;

    AgeModel M;
    Random RND;
    Population popHeap;


    public PQ(){
        M = new AgeModel(); //
        RND = new Random();
        popHeap = new Population();
    }


    private void insertEvent(Event E){} //TODO


    private void naissance(Sim x, float time){
        double D = M.randomAge(RND); // [n1]
        x.setDeathTime(time + D);
        if (x.getSex().equals(Sim.Sex.F)){ //[n2]
            double A = M.randomWaitingTime(RND, 2);// TODO RATE ??????
            Event R = new Event(Event.TypeE.Reproduction, x, time + A);
            insertEvent(R);
        }
        popHeap.insert(x);

    }

    private void reproduction(Sim x, float t){ //x = maman, t = temps de reproduction

    }


    public boolean isEmpty() {
        return false;
    }

    public Event deleteMin() {
        return null;
    }

    public void insert(Event e) {
    }
}
