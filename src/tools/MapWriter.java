package tools;

import engine.special_components.Transform;
import engine.special_objects.Asset;
import engine.special_objects.GameObject;
import engine.special_objects.Map;
import engine.special_objects.Sphere;
import utils.Vector3;

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
       
        //a1.addComponent(Transform.class);
        Sphere s1 = a1.addChild(Sphere.class);
        Sphere s2 = s1.addChild(Sphere.class);
        a1.addComponent(Transform.class);
        Transform t1 = a1.getComponent(Transform.class);
        t1.position = new Vector3(5,4,3);
        s2.radius = 5;
        s1.getComponent(Transform.class).position = new Vector3(14,2,4);
        
        AssetWriter.updateAssetFile(a1);
        
        AssetReader assetReader = new AssetReader(a1.getAssetFile());
        AssetReader.ParseTree a2 = assetReader.ReadAssetFile();
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
