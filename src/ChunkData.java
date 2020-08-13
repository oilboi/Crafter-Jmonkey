public class ChunkData {
    private static int renderDistance = Crafter.getRenderDistance();
    private static Chunk chunkArray[][] = new Chunk[(renderDistance*2)+1][(renderDistance*2)+1];

    private static int[] currentChunk = {0,0};

    public static void storeChunk(int x, int z, Chunk chunk){
        System.out.println();
        System.out.println("this is the data:");
        System.out.println("max:" + ((renderDistance*2)+1));
        System.out.println(x + renderDistance);

    }

}
