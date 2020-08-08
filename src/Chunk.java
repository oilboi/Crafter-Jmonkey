import java.util.Arrays;

public class Chunk {
    private static int sizeX = 16;
    private static int sizeY = 128;
    private static int sizeZ = 16;
    //array size
    private int[] blocks = new int[128*16*16];

    public Chunk( int chunkX, int chunkZ ){
        createChunk(chunkX, chunkZ);
    }

    private void createChunk( int chunkX, int chunkZ ){
        int x = 0;
        int y = 0;
        int z = 0;
        for (int i = 0; i < (128*sizeX*16); i++){
            blocks[i] = 1;
            y++;
            if( y > sizeY - 1 ) {
                y = 0;
                x++;
                if (x > sizeX - 1) {
                    x = 0;
                    z++;
                }
            }
        }
        System.out.println(Arrays.toString(blocks));
    }
}
