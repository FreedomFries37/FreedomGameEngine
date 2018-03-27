package engine.special_objects;

import engine.AssetFile;
import engine.special_objects.GameObject;

public class Asset extends GameObject{

    public Asset(String name){
        super();
        String newName = name.replace(' ', '_');
        setName(newName);
        AssetFile file = AssetFile.createAssetFile(newName);
        inheritFromAssetFile(file);
    }
    
    public Asset(GameObject g){
        this(g.getName());
        gameObject = g;
    }
    
    
    public AssetFile getAssetFile(){
        return assetFile;
    }
    
    @Override
    public String toStringExtended(){
        String output = gameObject.getClass().getName() + "\n";
        try {
            return output + toStringExtended(0);
        }catch(IllegalAccessException e){
            return "";
        }
    }
    
    @Override
    public void initizalize() {
        super.initizalize();
    }
}
