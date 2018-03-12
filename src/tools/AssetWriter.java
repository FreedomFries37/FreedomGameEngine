package tools;

import engine.Component;
import engine.Serializable;
import engine.special_objects.Asset;
import engine.AssetFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

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
    
    private static String componentStrings(Component object){
        String output = "";
        
        
        Field fields[] = object.getParams();
        int count = 0;
        for(Field f : fields){
            try{
                String part = f.getName() + "=" + f.get(object);
                if(count < fields.length-1) part += ",";
                count++;
                output += part;
            }catch(IllegalAccessException e){
                e.printStackTrace(); //should never happen
            }
           
        }
        
        return output;
    }
    
}
