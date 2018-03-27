package engine;

import engine.special_objects.GameObject;

public abstract class Component extends StandardBehavior{
    
    
    private GameObject parent;
    
    abstract public void initialize();
    
    public GameObject getParent() {
        return parent;
    }
    
    public void setParent(GameObject parent) {
        this.parent = parent;
    }
}
