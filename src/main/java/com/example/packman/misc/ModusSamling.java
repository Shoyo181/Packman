package com.example.packman.misc;

import java.util.LinkedList;
import java.util.Stack;

public class ModusSamling {

    private LinkedList<ModusTid> stack;

    public ModusSamling(){
        stack = new LinkedList<>();
    }

    public void push(ModusTid modusTid){
        stack.addFirst(modusTid);
    }

    public ModusTid pop(){
        return stack.removeFirst();
    }
    public Object peek() {
        return stack.peekFirst();
    }
    public boolean erTom(){
        return stack.isEmpty();
    }
    public int size(){
        return stack.size();
    }

}
