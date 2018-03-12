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
    
    public AssetFile getAssetFile(){
        return assetFile;
    }
    
    @Override
    public void initizalize() {
        super.initizalize();
    }
}
