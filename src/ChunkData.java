import java.util.Arrays;

public class ChunkData {
    private static int renderDistance = Crafter.getRenderDistance();
    private static Chunk chunkArray[][] = new Chunk[(renderDistance*2)+1][(renderDistance*2)+1];

    private static int[] currentChunk = {0,0};

    public static void storeChunk(int x, int z, Chunk chunk){
//        System.out.println();
//        System.out.println("this is the data:");
//        System.out.println("max:" + ((renderDistance*2)+1));
//        System.out.println(x + renderDistance);

        chunkArray[x + renderDistance][z + renderDistance] = chunk;

//        System.out.println(Arrays.deepToString(chunkArray));
    }

    public static Chunk getChunkData(int x, int z){
        Chunk test = chunkArray[x + renderDistance][z + renderDistance];

        if (test != null){
            return test;
        }
        return null;
    }

    //TODO the other function needs to be removed or reworked or moved to this class maybe
    public static short getBlock(int x, int y, int z, int chunkX, int chunkZ){
        //System.out.println(x + " " + y + " " + z + " " + chunkX + " " + chunkZ);
        //System.out.println(Arrays.deepToString(chunkArray));

        Chunk piece = chunkArray[chunkX][chunkZ];

        if (piece == null){
            return 0;
        }

        //System.out.println("x " + x + " | y: " + y + " | z:" + z);
        int hashy = ChunkMath.genHash(x, y, z);

        //System.out.println();
        short[] blocksYo = piece.getBlocks();

        return blocksYo[hashy];
    }

    public static boolean chunkExists(int chunkX, int chunkZ){

        //safety checks
        if(chunkArray.length < chunkX-1 || chunkX < 0){
            return false;
        }
        if(chunkArray.length < chunkZ-1 || chunkZ < 0){
            return false;
        }
        //check if chunk exists
        if ( chunkArray[chunkX][chunkZ] == null){
            return false;
        }
        //all checks pass
        return true;
    }
}
