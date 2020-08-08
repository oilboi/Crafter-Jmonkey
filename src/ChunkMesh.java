import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.ArrayList;
import java.util.Collection;

public class ChunkMesh extends Mesh{
    private static short chunkSizeX = 16;
    private static short chunkSizeY = 128;
    private static short chunkSizeZ = 16;

    public static Geometry genChunkMesh(Chunk chunk, AssetManager assetManager){
        //set up data
        int x = 0;
        int y = 0;
        int z = 0;

        Collection<Geometry> test = new ArrayList();

        for (int i = 0; i < 5; i++) {
            ChunkMesh mesh = new ChunkMesh();

            Vector3f[] vertices = new Vector3f[4];
            vertices[0] = new Vector3f(0+(i*3), 0, 0);
            vertices[1] = new Vector3f(3+(i*3), 0, 0);
            vertices[2] = new Vector3f(0+(i*3), 3, 0);
            vertices[3] = new Vector3f(3+(i*3), 3, 0);

            Vector2f[] texCoord = new Vector2f[4];
            texCoord[0] = new Vector2f(0, 0);
            texCoord[1] = new Vector2f(1, 0);
            texCoord[2] = new Vector2f(0, 1);
            texCoord[3] = new Vector2f(1, 1);

            int[] indexes = {2, 0, 1, 1, 3, 2};

            mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
            mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));
            mesh.updateBound();

            Geometry geo = new Geometry("test", mesh);

            Material mat = new Material(assetManager,
                    "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Blue);
            geo.setMaterial(mat);

            test.add(geo);
        }

        Mesh chunky = new Mesh();

        GeometryBatchFactory.mergeGeometries(test,chunky);

        Geometry geo = new Geometry("test", chunky);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geo.setMaterial(mat);

        return geo;
    }
}
