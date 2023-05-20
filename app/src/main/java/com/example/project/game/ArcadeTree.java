package com.example.project.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArcadeTree {
    public HashMap<Integer, ArrayList> arcadeTree = new HashMap<>();

    public ArcadeTree() {
        ArrayList<ArrayList> group;
        ArrayList variant;

        group = new ArrayList<>();
        variant = new ArrayList<>();
        variant.add("part1"); variant.add(2);
        group.add(variant);
        arcadeTree.put(0, group);

        group = new ArrayList<>();
        variant = new ArrayList<>();
        variant.add("part2"); variant.add(2);
        group.add(variant);
        arcadeTree.put(1, group);

        group = new ArrayList<>();
        variant = new ArrayList<>();
        variant.add("part3"); variant.add(1);
        group.add(variant);
        arcadeTree.put(2, group);
    }

    public ArrayList getNextPart(int inputs) {
        ArrayList variants = arcadeTree.get(inputs);
        return (ArrayList) variants.get( (int)(Math.random()*(variants.size())) );
    }
}
