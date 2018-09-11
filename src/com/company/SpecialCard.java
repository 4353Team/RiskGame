package com.company;

import java.util.ArrayList;
import java.util.List;

public class SpecialCard extends Card {
    private List<TroopsType> tr;
    public SpecialCard(TroopsType t1, TroopsType t2,TroopsType t3) {
        tr = new ArrayList<TroopsType>();
        tr.add(t1);
        tr.add(t2);
        tr.add(t3);
    }
    public SpecialCard() {
    }
    public String toString() {
        String output = "";
        for (TroopsType t:
             tr) {
            output = output + t + " ";
        }
        return output;
    }
}
