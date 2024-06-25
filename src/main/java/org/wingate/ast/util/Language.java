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

/**
 *
 * @author util2
 */
public enum Language {
	
    ARABIC("ar", "Arabic", "عربي"),
    AZERBAIJANI("az", "Azerbaijani", "Azərbaycan"),
    CATALAN("ca", "Catalan", "català"),
    CHINESE("zh", "Chinese", "中國人"),
    CZECH("cs", "Czech", "čeština"),
    DANISH("da", "Danish", "dansk"),
    DUTCH("nl", "Dutch", "Nederlands"),
    ENGLISH("en", "English", "English"),
    ESPERANTO("eo", "Esperanto", "Esperanto"),
    FINNISH("fi", "Finnish", "Suomalainen"),
    FRENCH("fr", "French", "Français"),
    GERMAN("de", "German", "Deutsch"),
    GREEK("el", "Greek", "Ελληνικά"),
    HEBREW("he", "Hebrew", "עִברִית"),
    HINDI("hi", "Hindi", "हिंदी"),
    HUNGARIAN("hu", "Hungarian", "Magyar"),
    INDONESIAN("id", "Indonesian", "bahasa Indonesia"),
    IRISH("ga", "Irish", "Gaeilge"),
    ITALIAN("it", "Italian", "Italiano"),
    JAPANESE("ja", "Japanese", "日本語"),
    KOREAN("ko", "Korean", "한국인"),
    PERSIAN("fa", "Persian", "فارسی"),
    POLISH("pl", "Polish", "Polski"),
    PORTUGUESE("pt", "Portuguese", "Português"),	
    RUSSIAN("ru", "Russian", "Русский"),
    SLOVAK("sk", "Slovak", "slovenský"),
    SPANISH("es", "Spanish", "Español"),
    SWEDISH("sv", "Swedish", "svenska"),
    TURKISH("tr", "Turkish", "Türkçe"),
    UKRAINIAN("uk", "Ukrainian", "українська"),
    NONE("none", "none", "none");
	
    String ISO_639;
    String englishName;
    String localeName;

    private Language(String ISO_639, String englishName, String localeName){
        this.ISO_639 = ISO_639;
        this.englishName = englishName;
        this.localeName = localeName;
    }

    public String getISO_639() {
        return ISO_639;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getLocaleName() {
        return localeName;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", englishName, localeName);
    }
    
    public static Language getFromISO(String iso){
        Language lng = NONE;
        
        for(Language language : values()){
            if(language.getISO_639().equalsIgnoreCase(iso)){
                lng = language;
                break;
            }
        }
        
        return lng;
    }
}
