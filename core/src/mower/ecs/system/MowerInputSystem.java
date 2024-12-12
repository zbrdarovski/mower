package mower.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

import mower.common.Mappers;
import mower.config.GameConfig;
import mower.ecs.component.MovementComponent;
import mower.ecs.component.MowerComponent;


public class MowerInputSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            MowerComponent.class,
            MovementComponent.class
    ).get();

    public MowerInputSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = Mappers.MOVEMENT.get(entity);

        movement.rSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.rSpeed = MathUtils.clamp(movement.rSpeed + GameConfig.MAX_MOWER_R_SPEED * deltaTime, -GameConfig.MAX_MOWER_R_SPEED, GameConfig.MAX_MOWER_R_SPEED);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.rSpeed = MathUtils.clamp(movement.rSpeed - GameConfig.MAX_MOWER_R_SPEED * deltaTime, -GameConfig.MAX_MOWER_R_SPEED, GameConfig.MAX_MOWER_R_SPEED);
        } else {
            //movement.rSpeed -=movement.rSpeed*deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.speed = MathUtils.clamp(movement.speed + GameConfig.MAX_MOWER_SPEED * deltaTime, 0, GameConfig.MAX_MOWER_SPEED);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.speed = MathUtils.clamp(movement.speed - GameConfig.MAX_MOWER_SPEED * deltaTime, 0, GameConfig.MAX_MOWER_SPEED);
        } else { //friction slows down item
            movement.speed -= movement.speed * deltaTime;
        }

    }
}
