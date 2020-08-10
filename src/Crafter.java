import com.jme3.app.SimpleApplication;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

public class Crafter extends SimpleApplication {
    //the working directory of the game
    public final static String DIRECTORY = System.getProperty("user.dir");

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

        //set window image
        BufferedImage icon = ImageIO.read(new File(DIRECTORY + "\\texture\\icon.png"));
        appSettings.setIcons(new BufferedImage[]{icon});

        System.out.println("Working Directory = " + DIRECTORY);

        //apply settings
        app.setSettings(appSettings);

        //begin app
        app.start();
    }


    @Override
    public void simpleInitApp(){

        flyCam.setMoveSpeed(100);

    }

    private int count = 0;
    @Override
    public void simpleUpdate(float tpf){

//        if (count < 20) {
//            count++;
//            Chunk chunky = new Chunk();
//
//            Geometry geo = ChunkMesh.genChunkMesh(chunky, assetManager);
//
//            rootNode.attachChild(geo);
//        }

//        Display.setLocation((int)(Math.random()*1000), (int)(Math.random() * 1000));
//
//        byte[] array = new byte[30];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
//
//        Display.setTitle(generatedString);
    }


}
