import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

public class ChunkMesh {
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static void /*Mesh*/ genChunkMesh(Chunk chunk){
        Mesh mesh = new Mesh();

        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        float[] meshContainter = new float[131072];

        //for (int i = )
        /*
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        vertices[2] = new Vector3f(0,3,0);
        vertices[3] = new Vector3f(3,3,0);

        Vector2f[] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);

        int [] indexes = { 2,0,1, 1,3,2 };



        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        mesh.setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();

        return mesh;
        */
    }
}
