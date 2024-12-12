package mower.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;

import mower.common.GameManager;
import mower.common.Mappers;
import mower.ecs.component.BoundsComponent;
import mower.ecs.component.MowerComponent;
import mower.ecs.component.ObstacleComponent;
import mower.ecs.system.passive.SoundSystem;
import mower.ecs.system.passive.TiledSystem;

public class CollisionSystem extends EntitySystem {

    private static final Family FAMILY_MOWER = Family.all(MowerComponent.class, BoundsComponent.class).get();
    private static final Family FAMILY_OBSTACLE = Family.all(ObstacleComponent.class, BoundsComponent.class).get();

    private SoundSystem soundSystem;
    private TiledSystem tiledSystem;

    @Override
    public void addedToEngine(Engine engine) {
        soundSystem = engine.getSystem(SoundSystem.class);
        tiledSystem = engine.getSystem(TiledSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.isGameOver()) return;

        ImmutableArray<Entity> mowers = getEngine().getEntitiesFor(FAMILY_MOWER);
        ImmutableArray<Entity> obstacles = getEngine().getEntitiesFor(FAMILY_OBSTACLE);

        for (Entity mower : mowers) {
            // pick; collision with tile
            BoundsComponent boundsMower = Mappers.BOUNDS.get(mower);
            if (tiledSystem.collideWith(boundsMower.rectangle)) {
                soundSystem.pick();
            }

            for (Entity obstacle : obstacles) {
                ObstacleComponent obstacleComponent = Mappers.OBSTACLE.get(obstacle);
                if (obstacleComponent.hit) {
                    continue;
                }

                BoundsComponent boundsObstacle = Mappers.BOUNDS.get(obstacle);
                if (Intersector.overlaps(boundsMower.rectangle, boundsObstacle.rectangle)) {
                    // obstacleComponent.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.obstacle();
                }
            }
        }
    }
}
