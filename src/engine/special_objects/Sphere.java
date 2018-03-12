package engine.special_objects;

public class Sphere extends GameObject{
    
    public double radius;
    
    @Override
    public void initizalize() {
        radius = 3;
        setName("Sphere");
    }
}
