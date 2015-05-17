package com.test.hormigas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;

import java.util.Random;
import java.util.Vector;

public class PantallaHormiga implements Screen {

    private static final int HORMIGAS_POR_CLIC = 100;
    private Stage stage;
    private FillViewport viewport;

    private SpriteBatch stageBatch;

    private static Vector<MyActor> actores;
    private static int posActores = 0;

    public PantallaHormiga(HormigasGame game) {
        stageBatch = new SpriteBatch();
        viewport = new FillViewport(Assets.screenWidth, Assets.screenHeight);
        actores = new Vector<>();

        stage = new Stage(viewport, stageBatch);
        Gdx.input.setInputProcessor(stage);

        Image background = new Image(Assets.background);
        background.setSize(Assets.screenWidth, Assets.screenHeight);
        background.setScaling(Scaling.fill);

        stage.addActor(background);


        crearHormiga(2);

        crearPlanta(5);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);

        act(delta);
        draw();
    }


    public void act(float delta) {
        stage.act(delta);
        processInput();
        detectarColision();
    }

    public void draw() {
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    /**
     * ACCIONES EN PANTALLA
     */

    public void crearHormiga(int numero) {
        for (int i = 0; i < numero; i++) {
            Random ran = new Random();
            actores.insertElementAt(new Hormiga(ran.nextInt(5) + 1, ran.nextFloat() * (Assets.screenWidth - Planta.TAMANO), ran.nextFloat() * (Assets.screenHeight - Planta.TAMANO)), posActores);
            stage.addActor(actores.get(posActores));
            ((Hormiga) actores.get(posActores)).mover();
            posActores++;
        }
    }

    public void crearPlanta(int numero) {
        for (int i = 0; i < numero; i++) {
            Random ran = new Random();
            actores.insertElementAt(new Planta(ran.nextFloat() * (Assets.screenWidth - Planta.TAMANO), ran.nextFloat() * (Assets.screenHeight - Planta.TAMANO)), posActores);
            stage.addActor(actores.get(posActores));
            posActores++;
        }
    }

    public void detectarColision() {
        for (MyActor act1 : actores) {
            for (MyActor act2 : actores) {
                if (act1 != act2 && (act2.getPolygon().contains(act1.getPolygon().getX(), act1.getPolygon().getY())
                        || act2.getPolygon().contains(act1.getPolygon().getX() + act1.getTamano(), act1.getPolygon().getY())
                        || act2.getPolygon().contains(act1.getPolygon().getX() + act1.getTamano(), act1.getPolygon().getY() + act1.getTamano())
                        || act2.getPolygon().contains(act1.getPolygon().getX(), act1.getPolygon().getY() + act1.getTamano()))) {
                    choque(act1, act2);
                }
            }

        }
    }

    public void choque(final MyActor actor1, final MyActor actor2) {

        if (actor1 instanceof Hormiga && actor2 instanceof Hormiga)
            choqueEntreHormigas((Hormiga) actor1, (Hormiga) actor2);
        else if (actor1 instanceof Hormiga && actor2 instanceof Planta)
            choqueHormigaPlanta((Hormiga) actor1, (Planta) actor2);
        else if (actor1 instanceof Planta && actor2 instanceof Hormiga)
            choqueHormigaPlanta((Hormiga) actor2, (Planta) actor1);

    }

    private void choqueHormigaPlanta(Hormiga hormiga, Planta planta) {


        if (!hormiga.isChocada()) {
            hormiga.setChocada(true);
            hormiga.clearActions();
            hormiga.mover();
        }

    }

    private void choqueEntreHormigas(final Hormiga h1, final Hormiga h2) {


        if (!h1.isChocada()) {
            h1.setChocada(true);
            h1.clearActions();
            h1.mover();
        }

        if (!h2.isChocada()) {
            h2.setChocada(true);
            h2.clearActions();
            h2.mover();
        }
    }

    private void processInput() {

        // Se ejecuta cuando le das ESC (ordenador) y Atras (movil).
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            //TODO cerrar aplicaciones
        }

        Vector3 touchPoint = new Vector3();

        stage.getViewport().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.justTouched()) {

            //TODO añadir hormiga
            for (int i = 0; i < HORMIGAS_POR_CLIC; i++) {
                Random ran = new Random();
                actores.insertElementAt(new Hormiga(ran.nextInt(5) + 1, touchPoint.x, touchPoint.y), posActores);
                stage.addActor(actores.get(posActores));
                ((Hormiga) actores.get(posActores)).mover();
                posActores++;
            }
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    public static int getPosActores() {
        return posActores;
    }

    public static Vector<MyActor> getActores() {
        return actores;
    }
}
