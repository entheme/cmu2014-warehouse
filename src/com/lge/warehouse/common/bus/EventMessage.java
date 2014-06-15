/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.lge.warehouse.common.bus;

import com.lge.warehouse.common.app.EventMessageType;
import com.lge.warehouse.common.app.WComponentType;
import java.io.Serializable;
/**
 *
 * @author seuki77
 */
public class EventMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private EventMessageType mType;
    private String mSrc;
    private String mDest;

    private Serializable mBody;
    public EventMessage(String src, String dest, EventMessageType type, Serializable body) {
        mSrc = src;
        mDest = dest;
        mType = type;
        mBody = body;
    }

    public String getSrc() {
        return mSrc;
    }
    public String getDest() {
        return mDest;
    }
    public EventMessageType getType() {
        return mType;
    }
    public Serializable getBody() {
        return mBody;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Src(").append(mSrc).append(")->Dest(").append(mDest).append(") : Type(").append(mType).append(")");
        return sb.toString();
    }
    
}
