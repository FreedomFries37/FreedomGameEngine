package engine.special_objects;

import engine.AssetFile;
import engine.Component;
import engine.Serializable;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameObject extends Serializable{
    
    private ArrayList<GameObject> children;
    private ArrayList<Component> components;
    private String name;
    AssetFile assetFile;
    
    //CONSTRUCTOR
    public GameObject(){
        children = new ArrayList<>();
        components = new ArrayList<>();
        name = "";
        assetFile = null;
    }
    
    
    
    //MUTATOR METHODS
    public Component addComponent(Class<? extends Component> type){
        try{
            Component c = type.newInstance();
            components.add(c);
            c.initialize();
            return c;
        }catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean removeComponent(Component o){
        if(!components.contains(o)) return false;
        components.remove(o);
        return true;
    }
    
    public void addChild(GameObject o){
        children.add(o);
    }
    
    public boolean removeChild(GameObject o){
        if(!children.contains(o)) return false;
        children.remove(o);
        return true;
    }
    
    public ArrayList<GameObject> getChildren() {
        return children;
    }
    
    public ArrayList<Component> getComponents() {
        return components;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    void updateSelfAndChildren(){
        update();
        for(GameObject o : children){
            o.updateSelfAndChildren();
        }
    }
    
    public void inheritFromAssetFile(AssetFile file){
        assetFile = file;
    }
    
    
    
    
    //ENGINE METHODS
    public void update(){}
    
    public void start(){}
    
    public void initizalize(){}
    
    
    //OTHER METHODS
    
    @Override
    public String toString() {
        return name;
    }
    
    public String toStringExtended(){
        try{
            return toStringExtended(0);
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return "";
        }
    }
    public String toStringExtended(int indent) throws IllegalAccessException{
        StringBuilder output = new StringBuilder();
        
        output.append(((Serializable) this).toStringExtended(indent));
        
       
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("children={\n");
        for (Serializable c: getChildren()) {
            ((GameObject) c).initizalize();
            for (int i = 0; i < indent+1; i++) output.append("\t");
            output.append(c.getClass().getName());
            output.append("{\n");
            output.append(c.toStringExtended(indent+2));
            for (int i = 0; i < indent+1; i++) output.append("\t");
            output.append("}\n");
        }
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("}\n");
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("components={\n");
        for (Serializable c: getComponents()) {
            for (int i = 0; i < indent+1; i++) output.append("\t");
            output.append(c.getClass().getName());
            output.append("{\n");
            output.append(c.toStringExtended(indent+2));
            for (int i = 0; i < indent+1; i++) output.append("\t");
            output.append("}\n");
        }
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("}\n");
        
        return output.toString();
    }
    
}
