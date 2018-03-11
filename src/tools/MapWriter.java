package tools;

import engine.*;
import engine.special_objects.Map;

import java.io.File;
import java.io.IOException;


public class MapWriter {
   
    
    public static void main(String[] args){
    
        
        /*
        File mapFolder = new File("maps");
        if(!mapFolder.exists()) mapFolder.mkdir();
        File mapFiles[] = mapFolder.listFiles();
        if(mapFiles == null) return;
        for(File n : mapFiles){
            Map m = MapLoader.loadMapFromFile(n);
        }
        */
        
        Map m = MapWriter.createNewMapFile("test");
    }
    
    
    
    public static Map createNewMapFile(String name){
        File mapFolder = new File("maps");
        if(!mapFolder.exists()) mapFolder.mkdir();
        
        File newMapFile = new File("maps/" + name);
        try{
            newMapFile.createNewFile();
            return new Map(name, newMapFile);
        }catch(IOException e){
            System.out.println("Something went wrong making the file");
            return null;
        }
    }
    
    
    
    
    
   
}
