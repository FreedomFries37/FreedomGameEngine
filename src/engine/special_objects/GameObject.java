package engine.special_objects;

import engine.Component;

import java.util.ArrayList;

public class GameObject {
    
    private ArrayList<GameObject> children;
    private ArrayList<Component> components;
    private String name;
    
    //CONSTRUCTOR
    public GameObject(){
        children = new ArrayList<>();
        components = new ArrayList<>();
        name = "";
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
    
    
    
    
    //ENGINE METHODS
    public void update(){}
    
    public void start(){}
    
    
    //OTHER METHODS
    
    @Override
    public String toString() {
        return name;
    }
}
