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
package org.wingate.ast.util;

import org.wingate.ast.sub.ASS;
import org.wingate.ast.sub.Event;
import org.wingate.ast.sub.Sentence;

/**
 *
 * @author util2
 */
public class AssTableModel extends javax.swing.table.DefaultTableModel {
    
    private ASS ass;

    public AssTableModel() {
        ass = new ASS();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0 -> { return Integer.class; }
            case 1, 2 -> { return String.class; }
            case 3 -> { return Sentence.class; } 
        }
        return Object.class;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch(column){
            case 0 -> { return "#"; }
            case 1 -> { return "Start"; }
            case 2 -> { return "End"; }
            case 3 -> { return "Text"; }
        }
        return "Unknown";
    }

    @Override
    public int getRowCount() {
        return ass == null || ass.getEvents().isEmpty() ? 0 : ass.getEvents().size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch(column){
            case 0 -> { return false; }
            case 1, 2, 3 -> { return true; }
        }
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch(column){
            case 0 -> { return row + 1; }
            case 1 -> { return ass.getEvents().get(row).getTime().getStartString(); }
            case 2 -> { return ass.getEvents().get(row).getTime().getEndString(); }
            case 3 -> { return ass.getEvents().get(row).getText(); }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Event event = ass.getEvents().get(row);
        switch(column){
            case 1 -> { if(aValue instanceof String str) event.getTime().setStartString(str); }
            case 2 -> { if(aValue instanceof String str) event.getTime().setEndString(str); }
            case 3 -> { if(aValue instanceof Sentence s) event.setText(s); }
        }
    }
    
    public void addEvent(Event event){
        ass.getEvents().add(event);
    }
    
    public void insertEvent(int row, Event event){
        ass.getEvents().add(row, event);
    }
    
    public void replaceEvent(int row, Event event){
        ass.getEvents().remove(row);
        ass.getEvents().add(row, event);
    }
    
    public void removeEvent(int row, Event event){
        ass.getEvents().remove(row);
    }

    public ASS getAss() {
        return ass;
    }

    public void setAss(ASS ass) {
        this.ass = ass;
    }
    
}
