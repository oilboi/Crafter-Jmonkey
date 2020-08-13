import com.jme3.math.FastMath;

import java.util.Arrays;

public class Chunk {
    //y x z - longest used for memory efficiency
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;
    private static int renderDistance = Crafter.getRenderDistance();

    private short[] block    = new short[chunkSizeX * chunkSizeY * chunkSizeZ];
    private byte[]  rotation = new byte[chunkSizeX * chunkSizeY * chunkSizeZ];

    public Chunk(int chunkX,int chunkZ){
//        if (Math.random() > 0.5) {
//            genRandom(); //this is for performance testing and uses A LOT of memory
//        } else {
//        genDebug();
//        }
//        genDebug();
//        genRandom();
        genBiome(chunkX,chunkZ);
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
            block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomInt(0,5));
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

    private FastNoise noisey = new FastNoise();

    private int heightAdder = 40;
    private byte dirtHeight = 4;
    //a basic biome test for terrain generation
    public void genBiome(int chunkX, int chunkZ){

        int x = 0;
        int y = 0;
        int z = 0;

        FastNoise noise = new FastNoise();

        byte height = (byte)(FastMath.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);

        //System.out.println(height);
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            //block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomInt(0,5));


//            if (y <= height) {
//                block[ChunkMath.genHash(x, y, z)] = (short)(FastMath.nextRandomInt(1,5));
//            } else {
//                block[ChunkMath.genHash(x, y, z)] = 0;
//            }
            if (y == height) {
                block[ChunkMath.genHash(x, y, z)] = 5;
            } else if (y < height && y >= height - dirtHeight){
                block[ChunkMath.genHash(x, y, z)] = 1;
            } else if (y < height - dirtHeight){
                block[ChunkMath.genHash(x, y, z)] = 2;
            }

            y++;
            if( y > chunkSizeY - 1){
                y = 0;
                x++;
                height = (byte)(FastMath.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);

                if( x > chunkSizeX - 1 ){
                    x = 0;
                    height = (byte)(FastMath.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                    z++;
                }
            }
        }
    }

    //this is full of magic numbers TODO: turn this into functions in ChunkMath
    //+ render distance is getting it to base count 0
    public static short getBlock(int x,int y,int z, int chunkX, int chunkZ){
        chunkX += renderDistance;
        chunkZ += renderDistance;
        //neighbor checking
        if(x < 0) {
            if (chunkX - 1 >= 0) {
                return ChunkData.getBlock(x+16,y,z,chunkX-1,chunkZ);
            }
            return 0;
        } else if (x >= chunkSizeX) {
            if ( chunkX + 1 <= renderDistance*2){
                return ChunkData.getBlock(x-16,y,z,chunkX+1,chunkZ);
            }
            return 0;

        } else if (y < 0 || y >= chunkSizeY) { //Y is caught regardless in the else clause if in bounds
            return 0;

        } else if (z < 0) {
            if (chunkZ - 1 >= 0) {
                return ChunkData.getBlock(x,y,z+16,chunkX,chunkZ-1);
            }
            return 0;

        } else if (z >= chunkSizeZ) {
            if (chunkZ + 1 <= renderDistance*2){
                return ChunkData.getBlock(x,y,z-16,chunkX,chunkZ+1);
            }
            return 0;

        }
        //self chunk checking
         else {
            return ChunkData.getBlock(x,y,z,chunkX,chunkZ);
        }
    }

    //debug testing for now
    public void genDebug(){
        int x = 0;
        int y = 0;
        int z = 0;

        short counter = 0;
        for ( int i = 0; i < (chunkSizeX * chunkSizeY * chunkSizeZ); i++){
            short newBlock = (short)(FastMath.nextRandomInt(1,5));
//            counter++;
//            if (counter > 19){
//                counter = 0;
//            }

//            System.out.println(newBlock);


            //TextureCalculator.calculateTextureMap(newBlock);

            int hashedPos = ChunkMath.genHash(x, y, z);

            //TODO: these are marked TODO because it shows up in yellow in my IDE

//            System.out.println("NEW BLOCK"); //TODO

//            System.out.println(hashedPos); //TODO


//            int[] tempOutput = {x, y, z};

//            System.out.println(Arrays.toString(tempOutput)); //TODO

//            int[] newHash = ChunkMath.getHash(hashedPos);

//            System.out.println(Arrays.toString(newHash)); //TODO

            block[hashedPos] = newBlock;

//            System.out.println("--------");

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