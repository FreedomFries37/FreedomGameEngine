package tools;

import engine.special_components.Transform;
import engine.special_objects.Asset;
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
        
        Transform t1 = a1.getComponent(Transform.class);
        t1.position = new Vector3(3,4,5);
        Sphere s1 = a1.addChild(Sphere.class);
        s1.getComponent(Transform.class).position = new Vector3(3,2,2);
        s1.setName("MEGASPHERE");
        Sphere s2 = s1.addChild(Sphere.class);
        s2.radius = 10;
        s2.setName("smallsphere");
        s1.radius = 15;
        
        AssetWriter.updateAssetFile(a1);
        
        AssetParseTreeGenerator assetParseTreeGenerator = new AssetParseTreeGenerator(a1.getAssetFile());
        AssetParseTreeGenerator.ParseTree a2 = assetParseTreeGenerator.ReadAssetFile();
        a2.printParseTree();
    
        AssetTreeConverter assetTreeConverter = new AssetTreeConverter();
        assetTreeConverter.testTreeConverter();
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
