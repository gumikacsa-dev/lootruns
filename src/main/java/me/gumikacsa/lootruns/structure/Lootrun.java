package me.gumikacsa.lootruns.structure;

public class Lootrun {

    public Point[] points;

    @Deprecated
    public Lootrun() {}

    public Lootrun(Point... points) {
        this.points = points;
    }

}
