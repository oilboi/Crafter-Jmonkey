import com.jme3.math.FastMath;

import java.util.Arrays;

public class Chunk {
    //y x z - longest used for memory efficiency
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    private short[] block    = new short[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[]  rotation = new byte[chunkSizeX * chunkSizeY * chunkSizeZ];

    public Chunk(){
        genRandom();
        //genDebug();
    }

    public short[] getBlocks(){
        return block;
    }

    //randomly assign block ids
    public void genRandom(){
        int x = 0;
        int y = 0;
        int z = 0;
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomFloat() * 2);
            y++;
            if( y > chunkSizeY - 1){
                y = 0;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
        }
    }

    public int getBlock(int x,int y,int z){
        //TODO: attempt to return neighboring block if out of bounds of x or z
        if( x < 0 || x >= chunkSizeX || y < 0 || y >= chunkSizeY || z < 0 || z >= chunkSizeZ){
            return 0;
        } else {
            int hashy = ChunkMath.genHash(x, y, z);
            return block[hashy];
        }
    }

    //debug testing for now
    public void genDebug(){
        int x = 0;
        int y = 0;
        int z = 0;

        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            short newBlock = 1;

            int hashedPos = ChunkMath.genHash(x, y, z);

            //TODO: these are marked TODO because it shows up in yellow in my IDE

            System.out.println("NEW BLOCK"); //TODO

            System.out.println(hashedPos); //TODO


            int[] tempOutput = {x, y, z};

            System.out.println(Arrays.toString(tempOutput)); //TODO

            int[] newHash = ChunkMath.getHash(hashedPos);

            System.out.println(Arrays.toString(newHash)); //TODO

            block[hashedPos] = newBlock;

            System.out.println("--------");

            y++;
            if( y > chunkSizeY - 1){
                y = 0;
                x++;
                if( x > chunkSizeX - 1 ){
                    x = 0;
                    z++;
                }
            }
        }
    }

    public void printChunk(){
        for (int i = 0; i < block.length; i++){
//            System.out.println(block[i]);
//            if(block[i] != 1){ //this is debug
//                System.out.printf("WARNING!");
//            }
        }
    }
}