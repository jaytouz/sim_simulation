package dev2;

public class Event implements Comparable<Event> {
    public enum TypeE {NAISSANCE, MORT, REPRODUCTION}
    protected double time;
    protected Sim subject;
    TypeE type;

    public Event(TypeE t, Sim x, double time){
        this.time = time;
        subject = x;
        type = t;
    }

    public void traiterEvent() {

    }

    public double getTime() {
        return time;
    }

    public Sim getSubject() {
        return subject;
    }

    public TypeE getType() {
        return type;
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.time);
    }


}