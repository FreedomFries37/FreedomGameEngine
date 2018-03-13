package tools;

import engine.special_components.Transform;
import engine.special_objects.*;
import utils.Line;
import utils.Plane;
import utils.Vector2;
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
        s1.postition2 = new Vector3(3,2,2);
        Sphere s2 = a1.addChild(Sphere.class);
        s2.radius = 10;
        s2.setName("smallsphere");
        s1.radius = 15;
        
        
        ScenePlane p1 = a1.addChild(ScenePlane.class);
        p1.plane = new Plane(new Line(new Vector2(0,1), new Vector2(1,0)),new Vector2(0,0));
        p1.setName("The_Plane");
        
        AssetWriter.updateAssetFile(a1);
        
        AssetParseTreeGenerator assetParseTreeGenerator = new AssetParseTreeGenerator(a1.getAssetFile());
        ParseTree a2 = assetParseTreeGenerator.ReadAssetFile();
        a2.printParseTree();
    
        AssetTreeConverter assetTreeConverter = new AssetTreeConverter();
        GameObject testObject = assetTreeConverter.createGameObjectFromAssetTree(a2);
        System.out.println(testObject.getName());
        
        
        //assetTreeConverter.testAssetTreeConverter();
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
