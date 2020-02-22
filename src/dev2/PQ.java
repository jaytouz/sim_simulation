package dev2;

import java.util.Arrays;

public class PQ<K extends Comparable<K>> {
    K[] T;
    int size;


    public PQ(){
        T = (K[]) new Comparable[4];
        size = 1;
    }

    private void insert(K item){
        checkSize(1);
        //swim qu'il faudrait faire.
//        T[size] = item;
        swim(item, size);
        size++;
    }

    private void swim(K item, int idx){
        int p = parent(idx);
        while (p != 0 && T[p].compareTo(item)>0){// //tant que parent plus grand a cet indice, aller voir parent de parent
            T[idx] = T[p];
            idx = p;
            p = parent(idx);
        }
        T[idx] = item;
    }
    private void sink(K item, int idx){
        int c = minChild(idx);
        while(c!=0 && T[c].compareTo(item) < 0){
            T[idx] = T[c];
            idx = c;
            c = minChild(c); // si pas de minChild retourne 0 et arret la boucle
        }
        T[idx] = item;
    }

    private K deleteMin(){
        K r = T[1];
        K v = T[size-1];
        T[size-1] = null;
        size--;
        if (size>1) sink(v, 1);
        return r;

    }

    private int parent(int idx){
        return Math.floorDiv(idx, 2);
    }

    /**
     * retourne l'indice de l'enfant le plus petit ou 0 s'il n'y a pas d'enfant.
     * @param idx
     * @return
     */
    private int minChild(int idx){
        int j = 0; //si aucun enfant retourner 0
        int c1 = (idx*2); int c2 = (idx*2)+1; //calculer la position des enfants de idx
        if (c1<=size-1){ //si enfant c1 existe
            j = c1;
            if (c2 <= size-1 && T[c2].compareTo(T[c1])<0){ //si enfant 2 existe et est plus petit que enfant 1
                j=c2;
            }
        }
        return j;
    }

    /**
     * double array size and copy item
     */
    private void expand(){
        if (size == 0){
            T = (K[]) new Comparable[1];

        }else{
            K[] copy = (K[]) new Comparable[T.length*2];
            for(int i = 0; i<size; i++){
                copy[i] = T[i];
            }
            T = copy;

        }
    }

    /**
     * reduce array size by half and copy item
     */
    private void shrink(){
        K[] copy = (K[]) new Comparable[T.length/2];
        for (int i = 0; i<size; i++){
            copy[i] = T[i];
        }
        T = copy;
    }

    /**
     * use delta == -1 if deleteItem
     * use delta == +1 if addItem
     * @param delta
     */
    private void checkSize(int delta){
        int newSize = size + delta;

        if (newSize >= T.length){
            expand();
        }else if(newSize <= T.length/4){
            shrink();
        }
    }

    @Override
    public String toString() {
        return "PQCustom{" +
                "T=" + Arrays.toString(T) +
                ", size=" + size +
                '}';
    }

    public static void main(String[] args) {
        Event E1 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 0.0);
        Event E2 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 1.0);
        Event E3 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 2.0);
        Event E4 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 3.0);
        Event E5 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 4.0);
        Event E6 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 5.0);
        Event E7 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 6.0);
        Event E8 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 7.0);
        Event E9 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 8.0);
        Event E10 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 9.0);
        Event E11 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 10.0);
        Event E12 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 11.0);
        Event E13 = new Event(Event.TypeE.Naissance, new Sim(Sim.Sex.F), 12.0);

        PQ<Event> pq = new PQ<Event>();
        pq.insert(E2);


    }
}

