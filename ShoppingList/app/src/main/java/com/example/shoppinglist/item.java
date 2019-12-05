package com.example.shoppinglist;

import java.util.ArrayList;

public class item extends ArrayList {
private String cItemName;
    private String cItemQuantity;
    private Boolean cStrike;

    public item(String itemName, String itemQuantity, Boolean strike) {

        cItemName = itemName;
        cItemQuantity = itemQuantity;
        cStrike = strike;
    }


    public String getcItemName() {
        return cItemName;
    }

    public String getcItemQuantity() {
        return cItemQuantity;
    }

    public Boolean getcStrike() {
        return cStrike;
    }

}
