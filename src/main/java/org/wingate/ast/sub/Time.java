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
public class Time {
    private long msStart;
    private long msStop;

    public Time() {
        this(0L, 0L);
    }

    public Time(long msStart, long msStop) {
        this.msStart = msStart;
        this.msStop = msStop;
    }

    public long getMsStart() {
        return msStart;
    }

    public void setMsStart(long msStart) {
        this.msStart = msStart;
    }

    public long getMsStop() {
        return msStop;
    }

    public void setMsStop(long msStop) {
        this.msStop = msStop;
    }
    
    public boolean isBetween(long msCurrent){
        return msStart <= msCurrent && msCurrent < msStop;
    }
    
    public long getExtendStart(long ms){
        return msStart - ms;
    }
    
    public long getExtendStop(long ms){
        return msStop + ms;
    }
    
    public long getInnerStart(long ms){
        return msStart + ms;
    }
    
    public long getInnerStop(long ms){
        return msStop - ms;
    }
    
    public long getDuration(){
        return Math.max(msStart, msStop) - Math.min(msStart, msStop);
    }
    
    public long[] getDurations(int part){
        long times = Math.round(Math.floor((double)getDuration() / (double)part));
        long last = getDuration() - (times * (part - 1));
        long s[] = new long[part];
        for(int i=0; i<part-1; i++){
            s[i] = times;
        }
        s[part-1] = last;
        return s;
    }
    
    public long[] getStarts(long[] durations){
        long[] s = new long[durations.length];
        long v = 0;
        for(int i=0; i<s.length; i++){
            s[i] = v;
            v += durations[i];
        }
        return s;
    }
    
    public long[] getEnds(long[] durations){
        long[] s = new long[durations.length];
        long v = 0;
        for(int i=0; i<s.length; i++){
            v += durations[i];
            s[i] = v;            
        }
        return s;
    }
}
