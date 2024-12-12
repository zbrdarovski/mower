package mower.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import mower.common.Mappers;
import mower.config.GameConfig;
import mower.ecs.component.DimensionComponent;
import mower.ecs.component.MovementComponent;
import mower.ecs.component.PositionComponent;
import mower.ecs.component.WorldWrapComponent;


public class WorldWrapSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(
            PositionComponent.class,
            DimensionComponent.class,
            MovementComponent.class,
            WorldWrapComponent.class
    ).get();

    public WorldWrapSystem() {
        super(FAMILY, 10);
    }

    // http://www.3dkingdoms.com/weekly/weekly.php?a=2
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);
        MovementComponent movement = Mappers.MOVEMENT.get(entity);

        // left/right
        if (position.x >= GameConfig.W_WIDTH - dimension.width) {
            position.x = GameConfig.W_WIDTH - dimension.width;
            movement.speed = movement.speed / 4;
            position.r = 180 - position.r;
        } else if (position.x < 0) {
            position.r = 180 - position.r;
            movement.speed = movement.speed / 4;
            position.x = 0;
        }

        // up/down
        if (position.y >= GameConfig.W_HEIGHT - dimension.height) {
            position.y = GameConfig.W_HEIGHT - dimension.height;
            position.r = 360 - position.r;
            movement.speed = movement.speed / 4;
        } else if (position.y < 0) {
            position.y = 0;
            position.r = 360 - position.r;
            movement.speed = movement.speed / 4;
        }
    }
}
