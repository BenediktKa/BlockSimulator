package teamnine.blocksim.block.physics;

import com.badlogic.gdx.math.Vector3;

public class BlockHitbox
{
	// HitBox Variables
	protected float posMinX, posMaxX;
	protected float posMinY, posMaxY;
	protected float posMinZ, posMaxZ;

	public BlockHitbox(Vector3 position)
	{

		// X-Coordinate
		posMinX = position.x;
		posMaxX = position.x + 1;

		// Y-Coordinate
		posMinY = position.y;
		posMaxY = position.y + 1;

		// Z-Coordinate
		posMinZ = position.z;
		posMaxZ = position.z + 1;
	}

	public boolean intersect(BlockHitbox hitbox)
	{
		return (posMinX < hitbox.posMaxX && posMaxX > hitbox.posMinX) && (posMinY < hitbox.posMaxY && posMaxY > hitbox.posMinY) && (posMinZ < hitbox.posMaxZ && posMaxZ > hitbox.posMinZ);
	}
}
