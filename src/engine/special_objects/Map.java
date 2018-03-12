package engine.special_objects;


import java.io.File;
import java.io.IOException;

public class Map extends GameObject {
    
    private File associatedFile;
    
    public Map(String name, File mapFile){
        super();
        setName(name);
        associatedFile = mapFile;
    }
    
    /*
    public static Map getMap(String path){
        try{
            return new Map(name, path);
        }catch(IOException e){
            System.out.println("Something Went Wrong With the Map");
            return null;
        }
    }
    */
    
    public void runUpdate(){
        updateSelfAndChildren();
    }
    
    public void printMap(){
        printObject(this, 0);
    }
    private void printObject(GameObject object, int indent){
        System.out.println(this);
        for(int i = 0; i < indent; i++){
            System.out.print("  ");
        }
        for(GameObject child : getChildren()){
            printObject(child, indent+1);
        }
    }
    
    public File getAssociatedFile() {
        return associatedFile;
    }
    
    @Override
    public void initizalize() {
    
    }
}
