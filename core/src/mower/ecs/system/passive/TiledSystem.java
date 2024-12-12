package mower.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import mower.common.GameManager;
import mower.config.GameConfig;
import mower.ecs.component.BoundsComponent;
import mower.ecs.component.ObstacleComponent;
import mower.util.OrthogonalTiledMapRendererStopStartAnimated;

public class TiledSystem extends EntitySystem {

    public static float UNIT_SCALE = 1f;

    private final TiledMap tiledMap;

    private float tileWidth;        // width of one tile
    private float tileHeight;       // height of one tile
    private int widthInt;           // number of tiles in width
    private int heightInt;          // number of tiles in height
    private float widthMapInPx;     // width of the map in pixels
    private float heightMapInPx;    // height of the map in pixels

    private TiledMapTileLayer collideTileLayer;
    private MapLayer collideObjectsLayer;

    private Array<BoundsComponent> debug;

    private OrthogonalTiledMapRendererStopStartAnimated mapRenderer;

    public TiledSystem(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        setProcessing(false);   // passive

        if (GameConfig.debug) {
            debug = new Array<BoundsComponent>();
            for (int i = 0; i < 30; i++) debug.add(new BoundsComponent());
        }

        init();
    }

    private void init() {
        mapRenderer = new OrthogonalTiledMapRendererStopStartAnimated(tiledMap, UNIT_SCALE);
        mapRenderer.setAnimate(true);

        TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get("l_background");
        collideTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("l_grass");
        collideObjectsLayer = tiledMap.getLayers().get("l_objects");

        // int myCustom = collideObjectsLayer.getProperties().get("custom_key", 2, Integer.class);

        widthInt = tiledLayer.getWidth();
        heightInt = tiledLayer.getHeight();
        tileWidth = tiledLayer.getTileWidth();
        tileHeight = tiledLayer.getTileHeight();

        widthMapInPx = tileWidth * widthInt;
        heightMapInPx = tileHeight * heightInt;

        GameConfig.W_WIDTH = widthMapInPx;
        GameConfig.W_HEIGHT = heightMapInPx;
    }

    private void addObstacles(MapLayer layer, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
            bounds.rectangle.set(rectangle);

            ObstacleComponent obstacle = engine.createComponent(ObstacleComponent.class);

            Entity entity = engine.createEntity();
            entity.add(bounds);
            entity.add(obstacle);

            engine.addEntity(entity);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        addObstacles(collideObjectsLayer, engine);

        // add debug bounds
        if (GameConfig.debug) {
            for (BoundsComponent bounds : debug) {
                Entity entity = engine.createEntity();
                entity.add(bounds);

                engine.addEntity(entity);
            }
        }
    }

    /**
     * Explain camera frame!
     *
     * @param camera
     * @param x
     * @param y
     */
    public void renderTiledView(OrthographicCamera camera, float x, float y) {
        camera.position.x = MathUtils.clamp(x, camera.viewportWidth / 2, widthMapInPx - camera.viewportWidth / 2);
        camera.position.y = MathUtils.clamp(y, camera.viewportHeight / 2, heightMapInPx - camera.viewportHeight / 2);
        // camera.position.x = MathUtils.lerp(camera.position.x,MathUtils.clamp(x, camera.viewportWidth / 2, widthMapInPx - camera.viewportWidth / 2),1f);
        // camera.position.y = MathUtils.lerp(camera.position.y, MathUtils.clamp(y, camera.viewportHeight / 2, heightMapInPx - camera.viewportHeight / 2),1f);
        // if (Math.abs(x1-camera.position.x)>0.1)  Gdx.app.log("lerp ", x1+" "+(x1-camera.position.x));
        // if (Math.abs(y1-camera.position.y)>0.1)  Gdx.app.log("lerp y", y1+" "+(y1-camera.position.y));
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public boolean collideTile(float x, float y) {
        int ix = (int) (x / tileWidth);
        int iy = (int) (y / tileHeight);

        if (collideTileLayer.getCell(ix, iy) != null) {
            collideTileLayer.setCell(ix, iy, null);
            return true;
        }
        return false;
    }

    public boolean collideWith(Rectangle rectangle) {
        Rectangle tmp = new Rectangle();
        Rectangle tmpArea = new Rectangle();

        tmpArea.set(rectangle.x - tileWidth, rectangle.y - tileHeight,
                rectangle.width + tileWidth * 2, rectangle.height + tileHeight * 2);

        int i = 0;  // init tiled checked

        if (GameConfig.debug) { // area that will be searched
            for (BoundsComponent bc : debug) {
                bc.rectangle.set(0, 0, tileWidth, tileHeight);
            }
            debug.get(i++).rectangle.set(tmpArea);//show area
        }

        float dx = 0;
        float dy = 0;
        int ix;
        int iy = MathUtils.clamp((int) (tmpArea.y / tileHeight) + 1, 0, heightInt);
        boolean result = false;

        do {
            ix = MathUtils.clamp((int) (tmpArea.x / tileWidth), 0, widthInt);
            dx = 0;
            do {
                if (GameConfig.debug) {
                    tmp.set(ix * tileWidth, iy * tileHeight, tileWidth, tileHeight);
                    debug.get(i++).rectangle.set(tmp);
                }
                if (collideTileLayer.getCell(ix, iy) != null) {
                    tmp.set(ix * tileWidth, iy * tileHeight, tileWidth, tileHeight);
                    if (tmp.overlaps(rectangle)) {
                        collideTileLayer.setCell(ix, iy, null);
                        GameManager.INSTANCE.incResult();
                        result = true;
                    }
                }
                ix++;
                dx += tileWidth;
            } while (dx < tmpArea.width);
            iy++;
            dy += tileHeight;
        } while (dy < tmpArea.height);

        return result;
    }
}
