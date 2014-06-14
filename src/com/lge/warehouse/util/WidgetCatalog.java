/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kihyung2.lee
 */
public class WidgetCatalog {
    static private final HashMap<Integer, Widget> sMapInt2Widget;
    static private final HashMap<Class, Integer> sMapClass2Int;

    static {
        sMapInt2Widget = new HashMap<>();
        sMapClass2Int = new HashMap<>();

        addToCatalog(new SoccerBallWidget(0));
        addToCatalog(new YellowBaseBallWidget(1));
        addToCatalog(new WhiteBaseBallWidget(2));
        addToCatalog(new BasketBallWidget(3));
        addToCatalog(new FootBallWidget(4));
    }
    
    static private void addToCatalog(Widget w) {
        sMapInt2Widget.put(w.getProuctId(), w);
        sMapClass2Int.put(w.getClass(), w.getProuctId());
    }
       
    static int getProductIdOf(Class cls) {
        return sMapClass2Int.get(cls);
    }
    
    static Widget getWidgetById(int productId) {
        return sMapInt2Widget.get(productId);
    }
    
    static Widget getWidgetByClass(Class cls) {
        return getWidgetById(getProductIdOf(cls));
    }
}
