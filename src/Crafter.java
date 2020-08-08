import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;

import java.awt.*;

public class Crafter extends SimpleApplication {

    public static void main(String[] args) {
        //self app creation
        Crafter app = new Crafter();

        //disable annoying jmonkey settings menu
        app.showSettings = false;
        AppSettings appSettings = new AppSettings(true);
        appSettings.setResizable(true);

        //window sizing
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        appSettings.setWidth(d.width/2);
        appSettings.setHeight(d.height/2);

        //window title
        appSettings.setTitle("Crafter");

        //apply settings
        app.setSettings(appSettings);


        //begin app
        app.start();
    }


    @Override
    public void simpleInitApp(){

        flyCam.setMoveSpeed(100);

        Chunk chunky = new Chunk();

        Geometry geo = ChunkMesh.genChunkMesh(chunky, assetManager);


        rootNode.attachChild(geo);

    }
}
