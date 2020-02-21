package com.krm.tmdb.videoplayer.floatwindow;

public class FloatParams implements Cloneable {

    public int x;
    public int y;
    public int w;
    public int h;
    public int round;
    public float fade = 1;
    public boolean canMove = true;
    public boolean canCross = true;

    public boolean systemFloat;


    @Override
    public FloatParams clone() {
        FloatParams stu = null;
        try {
            stu = (FloatParams) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }

}