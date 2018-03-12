package engine.special_objects;

import engine.AssetFile;
import engine.special_objects.GameObject;

public class Asset extends GameObject{

    public Asset(String name){
        super();
        setName(name);
        AssetFile file = AssetFile.createAssetFile(name);
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
