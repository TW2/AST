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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author util2
 */
public class SRT {
    
    private final List<Style> styles = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();

    public SRT() {
        styles.add(new Style());
    }

    public List<Style> getStyles() {
        return styles;
    }

    public List<Event> getEvents() {
        return events;
    }
    
    public void write(String path){
        int counter = 1;
        String start, end;
        try(PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);){
            for(Event event : events){
                pw.println(counter);
                start = event.getTime().getStartString().replace(".", ",").concat("0");
                end = event.getTime().getEndString().replace(".", ",").concat("0");
                pw.println(String.format("%s --> %s", start, end));
                if(event.getText().getText().contains("\\N")){
                    String[] t = event.getText().getText().split("\\\\N");
                    for(String s : t){
                        pw.println(s);
                    }
                }else{
                    pw.println(event.getText().getText());
                }
                pw.println("");
                counter++;
            }
        } catch (IOException ex) {
            Logger.getLogger(SRT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void read(String path){
        try(FileReader fr = new FileReader(path, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);){
            String line, text = "";
            Time time = null;
            boolean input = false;
            while((line = br.readLine()) != null){
                if(line.matches("\\d+")){
                    input = true;
                }else if(input == true){
                    if(line.isEmpty()){
                        Event event = new Event();
                        event.setType(Event.Type.Dialogue);
                        event.setLayer(0);
                        event.setTime(time != null ? time : new Time());
                        event.setStyle(styles.get(0));
                        event.setName("");
                        event.setMarginL(10);
                        event.setMarginR(10);
                        event.setMarginV(10);
                        event.setMarginT(10);
                        event.setMarginB(10);
                        event.setEffect("");
                        event.setText(new Sentence(text));
                        events.add(event);
                        
                        input = false;
                        text = "";
                    }else if(line.contains("->")){
                        String[] t = line.split(" ");
                        time = new Time(t[0], t[2]);
                    }else{
                        text += text.isEmpty() ? line : "\\N" + line;
                    }
                }
            }
            
            if(input == true){
                Event event = new Event();
                event.setType(Event.Type.Dialogue);
                event.setLayer(0);
                event.setTime(time != null ? time : new Time());
                event.setStyle(styles.get(0));
                event.setName("");
                event.setMarginL(10);
                event.setMarginR(10);
                event.setMarginV(10);
                event.setMarginT(10);
                event.setMarginB(10);
                event.setEffect("");
                event.setText(new Sentence(text));
                events.add(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(SRT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
