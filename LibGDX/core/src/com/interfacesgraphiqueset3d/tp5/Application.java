package com.interfacesgraphiqueset3d.tp5;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.interfacesgraphiqueset3d.tp5.Lampe;

public class Application extends ApplicationAdapter {
    private FitViewport viewport;
    private PerspectiveCamera camera;
    private Pixmap pixels;
    private Texture textureWithPixels;
    private SpriteBatch spriteBatch;
    private Vector2 currentScreen;
    private Vector3 currentScene;
    private Vector3 tmpVector3;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<Sphere> scene;

    @Override
    public void create() {
        // Get screen dimensions, in pixels :
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        Vector3 centerSphere = new Vector3(0.0f, 0.0f, 60.0f);
        float radius = 3;

        Vector3 origin = new Vector3(0f, 0f, -10f);

        scene = new ArrayList<Sphere>();

        // Initialisation des objets lampe(position,color)
        //Lampe lampe1 = new Lampe(direction, radius, direction);
        Sphere sphere1 = new Sphere(centerSphere, radius);

        scene.add(sphere1);


        // Create a camera with perspective view :
        camera = new PerspectiveCamera(50.0f, screenWidth, screenHeight);
        camera.position.set(origin);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 500f;
        camera.update();

        
        //Distance intersection et lampe
        
        //


        // Create a viewport to convert coords of screen space into coords of scene
        // space.
        viewport = new FitViewport(screenWidth, screenHeight, camera);
        
        pixels = Pixmap.createFromFrameBuffer(0, 0, screenWidth, screenHeight);

        // Create an array of pixels, initialized with grey color :
        for (int y = 0; y < screenHeight; y++) {
            for (int x = 0; x < screenWidth; x++) {
                pixels.setColor(0.1f, 0.1f, 0.1f, 1f);
                pixels.drawPixel(x, y);
            }
        }

        // Add pixels in a Texture in order to render them :
        spriteBatch = new SpriteBatch();
        textureWithPixels = new Texture(pixels);

        // Initialize coords of the first pixel, in screen space :
        currentScreen = new Vector2(0, 0);

        // Others initializations :
        currentScene = new Vector3();
        tmpVector3 = new Vector3();
    }

    @Override
    public void render() {
        // If "ctrl + s" is pressed :
        if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.S)) {
            // Save the pixels into a png file :
            savePixelsInPngFile();
        }

        // If "escape" is pressed :
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            // Close th application :
            Gdx.app.exit();
        }

        // Reset the screen buffer colors :
        ScreenUtils.clear(0, 0, 0, 1);

        // Process pixels color :
        //processPixel();
        lancerRayon(screenWidth, screenHeight, scene);

        textureWithPixels.draw(pixels,0,0);

        // Render the texture with pixels :
        spriteBatch.begin();
        spriteBatch.draw(textureWithPixels, 0, 0);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureWithPixels.dispose();
        pixels.dispose();
    }

    /**
     * Compute the color of each screen pixels and store the results in the pixels
     * map.
     */
    private boolean processPixel() {
        /*boolean isOk = true;

        // Get color of current pixel :
        Vector3 color = getColor((int) currentScreen.x, (int) currentScreen.y);

        // Save color into pixels map :
        pixels.setColor(color.x, color.y, color.z, 1f);
        pixels.drawPixel((int) currentScreen.x, (int) currentScreen.y);*/

        return true;
    }

    /**
     * Write pixels in the png file "core/assets/render.png". If the file already
     * exists it will be overrided.
     */
    private boolean savePixelsInPngFile() {
        boolean isOk = true;

        // Create file :
        FileHandle file = Gdx.files.local("render.png");

        // Write pixels in file :
        PixmapIO.writePNG(file, pixels);

        return isOk;
    }

    /**
     * Return the color processed with path tracing and Phong method for the given pixel.
     */
    private Vector3 getColor(int xScreen, int yScreen, ArrayList<Sphere> scene)
    {
        Vector3 color = new Vector3(0f, 0f, 0f);
        
        Ray ray1 = new Ray();

        //Coord pixel referentiel grille de pixels
        Vector3 currentPixel = new Vector3(xScreen,yScreen,0);
        //Pos 3D pixel
        Vector3 currentScene = camera.unproject(currentPixel);
        Vector3 posPixel3D = new Vector3(currentScene.x, currentScene.y, 0);

        //Vecteur direction rayon
        Vector3 dir = (posPixel3D.sub(camera.position));

        //ray1.origin.set(camera.position);
        //ray1.direction.set(dir);
        ray1.set(camera.position, dir);
        for (int i=0; i<scene.size();i++){
            if(Intersector.intersectRaySphere(ray1, scene.get(i).center, scene.get(i).radius, null)){
                color = new Vector3(1f,0.0f,0.0f);
            }
        }

        return color;
    }

    /**
     * Lancer de rayon en parcourant l'ensemble des pixels.
     */
    private void lancerRayon(int screenWidth, int screenHeight,ArrayList<Sphere> scene)
    {
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                Vector3 color = getColor(x, y,scene);
                pixels.setColor(color.x, color.y, color.z, 1f);
                pixels.drawPixel(x, y);
            }
        }

    }
}