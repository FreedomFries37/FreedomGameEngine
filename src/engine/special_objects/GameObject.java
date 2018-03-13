package engine.special_objects;

import engine.AssetFile;
import engine.Component;
import engine.Serializable;
import engine.StandardBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GameObject extends StandardBehavior{
    
    private ArrayList<GameObject> children;
    private ArrayList<Component> components;
    private String name;
    AssetFile assetFile;
    
    
    //CONSTRUCTOR
    public GameObject(){
        //super();
        children = new ArrayList<>();
        components = new ArrayList<>();
        name = "";
        assetFile = null;
        setGameObject();
    }
    
    
    
    //MUTATOR METHODS
    @SuppressWarnings("unchecked")
    public <T extends Component> T addComponent(Class<T> type){
        for(Component c : components){
            if(c.getClass().getDeclaringClass().equals(type.getClass())) return (T) c;
        }
        try{
            T c = type.newInstance();
            components.add(c);
            c.setParent(this);
            c.setGameObject();
            c.initialize();
            return c;
        }catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addComponent(Component o){
        for(Component c : components){
            if(c.getClass() == o.getClass()) return false;
        }
        components.add(o);
        o.setParent(this);
        o.gameObject = this;
        return true;
    }
    
    public boolean removeComponent(Component o){
        if(!components.contains(o)) return false;
        components.remove(o);
        return true;
    }
    
    public void addChild(GameObject o){
        children.add(o);
    }
    
    /*
    public GameObject addChild(Class<? extends GameObject> type){
        try{
            GameObject g = type.newInstance();
            g.initizalize();
            g.setInitialized();
            children.add(g);
            return g;
        }catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        
        return null;
    }
    */
    
    public <T extends GameObject> T addChild(Class<T> type){
        try{
            T g = type.newInstance();
            //g.initizalize();
            //g.setInitialized();
            children.add(g);
            return g;
        }catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        
        return null;
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
    
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type){
        
        for (Component c:
                getComponents()) {
            
            
            if(c.getClass() == type) return (T) c;
            
            //if(compT.getClass())) return (T) c;
        }
        
        return null;
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
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("name=\"");
        output.append(getName());
        output.append("\"\n");
        
        output.append(stringOfFields(indent));
        
       
        for (int i = 0; i < indent; i++) output.append("\t");
        output.append("children={\n");
        for (Serializable c: getChildren()) {
            //if(!c.initialzed()) ((GameObject) c).initizalize();
            for (int i = 0; i < indent+1; i++) output.append("\t");
            output.append(c.getClass().getName());
            if(((GameObject) c).assetFile != null){
                output.append("[A]\"");
                output.append(((Asset) c).assetFile);
                output.append("\"\n");
            }else {
                output.append("{\n");
                output.append(c.toStringExtended(indent + 2));
                for (int i = 0; i < indent + 1; i++) output.append("\t");
                output.append("}\n");
            }
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
