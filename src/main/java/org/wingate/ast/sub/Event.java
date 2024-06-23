/*
 * Copyright (C) 2024 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.wingate.ast.sub;

import java.util.List;

/**
 *
 * @author util2
 */
public class Event {
    public enum Type {
        Dialogue("Dialogue", "D"),
        Comment("Comment", "#");
        
        String name;
        String column;
        
        private Type(String name, String column){
            this.name = name;
            this.column = column;
        }

        public String getName() {
            return name;
        }

        public String getColumn() {
            return column;
        }
        
        public static Type get(String s){
            Type type = Comment;
            
            for(Type t : values()){
                if(t.getName().equalsIgnoreCase(s) || t.getColumn().equals(s)){
                    type = t;
                    break;
                }
            }
            
            return type;
        }
    }
    
    private Type type;
    private int layer;
    private int marginL;
    private int marginR;
    private int marginV;
    private int marginT;
    private int marginB;
    private Time time;
    private Style style;
    private String name;
    private String effect;
    private Sentence text;

    public Event() {
        type = Type.Dialogue;
        layer = 0;
        marginL = 10;
        marginR = 10;
        marginV = 10;
        marginT = 10;
        marginB = 10;
        time = new Time();
        style = new Style();
        name = "";
        effect = "";
        text = new Sentence();
    }
    
    public Event(String raw, List<Style> styles) {
        String[] t = raw.split(",", 10);
        type = Type.get(raw.substring(0, raw.indexOf(":")));
        layer = Integer.parseInt(t[0].substring(t[0].indexOf(" ") + 1, t[0].length()));        
        time = new Time(t[1], t[2]);
        for(Style sty : styles){
            if(sty.getName().equals(t[3])){
                style = sty;
                break;
            }
        }
        if(style == null) style = styles.get(0);
        name = t[4];
        marginL = Integer.parseInt(t[5]);
        marginR = Integer.parseInt(t[6]);
        marginV = Integer.parseInt(t[7]);
        marginT = marginV / 2;
        marginB = marginV / 2;
        effect = t[8];
        text = new Sentence(t[9]);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getMarginL() {
        return marginL;
    }

    public void setMarginL(int marginL) {
        this.marginL = marginL;
    }

    public int getMarginR() {
        return marginR;
    }

    public void setMarginR(int marginR) {
        this.marginR = marginR;
    }

    public int getMarginV() {
        return marginV;
    }

    public void setMarginV(int marginV) {
        this.marginV = marginV;
    }

    public int getMarginT() {
        return marginT;
    }

    public void setMarginT(int marginT) {
        this.marginT = marginT;
    }

    public int getMarginB() {
        return marginB;
    }

    public void setMarginB(int marginB) {
        this.marginB = marginB;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Sentence getText() {
        return text;
    }

    public void setText(Sentence text) {
        this.text = text;
    }
    
}
