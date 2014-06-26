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

    static final OrderTestUnit testScript[][] = {
        {   //Order 1
            new OrderTestUnit(0,7), 
            new OrderTestUnit(1,5),
            new OrderTestUnit(2,3),
            new OrderTestUnit(3,8),
            new OrderTestUnit(4,10)
        },
        {   //Order 2
            new OrderTestUnit(5,15)
        },
//        {   //Order 3
//            new OrderTestUnit(0,10),
//        },
//        {   //Order 4
//            new OrderTestUnit(0,5),
//            new OrderTestUnit(3,7)
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
