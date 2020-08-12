import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

public class Loader {
    private static Material textureAtlas;

    public Loader(AssetManager assetManager){
        Texture texture = assetManager.loadTexture("textureAtlas.png");
        texture.setMagFilter(Texture.MagFilter.Nearest);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);

        textureAtlas = mat;
    }

    public static Material getTextureAtlas(){
        return(textureAtlas);
    }
}
