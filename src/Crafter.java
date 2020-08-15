import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Crafter extends SimpleApplication {


    public static void main(String[] args) throws IOException {
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

        app.setDisplayFps(true);

        //set window image
        BufferedImage icon = ImageIO.read(new File(System.getProperty("user.dir") + "\\texture\\icon.png"));
        appSettings.setIcons(new BufferedImage[]{icon});

        //apply settings
        app.setSettings(appSettings);

        //begin app
        app.start();
    }

    private void initKeys() {
        Inputs.initializeKeys(inputManager,actionListener,analogListener);
    }

    private BitmapText playerPosText;
    @Override
    public void simpleInitApp() {
        initKeys();
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(0);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        cam.setFrustumPerspective(72,d.height/d.width, 0.01f, 1000f );

        cam.setLocation(new Vector3f(0,128,128));


        this.assetManager.registerLocator("texture/", FileLocator.class); // default

        rootNode.setCullHint(Spatial.CullHint.Never);

        //used statically. Object initialized here for loading textures
        Loader textureLoader = new Loader(assetManager);

        guiFont = assetManager.loadFont("pixel.fnt");
        playerPosText = new BitmapText(guiFont, false);
        playerPosText.setSize(guiFont.getCharSet().getRenderedSize());
        playerPosText.setName("pos");

        Sphere collisionImpactMesh = new Sphere(32, 32, 0.3f, false, false);
        Geometry geom = new Geometry("A shape", collisionImpactMesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/ShowNormals.j3md");
        geom.setMaterial(mat);
        // if you want, transform (move, rotate, scale) the geometry.
        geom.setLocalTranslation(0,55,0);
        geom.setName("collision");
        rootNode.attachChild(geom);
    }

    private static int renderDistance = 2;

    private int x = -renderDistance;
    private int z = -renderDistance;
    private int counter = 0;
    private boolean genned = false;

    Chunk chunk;

    public static int getRenderDistance() {
        return renderDistance;
    }



    @Override
    public void simpleUpdate(float tpf){

        GameCamera.handleCamera(cam);

        if (genned) {
            Player.playerOnTick(tpf, rootNode);
        }

        guiNode.detachChildNamed("pos");
        playerPosText.setText("X: " + Player.getPos().x + "\nY: " + Player.getPos().y + "\nZ: " + Player.getPos().z);
        playerPosText.setLocalTranslation(10, cam.getHeight(), 0);
        playerPosText.setSize(24f);
        guiNode.attachChild(playerPosText);
        //this is for warming up vm then gen 1 chunk
//        if (counter > 5 && !genned) {
//            long startTime = System.currentTimeMillis();
//            chunk = new Chunk(x,z);
//            ChunkData.storeChunk(x,z, chunk);
//            ChunkMesh.genChunkMesh(chunk, assetManager, x, z, rootNode,false);
//
//            genned = true;
//            long endTime = System.currentTimeMillis();
//            double timeElapsed = (double)(endTime - startTime)/1000;
//            System.out.println("Chunk init time: " + timeElapsed + " seconds");
//        } else if (!genned){
//            System.out.println(counter);
//            counter++;
//        }

        //this is for dynamic chunk generation
        //counter++;
        if (z <= renderDistance) {
            long startTime = System.currentTimeMillis();

            chunk = new Chunk(x,z);
            ChunkData.storeChunk(x,z, chunk);
            ChunkMesh.genChunkMesh(chunk, assetManager, x, z, rootNode,false);

            long endTime = System.currentTimeMillis();
            double timeElapsed = (double) (endTime - startTime) / 1000;
            System.out.println("Chunk init time: " + timeElapsed + " seconds");


            x++;
            if (x > renderDistance) {
                x = -renderDistance;
                z++;
                if (z > renderDistance){
                    genned = true;
                }
            }
        }

    }


    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
        }
    };

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            Inputs.handleKeys(cam, tpf, name, value);
        }
    };


}
