public class ChunkMath {
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static int genHash(int x, int y, int z){
        if( x < 0 || x > chunkSizeX-1  || y < 0 || y > chunkSizeY-1 || z < 0 || y > chunkSizeZ-1){
            return 0;
        }
        return((x*chunkSizeY) + y + (z*(chunkSizeX * chunkSizeY)));
    }

    public static int[] getHash(int i) {
        int z = (int)(Math.floor(i/(chunkSizeX * chunkSizeY)));
        i %= (chunkSizeX * chunkSizeY);
        int x = (int)(Math.floor(i/chunkSizeY));
        i %= chunkSizeY;
        int y = (int)(Math.floor(i));
        int[] result = {x,y,z};
        return result;
    }

}
