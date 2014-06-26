/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lge.warehouse.customer.ui;

/**
 *
 * @author Jihun
 */
public class OrderTestSctipt {
    static final int SOCCER = 0;
    static final int TENNIS = 1;
    static final int BASEBALL = 2;
    static final int FOOTBALL = 3;
    static final int BASKETBALL = 4;
    static final int NEWWIDGET = 5;

    static final OrderTestUnit testScript[][] = {
        {   //0,0,0,0,0,-
            //Supervisor:Fill all widgets
            //10,10,10,10,10,-
            //Order 1 - Order all
            new OrderTestUnit(SOCCER,2), 
            new OrderTestUnit(TENNIS,3),
            new OrderTestUnit(BASEBALL,5),
            new OrderTestUnit(FOOTBALL,1),
            new OrderTestUnit(BASKETBALL,4)
        },
        {   //8,7,5,9,6,-
            //Order 2 - Partial order
            new OrderTestUnit(SOCCER,4),
            new OrderTestUnit(BASKETBALL,3)
        },
        {   //4,7,5,9,3,-
            //Order 3 - backorder
            new OrderTestUnit(TENNIS,10)
        },
        {   //Supervisor:Fill widget(Tennis)
            //4,17,5,9,3,-
            //4,7,5,9,3,-
            //Supervisor:Add new widget
            //4,7,5,9,3,0
            //Supervisor:Fill new widget
            //4,7,5,9,3,10
            //Order 4 - new widget
            new OrderTestUnit(NEWWIDGET,5)
        },
        
//        {   //4,7,5,9,3,5
//            //Fill (6,3,5,1,7,5)
//            //10,10,10,10,10
//            //Order 5 - order all
//            new OrderTestUnit(SOCCER,2), 
//            new OrderTestUnit(TENNIS,3),
//            new OrderTestUnit(BASEBALL,5),
//            new OrderTestUnit(FOOTBALL,1),
//            new OrderTestUnit(BASKETBALL,4)
//        },
//        {   //8,7,5,9,6,-
//            //Order 6 - Partial order
//            new OrderTestUnit(SOCCER,4),
//            new OrderTestUnit(BASKETBALL,3)
//        },
       
        
      
        
    };
    
    private static int curScriptNo = 0;
    
    public OrderTestUnit getOrderTestUnit(int scriptNo, int index) {
        return testScript[scriptNo][index];
    }
    
    public int getOrderScriptSize(int scriptNo) {
        return testScript[scriptNo].length;
    }
    
    public int getTotalScriptSize() {
        return testScript.length;
    }
    
    public int getNextScriptNo() {
        if(curScriptNo >= getTotalScriptSize()) {
            return -1;
        } else {
            return curScriptNo++;
        }
    }
    
}
