package mower.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import mower.common.Mappers;
import mower.ecs.component.MovementComponent;
import mower.ecs.component.PositionComponent;

public class MovementSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            MovementComponent.class
    ).get();

    public MovementSystem() {
        super(FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        MovementComponent movement = Mappers.MOVEMENT.get(entity);

        // Gdx.app.log("1",position.toString()+" " +  movement.toString());
        position.r += movement.rSpeed;
        if (position.r < 0) position.r = position.r + 360;
        if (position.r > 360) position.r = position.r - 360;

        float tmpR = position.r;
        position.x += MathUtils.cosDeg(tmpR) * movement.speed * 10;
        position.y += MathUtils.sinDeg(tmpR) * movement.speed * 10;

        // MathUtils.atan2(0.1f, 2.1f);
        // Gdx.app.log("2", tmpR + "," + position.toString());

        // position.r += movement.rSpeed;
    }
}
