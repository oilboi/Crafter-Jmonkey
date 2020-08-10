import com.jme3.math.FastMath;

public class ChunkMath {
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static int genHash(int x, int y, int z){
        return((x*chunkSizeY) + y + (z*(chunkSizeX * chunkSizeY)));
    }

    public static int[] getHash(int i) {
        int z = (int)(FastMath.floor(i/(chunkSizeX * chunkSizeY)));
        i %= (chunkSizeX * chunkSizeY);
        int x = (int)(FastMath.floor(i/chunkSizeY));
        i %= chunkSizeY;
        int y = (int)(FastMath.floor(i));
        int[] result = {x,y,z};
        return result;
    }
}
