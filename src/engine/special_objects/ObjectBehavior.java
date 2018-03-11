package engine.special_objects;

import engine.Component;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;

public class ObjectBehavior {
    
    private ArrayList<ObjectBehavior> children;
    private ArrayList<Component> components;
    private String name;
    
    //CONSTRUCTOR
    public ObjectBehavior(){
        children = new ArrayList<>();
        components = new ArrayList<>();
        name = "";
    }
    
    
    //MUTATOR METHODS
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
    
    public ArrayList<ObjectBehavior> getChildren() {
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
        for(ObjectBehavior o : children){
            o.updateSelfAndChildren();
        }
    }
    
    
    
    
    //ENGINE METHODS
    public void update(){}
    
    public void start(){}
    
    
    //OTHER METHODS
    
    @Override
    public String toString() {
        return name;
    }
}
