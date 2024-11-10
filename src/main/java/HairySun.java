import circle.Collection;
import processing.core.PApplet;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class HairySun extends PApplet {

    private Collection c;

    public static void main(String[] args) {
        PApplet.main(HairySun.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        noFill();
        frameRate(-1);

        Collection.setPApplet(this);
        c = new Collection();
        for (int k = 0; k < NUMBER_OF_CIRCLES; k++) {
            c.add(Collection.generateCircle());
        }
    }

    @Override
    public void draw() {
        c.render();
        c.move();
        c.renew();

        if (frameCount >= NUMBER_OF_ITERATIONS) {
            noLoop();
            saveSketch(this);
        }
    }
}
