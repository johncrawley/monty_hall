package com.jacstuff.montyhallproblem;

public class Door {
    private boolean containsPrize;
    private boolean isOpen;
    private int index;

    public Door(int index, boolean containsPrize){
        this.index = index;
        this.containsPrize = containsPrize;
    }


    public void open(){
        isOpen = true;
    }


    public int getIndex(){
        return index;
    }


    public boolean containsPrize(){
        return containsPrize;
    }


    public boolean isOpen(){
        return isOpen;
    }
}

