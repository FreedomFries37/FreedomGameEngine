package engine.special_objects;

import engine.special_components.Transform;
import utils.Vector3;

public class Sphere extends GameObject{
    
    public double radius;
    public String gamma;
    public Vector3 postition2;
    
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
