package engine.special_objects;

import engine.special_components.Transform;

public class Sphere extends GameObject{
    
    public double radius;
    
    @Override
    public void initizalize() {
        //radius = 1;
        setName("Sphere");
        addComponent(Transform.class);
    }
    
    
}
