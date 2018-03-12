package engine;

import java.io.File;
import java.io.IOException;

public class AssetFile extends File {
    
    public AssetFile(String path){
        super(path);
    }
    
    public static AssetFile createAssetFile(String name){
        AssetFile f = new AssetFile("assets/" + name + ".asset");
        try {
            if (f.createNewFile()) System.out.println("Created new Asset File: " + name);
        }catch (IOException e){
            System.out.println("Failed to create file");
            return null;
        }
        return f;
    }
}

