package tools;

import engine.special_components.Transform;
import engine.special_objects.Asset;
import engine.special_objects.GameObject;
import engine.special_objects.Map;
import engine.special_objects.Sphere;

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
        Asset a1 = new Asset("test object");
        GameObject o1 = new Sphere();
        a1.addComponent(Transform.class);
        a1.addChild(o1);
        AssetWriter.updateAssetFile(a1);
    }
    
    
    
    public static Map createNewMapFile(String name){
        File mapFolder = new File("maps");
        if(!mapFolder.exists()) mapFolder.mkdir();
        
        File newMapFile = new File("maps/" + name + ".mapfile");
        try{
            if(newMapFile.createNewFile()) System.out.println("Created new map at " + newMapFile.getAbsolutePath());
            return new Map(name, newMapFile);
        }catch(IOException e){
            System.out.println("Something went wrong making the file");
            return null;
        }
    }
    
    
    
    
    
   
}
