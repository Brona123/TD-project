package fi.joutsijoki.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.AnimationBuilder;
import fi.joutsijoki.Constant;
import fi.joutsijoki.Effect;
import fi.joutsijoki.GameField;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.Node;
import fi.joutsijoki.screens.GameScreen;

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
    protected boolean initialRotationSet = false;
    protected GameScreen parent;

    public Enemy() {
        init();
    }

    public Enemy(Texture t, int health, float x, float y, int bounty) {
        init();
        this.texture = t;
        this.walkAnimation = AnimationBuilder.buildAnimation(t, 3, 1, 0.2f);
        this.walkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.maxHealth = health;
        this.currentHealth = maxHealth;
        this.position = new Vector2(x, y);
        this.bounty = bounty;
    }

    public Enemy(Texture t, int health, float x, float y, int bounty, GameScreen parent) {
        init();
        this.texture = t;
        this.walkAnimation = AnimationBuilder.buildAnimation(t, 3, 1, 0.2f);
        this.walkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.maxHealth = health;
        this.currentHealth = maxHealth;
        this.position = new Vector2(x, y);
        this.bounty = bounty;
        this.parent = parent;
    }

    private void init() {
        healthBar = new HealthBar();
        effects = new Array<Effect>();
    }

    public void damage(int damage) {
        this.currentHealth -= damage;

        if (this.currentHealth < 0) {
            this.alive = false;
            this.parent.increaseMoney(this.bounty);
            System.out.println("CURRENT MONEY: " + GameField.money);
        }
    }

    public void update() {
        // Travelled to next node
        if (this.delta >= 1f) {
            this.currentNode = this.nextNode;
            this.nextNode = this.path.get(nextNodeIndex);
            this.nextNodeIndex++;
            this.delta = 0f;

            rotateToNextNode();
        }

        // Tick all effects
        for (int i = 0; i < this.effects.size; i++) {
            this.effects.get(i).tick(this);
        }

        // Move the enemy according to speed
        move();

        // Enemy is at the end of the road
        if (this.nextNodeIndex >= this.path.getCount() && this.delta >= 1f) {
            this.atEnd = true;
        }

        if (!initialRotationSet) {
            rotateToNextNode();
            initialRotationSet = true;
        }
    }

    private void move() {
        this.delta += this.speed;
        Vector2 currentVec = new Vector2(this.currentNode.x, this.currentNode.y);
        Vector2 vec = currentVec.lerp(new Vector2(this.nextNode.x, this.nextNode.y), this.delta);
        this.position = vec;
    }

    private void rotateToNextNode() {
        double atan = Math.atan2(this.nextNode.y - this.currentNode.y, this.nextNode.x - this.currentNode.x);
        double angle = atan * (180 / Math.PI);
        this.setRotation((float)angle);
    }

    public void draw(SpriteBatch batch) {
        this.stateTime += Gdx.graphics.getDeltaTime();
        this.currentTexture = this.walkAnimation.getKeyFrame(this.stateTime, true);

        batch.draw(this.currentTexture
                , this.position.x
                , this.position.y
                , Constant.CELL_WIDTH / 2
                , Constant.CELL_HEIGHT / 2
                , Constant.CELL_WIDTH
                , Constant.CELL_HEIGHT
                , 1
                , 1
                , this.getRotation()
                , true);

        for (Effect e : this.effects) {
            e.draw(batch
                    , this.position.x
                    , this.position.y + Constant.CELL_HEIGHT
                    , Constant.CELL_WIDTH
                    , flip(Constant.CELL_HEIGHT));
        }

        this.healthBar.draw(batch, this.position, (float)this.currentHealth / (float)this.maxHealth);
    }

    public void setPath(GraphPathImpl path) {
        this.path = path;
        this.currentNode = path.get(0);
        this.nextNode = path.get(1);
        this.nextNodeIndex = 2;
    }

    public int flip(int toFlip) {
        return toFlip * -1;
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
