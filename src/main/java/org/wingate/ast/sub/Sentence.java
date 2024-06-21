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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author util2
 */
public class Sentence {
    
    private String text;
    private String comments;

    public Sentence() {
        this("", "");
    }

    public Sentence(String text) {
        this(text, "");
    }

    public Sentence(String text, String comments) {
        this.text = text;
        this.comments = comments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean containsEffects() {
        return text.contains("{\\");
    }
    
    public List<FxAndWords> getEffects(){
        final List<FxAndWords> list = new ArrayList<>();
        String s = text.replace("}{", "");
        Pattern p = Pattern.compile("(?<fx>\\{[^\\}]+)\\}*(?<str>[^\\{]*)");
        Matcher m = p.matcher(s);
        while(m.find()){
            list.add(new FxAndWords(m.group("fx").concat("}"), m.group("str")));
        }
        return list;
    }

    @Override
    public String toString() {
        return text;
    }
    
}
