package engine;

import java.util.ArrayList;

public class ObjectBehavior {
    
    private ArrayList<ObjectBehavior> children;
    private ArrayList<Component> components;
    
    public ObjectBehavior(){
        children = new ArrayList<>();
        components = new ArrayList<>();
    }
    
    public void addComponent(Component c){
        components.add(c);
    }
    
    public boolean removeComponent(Component o){
        if(!components.contains(o)) return false;
        components.remove(o);
        return true;
    }
    
    public void addChild(ObjectBehavior o){
        children.add(o);
    }
    
    public boolean removeChild(ObjectBehavior o){
        if(!children.contains(o)) return false;
        children.remove(o);
        return true;
    }
    
}
