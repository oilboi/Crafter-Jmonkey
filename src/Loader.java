import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

public class Loader {
    public static Material loadMaterial(String textureName, AssetManager assetManager){
        Texture texture = assetManager.loadTexture(textureName);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        return(mat);
    }
}
