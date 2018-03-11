package engine.special_components;

import engine.Component;
import utils.Vector3;

public class Transform implements Component {
    Vector3 position;
    
    @Override
    public void initialize() {
        position.x = position.y = position.z = 0;
    }
}
