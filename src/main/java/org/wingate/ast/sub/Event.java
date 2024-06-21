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
    private Time start;
    private Time stop;
    private Style style;
    private String name;
    //private Effects effect;
    private Sentence text;
}
