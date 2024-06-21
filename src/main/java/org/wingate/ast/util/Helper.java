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

/**
 *
 * @author util2
 */
public class Helper {

    public Helper() {
    }
    
    public static Color getFromABGR(String A_BGR){
        String c = A_BGR.replace("&", "");
        c = c.replace("H", "");
        switch (c.length()) {
            case 8 -> {
                int a = 255 - Integer.parseInt(c.substring(0, 2), 16);
                int b = Integer.parseInt(c.substring(2, 4), 16);
                int g = Integer.parseInt(c.substring(4, 6), 16);
                int r = Integer.parseInt(c.substring(6), 16);
                return new Color(r, g, b, a);
            }
            case 6 -> {
                int b = Integer.parseInt(c.substring(0, 2), 16);
                int g = Integer.parseInt(c.substring(2, 4), 16);
                int r = Integer.parseInt(c.substring(4), 16);
                return new Color(r, g, b);
            }
            default -> {
                return Color.black;
            }
        }
    }
    
    public static Color getFromAHTML(String A_HTML){
        String c = A_HTML.replace("#", "");
        switch (c.length()) {
            case 8 -> {
                int a = 255 - Integer.parseInt(c.substring(0, 2), 16);
                int r = Integer.parseInt(c.substring(2, 4), 16);
                int g = Integer.parseInt(c.substring(4, 6), 16);
                int b = Integer.parseInt(c.substring(6), 16);
                return new Color(r, g, b, a);
            }
            case 6 -> {
                int r = Integer.parseInt(c.substring(0, 2), 16);
                int g = Integer.parseInt(c.substring(2, 4), 16);
                int b = Integer.parseInt(c.substring(4), 16);
                return new Color(r, g, b);
            }
            default -> {
                return Color.black;
            }
        }
    }
    
    public static String getABGR(Color color){
        String a = Integer.toHexString(255 - color.getAlpha());
        if(a.length() == 1) a = "0" + a;
        String b = Integer.toHexString(color.getBlue());
        if(b.length() == 1) b = "0" + b;
        String g = Integer.toHexString(color.getGreen());
        if(g.length() == 1) g = "0" + g;
        String r = Integer.toHexString(color.getRed());
        if(r.length() == 1) r = "0" + r;
        return "&H" + a + b + g + r + "&";
    }
    
    public static String getBGR(Color color){
        String b = Integer.toHexString(color.getBlue());
        if(b.length() == 1) b = "0" + b;
        String g = Integer.toHexString(color.getGreen());
        if(g.length() == 1) g = "0" + g;
        String r = Integer.toHexString(color.getRed());
        if(r.length() == 1) r = "0" + r;
        return "&H" + b + g + r + "&";
    }
    
    public static String getAHTML(Color color){
        String a = Integer.toHexString(255 - color.getAlpha());
        if(a.length() == 1) a = "0" + a;
        String b = Integer.toHexString(color.getBlue());
        if(b.length() == 1) b = "0" + b;
        String g = Integer.toHexString(color.getGreen());
        if(g.length() == 1) g = "0" + g;
        String r = Integer.toHexString(color.getRed());
        if(r.length() == 1) r = "0" + r;
        return "#" + a + r + g + b;
    }
    
    public static String getHTML(Color color){
        String b = Integer.toHexString(color.getBlue());
        if(b.length() == 1) b = "0" + b;
        String g = Integer.toHexString(color.getGreen());
        if(g.length() == 1) g = "0" + g;
        String r = Integer.toHexString(color.getRed());
        if(r.length() == 1) r = "0" + r;
        return "#" + r + g + b;
    }
}
