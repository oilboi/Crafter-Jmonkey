import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

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

        //apply settings
        app.setSettings(appSettings);

        //begin app
        app.start();
    }


    @Override
    public void simpleInitApp(){

        flyCam.setMoveSpeed(100);
        Mesh mesh = new Mesh();

        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        vertices[2] = new Vector3f(0,3,0);
        vertices[3] = new Vector3f(3,3,0);

        Vector2f[] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);

        int [] indexes = { 2,0,1, 1,3,2 };

        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();

        Geometry geo = new Geometry("OurMesh", mesh); // using our custom mesh object
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geo.setMaterial(mat);
        rootNode.attachChild(geo);


        Chunk chunky = new Chunk();

        chunky.printChunk();



    }
}
