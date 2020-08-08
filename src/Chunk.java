import java.util.Arrays;

public class Chunk {
    //y x z - longest used for memory efficiency
    private static short chunkSizeX = 2;
    private static short chunkSizeY = 2;
    private static short chunkSizeZ = 2;

    private static short[] block    = new short[chunkSizeX * chunkSizeY * chunkSizeZ];
    private static byte[]  rotation = new byte[chunkSizeX * chunkSizeY * chunkSizeZ];

    public Chunk(){
        genRandom();
    }

    private static int hash(int x, int y, int z){
        return((x*chunkSizeY) + y + (z*(chunkSizeX * chunkSizeY)));
    }

    private static int[] getHash(int i) {
        int z = (int)(Math.floor(i/(chunkSizeX * chunkSizeY)));
        i %= (chunkSizeX * chunkSizeY);
        int x = (int)(Math.floor(i/chunkSizeY));
        i %= chunkSizeY;
        int y = (int)(Math.floor(i));
        int[] result = {x,y,z};
        return result;
    }

    public static void genRandom(){
        for (int y = 0; y < chunkSizeY; y++){
            for(int x = 0; x < chunkSizeX; x++) {
                for ( int z = 0; z < chunkSizeZ; z++) {

                    short newBlock = 1;//(short)(Math.random() * 30);
                    int hashedPos = hash(x,y,z);

                    //System.out.println(hashedPos);
                    System.out.println("NEW BLOCK");

                    int[] tempOutput = {x,y,z};
                    System.out.println(Arrays.toString(tempOutput));

                    int[] newHash = getHash(hashedPos);

                    System.out.println(Arrays.toString(newHash));

                    block[hashedPos] = newBlock;

                }
            }
        }
    }

    public static void printChunk(){
        for (int i = 0; i < block.length; i++){
            System.out.println(block[i]);
//            if(block[i] != 1){
//                System.out.printf("WARNING!");
//            }
        }
    }
}