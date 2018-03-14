package tools;

import engine.AssetFile;
import engine.special_objects.Asset;

import java.io.FileWriter;
import java.io.IOException;

public class AssetWriter {
    
    public static void updateAssetFile(Asset f){
        AssetFile file = f.getAssetFile();
        if(!file.delete()) return;
        file = AssetFile.createAssetFile(f.getName());
        
        FileWriter writer;
        
        try{
           
            writer = new FileWriter(file);
    
            
            String s = f.toStringExtended();
            writer.write(s);
    
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    
}
