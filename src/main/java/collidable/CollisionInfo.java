package collidable;


import geometry.Point;

/**
 * The type CollisionInfo.
 *
 * @author avrhamBicha  <@address  example.com/>
 * @version 19.0.2
 * @since 2023 -04-23
 */
public class CollisionInfo {

    private final Point collisionPoint;
    private final Collidable objectInvolved;

    /**
     * Constructor.
     *
     * @param collisionPoint is the collision point.
     * @param objectInvolved is the object that the collision occurs with.
     */
    public CollisionInfo(Point collisionPoint, Collidable objectInvolved) {
        this.collisionPoint = collisionPoint;
        this.objectInvolved = objectInvolved;
    }

    /**
     * @return the point at which the collision occurs.
     */
    public Point collisionPoint() {
        return this.collisionPoint;
    }

    /**
     * @return the collidable object involved in the collision.
     */
    public Collidable collisionObject() {
        return this.objectInvolved;
    }


}
