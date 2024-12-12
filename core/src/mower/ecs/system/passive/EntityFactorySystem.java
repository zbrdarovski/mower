package mower.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import mower.assets.AssetDescriptors;
import mower.assets.RegionNames;
import mower.config.GameConfig;
import mower.ecs.component.BoundsComponent;
import mower.ecs.component.DimensionComponent;
import mower.ecs.component.MovementComponent;
import mower.ecs.component.MowerComponent;
import mower.ecs.component.PositionComponent;
import mower.ecs.component.TextureComponent;
import mower.ecs.component.WorldWrapComponent;
import mower.ecs.component.ZOrderComponent;

public class EntityFactorySystem extends EntitySystem {

    private static final int MOWER_Z_ORDER = 1;

    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public EntityFactorySystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false);   // passive
        init();
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
    }

    public void createMower() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = (GameConfig.W_WIDTH - GameConfig.MOWER_HEIGHT) / 2;
        position.r = 90;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.MOWER_WIDTH;
        dimension.height = GameConfig.MOWER_HEIGHT;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        MovementComponent movement = engine.createComponent(MovementComponent.class);

        MowerComponent mowerComponent = engine.createComponent(MowerComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.MOWER);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = MOWER_Z_ORDER;

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movement);
        entity.add(mowerComponent);
        entity.add(worldWrap);
        entity.add(texture);
        entity.add(zOrder);

        engine.addEntity(entity);
    }
}
