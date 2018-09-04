package com.company;

import java.util.ArrayList;
import java.util.List;

public class SpecialCard extends Card {
    List<TroopsType> t;
    public SpecialCard() {
        t = new ArrayList<TroopsType>();
        t.add(TroopsType.INFANTRY);
    }
}
