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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import org.wingate.ast.sub.Event;

/**
 *
 * @author util2
 */
public class AssTableRenderer extends javax.swing.table.DefaultTableCellRenderer {
    
    public AssTableRenderer() {
        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if(value instanceof Event){
            switch(column){
                case 1 -> { setForeground(Color.green.darker()); }
                case 2 -> { setForeground(Color.red); }
                case 3 -> { setForeground(Color.blue.darker()); }
            }
            
            setBackground(row % 2 == 0 ? Color.lightGray : Color.white);
            
            if(column == 0){
                setBackground(Color.magenta.darker());
                setForeground(Color.white);
            }
        }
        
        return this;
    }
    
    
    
}
