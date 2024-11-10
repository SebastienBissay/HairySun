package circle;

import processing.core.PVector;

import static parameters.Parameters.HEIGHT;
import static parameters.Parameters.WIDTH;

public class Circle {
    private final float radius;
    private PVector position;
    private PVector speed;

    public Circle(PVector position, PVector speed, float radius) {
        this.position = position;
        this.speed = speed;
        this.radius = radius;
    }

    public void move() {
        position.add(speed);
    }

    public boolean isOut() {
        return (position.x < 0 || position.x > WIDTH || position.y < 0 || position.y > HEIGHT);
    }

    public PVector getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }
}
