package engine.special_objects;

import engine.special_components.Transform;

public class Sphere extends GameObject{
    
    public double radius;
    public char letter = 'a';
    public boolean condition = true;
    public String gamma = "$yolo";
    
    private static int spheresCreated = 0;
    
    @Override
    public void initizalize() {
        //radius = 1;
        setName("Sphere" + spheresCreated);
        spheresCreated++;
        gamma = getName();
        //addComponent(Transform.class);
    }
    
    
}
