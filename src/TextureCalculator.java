import com.jme3.math.FastMath;

import java.util.Arrays;

public class TextureCalculator {
    private static final byte sizeX = 10;
    private static final byte sizeY = 10;
    private static final byte pixel = 16;

    public static float[] calculateTextureMap(short id){
        id -= 1;
        byte y = (byte)FastMath.floor(id / sizeX);
        byte x = (byte)FastMath.floor(id - (sizeX * y));

//        System.out.println("--------------------------------");
//        System.out.println("ID: " + id + " Debug Pos Y: " + y);
//        System.out.println("ID: " + id + " Debug Pos X: " + x);

        float[] textureBounds = new float[4];
        //x
        textureBounds[0] = (float)x / (float)sizeX;
        textureBounds[1] = (float)(x+1) / (float)sizeX;
        //y
        textureBounds[2] = (float)y / (float)sizeY;
        textureBounds[3] = (float)(y+1) / (float)sizeY;

        return textureBounds;
    }
}
