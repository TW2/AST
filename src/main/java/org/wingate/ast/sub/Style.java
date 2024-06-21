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

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import org.wingate.ast.util.Helper;

/**
 *
 * @author util2
 */
public class Style {
    
    private String name;
    private Font font;
    private Color textColor;
    private Color karaokeColor;
    private Color outlineColor;
    private Color shadowColor;
    private float scaleX;
    private float scaleY;
    private float scaleZ;
    private float spacing;
    private float angleX;
    private float angleY;
    private float angleZ;
    private int borderStyle;
    private float outline;
    private float shadow;
    private int alignment;
    private int marginL;
    private int marginR;
    private int marginV;
    private int marginT;
    private int marginB;
    private float alphaLevel;
    private int encoding;
    private float relativeTo;
    private final Map<TextAttribute, Object> attributes = new HashMap<>();

    public Style() {
        name = "Default";
        font = new Font("Arial", Font.PLAIN, 28);
        textColor = Color.white;
        karaokeColor = Color.yellow;
        outlineColor = Color.black;
        shadowColor = Color.black;
        scaleX = 1f; scaleY = 1f; scaleZ = 1f;
        spacing = 0;
        angleX = 0f; angleY = 0f; angleZ = 0f;
        borderStyle = 1; // 1 or 3
        outline = 2f; shadow = 2f;
        alignment = 2; // 1 to 9
        marginL = 10; marginR = 10; marginV = 10;
        marginT = 10; marginB = 10;
        alphaLevel = 0;
        encoding = 1;
        relativeTo = 0f;
    }
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Style(String raw) {
        String[] t = raw.split(",");
        name = t[0].substring("Style: ".length());
        font = new Font(t[1], Font.PLAIN, Integer.parseInt(t[2]));
        textColor = Helper.getFromABGR(t[3]);
        karaokeColor = Helper.getFromABGR(t[4]);
        outlineColor = Helper.getFromABGR(t[5]);
        shadowColor = Helper.getFromABGR(t[6]);
        setFont(t[1], Integer.parseInt(t[2]), t[7].equals("-1"), t[8].equals("-1"), t[9].equals("-1"), t[10].equals("-1"));
        scaleX = Float.parseFloat(t[11]) / 100f; scaleY = Float.parseFloat(t[12]) / 100f; scaleZ = 1f;
        spacing = Integer.parseInt(t[13]);
        angleX = Float.parseFloat(t[14]); angleY = 0f; angleZ = 0f;
        borderStyle = Integer.parseInt(t[15]); // 1 or 3
        outline = Float.parseFloat(t[16]); shadow = Float.parseFloat(t[17]);
        alignment = Integer.parseInt(t[18]); // 1 to 9
        marginL = Integer.parseInt(t[19]); marginR = Integer.parseInt(t[20]); marginV = Integer.parseInt(t[21]);
        marginT = 10; marginB = 10;
        alphaLevel = 0;
        encoding = Integer.parseInt(t[22]);
        relativeTo = 0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
    
    public void setFont(String name, float size, boolean bold, boolean italic, boolean underline, boolean strikeout){
        attributes.put(TextAttribute.FAMILY, name);
        attributes.put(TextAttribute.SIZE, size);
        attributes.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        attributes.put(TextAttribute.POSTURE, italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        attributes.put(TextAttribute.UNDERLINE, underline ? 0 : -1);
        attributes.put(TextAttribute.STRIKETHROUGH, strikeout);
        Font f = new Font(attributes);
        this.font = f;
    }
    
    public boolean isBold(){
        boolean v = false;
        for(Map.Entry<TextAttribute, Object> entry : attributes.entrySet()){
            if(entry.getKey() == TextAttribute.WEIGHT){
                if(entry.getValue() == TextAttribute.WEIGHT_BOLD){                        
                    v = true;
                }
            }
        }
        return v;
    }
    
    public boolean isItalic(){
        boolean v = false;
        for(Map.Entry<TextAttribute, Object> entry : attributes.entrySet()){
            if(entry.getKey() == TextAttribute.POSTURE){
                if(entry.getValue() == TextAttribute.POSTURE_OBLIQUE){                        
                    v = true;
                }
            }
        }
        return v;
    }
    
    public boolean isUnderline(){
        boolean v = false;
        for(Map.Entry<TextAttribute, Object> entry : attributes.entrySet()){
            if(entry.getKey() == TextAttribute.UNDERLINE){
                if(entry.getValue() instanceof Integer i){
                    if(i==0) v = true;
                }
            }
        }
        return v;
    }
    
    public boolean isStrikeOut(){
        boolean v = false;
        for(Map.Entry<TextAttribute, Object> entry : attributes.entrySet()){
            if(entry.getKey() == TextAttribute.STRIKETHROUGH){
                if(entry.getValue() instanceof Boolean b){                        
                    v = b;
                }
            }
        }
        return v;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
    
    public void setTextColor(String A_BGR){
        this.textColor = Helper.getFromABGR(A_BGR);
    }

    public Color getKaraokeColor() {
        return karaokeColor;
    }

    public void setKaraokeColor(Color karaokeColor) {
        this.karaokeColor = karaokeColor;
    }
    
    public void setKaraokeColor(String A_BGR){
        this.karaokeColor = Helper.getFromABGR(A_BGR);
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
    
    public void setOutlineColor(String A_BGR){
        this.outlineColor = Helper.getFromABGR(A_BGR);
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }
    
    public void setShadowColor(String A_BGR){
        this.shadowColor = Helper.getFromABGR(A_BGR);
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public float getAngleX() {
        return angleX;
    }

    public void setAngleX(float angleX) {
        this.angleX = angleX;
    }

    public float getAngleY() {
        return angleY;
    }

    public void setAngleY(float angleY) {
        this.angleY = angleY;
    }

    public float getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(float angleZ) {
        this.angleZ = angleZ;
    }

    public int getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(int borderStyle) {
        this.borderStyle = borderStyle;
    }

    public float getOutline() {
        return outline;
    }

    public void setOutline(float outline) {
        this.outline = outline;
    }

    public float getShadow() {
        return shadow;
    }

    public void setShadow(float shadow) {
        this.shadow = shadow;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
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

    public float getAlphaLevel() {
        return alphaLevel;
    }

    public void setAlphaLevel(float alphaLevel) {
        this.alphaLevel = alphaLevel;
    }

    public int getEncoding() {
        return encoding;
    }

    public void setEncoding(int encoding) {
        this.encoding = encoding;
    }

    public float getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(float relativeTo) {
        this.relativeTo = relativeTo;
    }
    
}
