package circle;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static parameters.Parameters.*;
import static processing.core.PApplet.*;

public class Collection {
    private static PApplet pApplet;

    private final int nX;
    private final int nY;
    private final List<Circle> circles;
    private final List<List<Circle>> cells;

    public Collection() {
        circles = new ArrayList<>();
        cells = new ArrayList<>();
        nX = ceil(WIDTH / MAXIMUM_SIZE);
        nY = ceil(HEIGHT / MAXIMUM_SIZE);
        for (int i = 0; i < nX * nY; i++) {
            cells.add(new ArrayList<>());
        }
    }

    private static void trace(PVector A, PVector B) {
        float d = PVector.sub(A, B).mag();
        for (int i = 0; i < d; i++) {
            float t = pApplet.random(1);
            pApplet.point(lerp(A.x, B.x, t), lerp(A.y, B.y, t));
        }
    }

    public static void setPApplet(PApplet pApplet) {
        Collection.pApplet = pApplet;
    }

    public static Circle generateCircle() {
        return new Circle(PVector.random2D(pApplet).mult(CIRCLE_INITIAL_RADIUS).add(WIDTH / 2f, HEIGHT / 2f),
                PVector.random2D(pApplet).mult(pApplet.random(CIRCLE_MINIMUM_SPEED, CIRCLE_MAXIMUM_SPEED)),
                pApplet.random(MINIMUM_SIZE, MAXIMUM_SIZE));
    }

    public void clearCells() {
        for (List<Circle> cell : cells) {
            cell.clear();
        }
    }

    public void add(Circle c) {
        circles.add(c);
        int i = constrain(floor(c.getPosition().x / MAXIMUM_SIZE), 0, nX - 1);
        int j = constrain(floor(c.getPosition().y / MAXIMUM_SIZE), 0, nY - 1);
        cells.get(i + nX * j).add(c);
    }

    public void remove(Circle c) {
        circles.remove(c);
    }

    private ArrayList<Circle> getIntersectingCircles(Circle c) {
        ArrayList<Circle> intersectingCircles = new ArrayList<>();
        for (int i = max(0, floor(c.getPosition().x / MAXIMUM_SIZE) - 2);
             i < min(nX - 1, floor(c.getPosition().x / MAXIMUM_SIZE) + 3);
             i++) {
            for (int j = max(0, floor(c.getPosition().y / MAXIMUM_SIZE) - 2);
                 j < min(nY - 1, floor(c.getPosition().y / MAXIMUM_SIZE) + 3);
                 j++) {
                cells.get(i + nX * j).stream()
                        .filter(d -> c != d)
                        .filter(d -> sq(c.getPosition().x - d.getPosition().x)
                                + sq(c.getPosition().y - d.getPosition().y)
                                < sq(c.getRadius() + d.getRadius()))
                        .forEach(intersectingCircles::add);
            }
        }

        return intersectingCircles;
    }

    public void move() {
        circles.forEach(Circle::move);
    }

    public void render() {
        clearCells();
        circles.forEach(
                c -> cells.get(constrain(floor(c.getPosition().x / MAXIMUM_SIZE), 0, nX - 1)
                                + nX * constrain(floor(c.getPosition().y / MAXIMUM_SIZE), 0, nY - 1))
                        .add(c));
        circles.forEach(c -> getIntersectingCircles(c).forEach(d -> trace(c.getPosition(), d.getPosition())));
    }

    public void renew() {
        for (int i = NUMBER_OF_CIRCLES - 1; i >= 0; i--) {
            if (circles.get(i).isOut()) {
                circles.remove(i);
                add(generateCircle());
            }
        }
    }
}
