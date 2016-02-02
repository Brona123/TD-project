package fi.joutsijoki.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.Effect;
import fi.joutsijoki.Labyrinth;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.Node;

/**
 * Created by Sami on 21.1.2016.
 */
public class Enemy extends Sprite {
    protected Texture texture;
    protected Vector2 position;
    protected GraphPathImpl path;
    protected Node nextNode;
    protected Node currentNode;
    protected int nextNodeIndex;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean alive = true;
    protected boolean atEnd;
    protected float delta = 0f;
    protected float speed = 0.03f;
    protected HealthBar healthBar;
    protected Array<Effect> effects;
    protected Animation walkAnimation;
    protected TextureRegion currentTexture;
    protected float stateTime;
    protected int bounty;

    public Enemy() {
        healthBar = new HealthBar();
        effects = new Array<Effect>();
    }

    public void damage(int damage) {
        this.currentHealth -= damage;

        if (this.currentHealth < 0) {
            this.alive = false;
            Labyrinth.money += this.bounty;
            System.out.println("CURRENT MONEY: " + Labyrinth.money);

        }
    }

    public void update() {

        if (this.nextNodeIndex >= this.path.getCount()) {
            this.atEnd = true;
            return;
        }

        if (this.isAtNode(this.nextNode)) {

            this.currentNode = this.nextNode;
            this.nextNode = this.path.get(nextNodeIndex);
            this.nextNodeIndex++;
            this.delta = 0f;

            double atan = Math.atan2(this.nextNode.y - this.currentNode.y, this.nextNode.x - this.currentNode.x);
            double angle = atan * (180 / Math.PI);
            this.setRotation((float)angle);
        }

        for (int i = 0; i < this.effects.size; i++) {
            this.effects.get(i).tick(this);
        }

        this.delta += this.speed;
        Vector2 currentVec = new Vector2(this.currentNode.x, this.currentNode.y);
        Vector2 vec = currentVec.lerp(new Vector2(this.nextNode.x, this.nextNode.y), this.delta);
        this.position = vec;
    }

    public void draw(SpriteBatch batch) {
        this.stateTime += Gdx.graphics.getDeltaTime();
        this.currentTexture = this.walkAnimation.getKeyFrame(this.stateTime, true);

        batch.draw(this.currentTexture
                , this.position.x
                , this.position.y
                , Labyrinth.CELL_WIDTH / 2
                , Labyrinth.CELL_HEIGHT / 2
                , Labyrinth.CELL_WIDTH
                , Labyrinth.CELL_HEIGHT
                , 1
                , 1
                , this.getRotation()
                , true);

        for (Effect e : this.effects) {
            e.draw(batch
                    , this.position.x
                    , this.position.y + Labyrinth.CELL_HEIGHT
                    , Labyrinth.CELL_WIDTH
                    , flip(Labyrinth.CELL_HEIGHT));
        }

        this.healthBar.draw(batch, this.position, (float)this.currentHealth / (float)this.maxHealth);
    }

    public int flip(int toFlip) {
        return toFlip * -1;
    }

    public boolean isAtNode(Node n) {
        if (n.x + 0.5f >= position.x
                && n.x - 0.5f <= position.x
                && n.y + 0.5f >= position.y
                && n.y - 0.5f <= position.y) {
            return true;
        } else {
            return false;
        }
    }

    public void setEffect(Effect.TYPE type) {
        this.effects.add(new Effect(type));
    }

    public boolean isAtEnd() {
        return this.atEnd;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Vector2 getPos() {
        return this.position;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getBounty() {
        return this.bounty;
    }
}
