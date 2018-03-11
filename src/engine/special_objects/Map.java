package engine.special_objects;


import java.io.File;
import java.io.IOException;

public class Map extends ObjectBehavior {
    
    File associatedFile;
    
    public Map(String name) throws IOException{
        super();
        setName(name);
        String path = "maps/" + name;
        associatedFile = new File(path);
        if(associatedFile.createNewFile()) System.out.println("New file at " + associatedFile.getAbsolutePath() + " created");
    }
    
    public static Map getMap(String name){
        try{
            return new Map(name);
        }catch(IOException e){
            System.out.println("Something Went Wrong With the Map");
            return null;
        }
    }
    
    public void runUpdate(){
        updateSelfAndChildren();
    }
    
    public void printMap(){
        printObject(this, 0);
    }
    private void printObject(ObjectBehavior object, int indent){
        System.out.println(this);
        for(int i = 0; i < indent; i++){
            System.out.print("  ");
        }
        for(ObjectBehavior child : getChildren()){
            printObject(child, indent+1);
        }
    }
}
