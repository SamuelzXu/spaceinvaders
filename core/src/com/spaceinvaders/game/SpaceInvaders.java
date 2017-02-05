package com.spaceinvaders.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SpaceInvaders extends ApplicationAdapter implements InputProcessor{
    private String spritePos = "A";
    boolean playerAlive=true;
    private int playerX, playerY=20, ct=0,timeout=40, maxX=555, minX=5,
            dir=5, score=0, shotTimer=0, laserCooldown=0, soundTick=1;

    SpriteBatch batch;
    HashMap<String,Texture> sprites = new HashMap<String,Texture>();
    HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    ArrayList<Laser> playerLasers = new ArrayList<Laser>();
    ArrayList<Laser> alienLasers = new ArrayList<Laser>();
    ArrayList<Alien> aliens = new ArrayList<Alien>();
    ArrayList<Wall> walls = new ArrayList<Wall>();
    Rectangle endRect = new Rectangle(), playerRect = new Rectangle();


    @Override
    public void create () {
        batch = new SpriteBatch();
        endRect.set(0,-1,600,1);
//        FreeTypeFontGenerator font = new FreeTypeFontGenerator(Gdx.files.internal("space_invaders.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size=15;
//        gameFont = font.generateFont(parameter);
        loadSprites();
        loadAliens();
        loadPlayer();
        loadWalls();
        loadAudio();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        takeInput();

        if (checkGame() == 3) {
            updateAll();
            drawAll();

            shot();
        }


        if (checkGame()==1){
            batch.draw(sprites.get("winner"),0,0);
        }else if (checkGame()==2){
            batch.draw(sprites.get("gameover"),130,150);
        }

        batch.end();
    }

    private void shot(){
        shotTimer++;
        if(shotTimer==100){
            Random rand = new Random();
            Alien randAlien = aliens.get(rand.nextInt(aliens.size()));
            alienLasers.add(new Laser(randAlien.getX()+15,randAlien.getY(),-8));
            shotTimer=0;
        }
    }
    private void updateAll(){
        updateAliens();
        updateLasers();
        updatePlayer();
        updateWalls();
    }
    private void drawAll(){
        drawAliens();
        drawLasers();
        drawPlayer();
        drawWalls();
        drawScore();
    }
    private void loadAudio() {
        sounds.put("snd1",Gdx.audio.newSound(Gdx.files.internal("1.mp3")));
        sounds.put("snd2",Gdx.audio.newSound(Gdx.files.internal("2.mp3")));
        sounds.put("snd3",Gdx.audio.newSound(Gdx.files.internal("3.mp3")));
        sounds.put("snd4",Gdx.audio.newSound(Gdx.files.internal("4.mp3")));
        sounds.put("fire",Gdx.audio.newSound(Gdx.files.internal("Fire.mp3")));
        sounds.put("die",Gdx.audio.newSound(Gdx.files.internal("Die.mp3")));
        sounds.put("destroy",Gdx.audio.newSound(Gdx.files.internal("Destroy.mp3")));

    }

    private int checkGame(){
        if(aliens.size()==0){
            System.out.println("You are winner!");
            return 1;
        }else{
            for(Alien alien:aliens){
                if (alien.getRect().overlaps(endRect)||!playerAlive){
                    System.out.println("You lose!");
                    return 2;
                }
            }
            return 3;
        }
    }

    private void loadAliens(){
        for (int i=0;i<11;i++){
            aliens.add(new Alien(100+40*i,500,"1"));
            aliens.add(new Alien(100+40*i,460,"2"));
            aliens.add(new Alien(100+40*i,420,"2"));
            aliens.add(new Alien(100+40*i,380,"3"));
            aliens.add(new Alien(100+40*i,340,"3"));
        }
    }
    private void loadSprites(){
        sprites.put("winner", new Texture("winner.jpg"));
        sprites.put("gameover",new Texture("gameover.jpg"));
        sprites.put("alien1A",new Texture("alien1A.jpg"));
        sprites.put("alien1B",new Texture("alien1B.jpg"));
        sprites.put("alien2A",new Texture("alien2A.jpg"));
        sprites.put("alien2B",new Texture("alien2B.jpg"));
        sprites.put("alien3A",new Texture("alien3A.jpg"));
        sprites.put("alien3B",new Texture("alien3B.jpg"));
        sprites.put("title",new Texture("title.jpg"));
        sprites.put("player",new Texture("player.jpg"));
        sprites.put("laser",new Texture("laser.jpg"));
        sprites.put("boom",new Texture("boom.jpg"));
        sprites.put("wall1A",new Texture("wall1A.jpg"));
        sprites.put("wall1B",new Texture("wall1B.jpg"));
        sprites.put("wall1C",new Texture("wall1C.jpg"));
        sprites.put("wall2A",new Texture("wall2A.jpg"));
        sprites.put("wall2B",new Texture("wall2B.jpg"));
        sprites.put("wall2C",new Texture("wall2C.jpg"));
        sprites.put("wall3A",new Texture("wall3A.jpg"));
        sprites.put("wall3B",new Texture("wall3B.jpg"));
        sprites.put("wall1Ab",new Texture("wall1Ab.jpg"));
        sprites.put("wall1Bb",new Texture("wall1Bb.jpg"));
        sprites.put("wall1Cb",new Texture("wall1Cb.jpg"));
        sprites.put("wall2Ab",new Texture("wall2Ab.jpg"));
        sprites.put("wall2Bb",new Texture("wall2Bb.jpg"));
        sprites.put("wall2Cb",new Texture("wall2Cb.jpg"));
        sprites.put("wall3Ab",new Texture("wall3Ab.jpg"));
        sprites.put("wall3Bb",new Texture("wall3Bb.jpg"));
    }
    private void loadWalls(){
        for (int i=0;i<3;i++) {
            walls.add(new Wall(100+i*160, 60, 26, 15, sprites.get("wall3A"), sprites.get("wall3Ab")));
            walls.add(new Wall(156+i*160, 60, 26, 15, sprites.get("wall3B"), sprites.get("wall3Bb")));
            walls.add(new Wall(100+i*160, 75, 26, 27, sprites.get("wall2A"), sprites.get("wall2Ab")));
            walls.add(new Wall(126+i*160, 75, 30, 27, sprites.get("wall2B"), sprites.get("wall2Bb")));
            walls.add(new Wall(156+i*160, 75, 26, 27, sprites.get("wall2C"), sprites.get("wall2Cb")));
            walls.add(new Wall(100+i*160, 102, 26, 18, sprites.get("wall1A"), sprites.get("wall1Ab")));
            walls.add(new Wall(126+i*160, 102, 30, 18, sprites.get("wall1B"), sprites.get("wall1Bb")));
            walls.add(new Wall(156+i*160, 102, 26, 18, sprites.get("wall1C"), sprites.get("wall1Cb")));
        }
    }
    private void loadPlayer(){
        playerRect.set(playerX,playerY,40,25);
    }

    private void takeInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(playerX>0) {
                playerX -= 5;
                playerRect.setX(playerX);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if (playerX<540) {
                playerX += 5;
                playerRect.setX(playerX);
            }
        }
        if(Gdx.input.isKeyJustPressed((Input.Keys.SPACE))){
            if (laserCooldown>=80&&checkGame()==3){
                playerLasers.add(new Laser(playerX + 18, playerY + 25, 8));
                laserCooldown=0;

                sounds.get("fire").play();
            }
            if (checkGame()==2||checkGame()==1){
                reload();
                playerAlive=true;
            }
        }
        laserCooldown++;
    }

    private void drawAliens() {
        for(Alien alien:aliens){
            batch.draw(sprites.get("alien"+alien.getType()+spritePos),alien.getX(),alien.getY());
            if(dir!=-5 && alien.getX()>=maxX){
                timeout=Math.max(5,timeout-5);
                dir=-5;
                for (int i=0;i<aliens.size();i++){
                    aliens.get(i).shiftY(-10);
                }
            }else if(dir!=5 && alien.getX()<=minX){
                timeout=Math.max(5,timeout-5);
                dir=5;
                for (int i=0;i<aliens.size();i++){
                    aliens.get(i).shiftY(-10);
                }
            }
        }
    }
    private void drawLasers() {
        for(Laser laser:playerLasers){
            batch.draw(sprites.get("laser"),laser.getX(),laser.getY());
        }
        for(Laser laser:alienLasers){
            batch.draw(sprites.get("laser"),laser.getX(),laser.getY());
        }
    }
    private void drawPlayer(){
        batch.draw(sprites.get("player"), playerX, playerY);
    }
    private void drawWalls(){
        for (Wall wall:walls){
            batch.draw(wall.getTex(),wall.getX(),wall.getY());
        }
    }
    private void drawScore(){
//        gameFont.draw(batch,"Score: "+score,200,400);
    }

    private void updateLasers() {//Removes all inactive player and alien lasers, and moves them in their directions
        for (int i=0;i<playerLasers.size();i++){
            if (playerLasers.get(i).getActive()){
                playerLasers.get(i).update();
            }else{
                playerLasers.remove(playerLasers.get(i));
            }
        }
        for (int j=0;j<alienLasers.size();j++){
            if (alienLasers.get(j).getActive()){
                alienLasers.get(j).update();
            }else{
                alienLasers.remove(alienLasers.get(j));
            }
        }
    }
    private void updateAliens() {
        if (ct>timeout){
            if(spritePos.equals("A")){
                spritePos="B";
            }else{
                spritePos="A";
            }
            for (int i=0;i<aliens.size();i++){
                aliens.get(i).shiftX(dir);
            }

            sounds.get("snd"+Integer.toString(soundTick)).play();
            if(soundTick==4){
                soundTick=1;
            }else{
                soundTick++;
            }
            ct=0;
        }else{
            ct++;
        }

        for (int i=0;i< playerLasers.size();i++){
            for (int j=0;j<aliens.size();j++){
                if (playerLasers.get(i).getRect().overlaps(aliens.get(j).getRect())){
                    //If any player lasers hit the alien, the alien dies.
                    batch.draw(sprites.get("boom"),aliens.get(j).getX(),aliens.get(j).getY());
                    playerLasers.remove(playerLasers.get(i));
                    //Score update according to type of alien
                    if(aliens.get(j).getType().equals("1")){
                        score+=40;
                    }else if(aliens.get(j).getType().equals("2")){
                        score+=20;
                    }else if(aliens.get(j).getType().equals("3")){
                        score+=10;
                    }
                    aliens.remove(aliens.get(j));
                    //Play alien death sound
                    sounds.get("destroy").play();
                    break;
                }
            }
        }
    }
    private void updatePlayer() {
        for (int i=0;i<alienLasers.size();i++){//Checks if any alien lasers have collided with the player
            if (playerRect.overlaps(alienLasers.get(i).getRect())){
                alienLasers.remove(alienLasers.get(i));
                playerAlive=false;

                sounds.get("die").play();//Plays death sound
            }
        }
    }
    private void updateWalls() {
        for (int i=0;i<walls.size();i++){
            for (int j=0;j<playerLasers.size();j++){
                if (playerLasers.get(j).getRect().overlaps(walls.get(i).getRect())){
                    playerLasers.remove(playerLasers.get(j));
                    walls.get(i).hit();
                    if(!walls.get(i).getActive()) {
                        walls.remove(walls.get(i));
                        sounds.get("destroy").play();
                    }
                }
            }
        }
        for (int i=0;i<walls.size();i++){
            for (int j=0;j<alienLasers.size();j++){
                if (alienLasers.get(j).getRect().overlaps(walls.get(i).getRect())){
                    alienLasers.remove(alienLasers.get(j));
                    walls.get(i).hit();
                    if(!walls.get(i).getActive()) {
                        walls.remove(walls.get(i));
                        sounds.get("destroy").play();
                    }
                }
            }
        }
        for (int i=0;i<walls.size();i++){
            for (int j=0;j<aliens.size();j++){
                if (aliens.get(j).getRect().overlaps(walls.get(i).getRect())){
                    batch.draw(sprites.get("boom"),aliens.get(j).getX(),aliens.get(j).getY());
                    aliens.remove(aliens.get(j));
                    walls.get(i).hit();
                    if(!walls.get(i).getActive()) {
                        walls.remove(walls.get(i));
                        sounds.get("destroy").play();
                    }
                }
            }
        }
    }

    private void reload(){
        aliens.clear();
        playerLasers.clear();
        playerX=150;
        alienLasers.clear();
        walls.clear();
        loadAliens();
        loadWalls();
    }

    @Override
    public void dispose() {
        batch.dispose();
        sprites.get("player").dispose();
        sprites.get("laser").dispose();
    }

    public boolean touchDown (int x, int y, int pointer, int button) {return true;}
    public boolean touchDragged(int x, int y, int z){
        return true;
    }

    public boolean keyTyped(char ch){
        return true;
    }

    public boolean keyDown(int num){
        return true;
    }

    public boolean mouseMoved(int x, int y){
        return true;
    }

    public boolean keyUp(int num){
        return true;
    }

    public boolean touchUp(int x,int y,int x2,int y2){
        return true;
    }

    public boolean scrolled(int num){
        return true;
    }
}

class Wall {
    private float x,y;
    private Rectangle rect=new Rectangle();
    private Texture tex,tex2;
    private int life=2;

    public Wall(float x, float y, float width, float length, Texture tex, Texture tex2){
        this.x=x;
        this.y=y;
        rect.set(x,y,width,length);
        this.tex=tex;
        this.tex2=tex2;
    }

    public Texture getTex(){
        if(life==2){
            return tex;
        }else{
            return tex2;
        }
    }

    public void hit(){
        life--;
    }

    public boolean getActive(){
        if (life<=0){
            return false;
        }else{
            return true;
        }
    }

    public Rectangle getRect(){
        return rect;
    }

    public int getX(){
        return Math.round(x);
    }

    public int getY(){
        return Math.round(y);
    }
}

class Alien {
    private float x,y;
    private String type;
    private Rectangle rect=new Rectangle();
    private boolean alive=true;

    public Alien(float x,float y,String type){
        this.x=x;
        this.y=y;
        this.type=type;
        if (type.equals("1")){
            rect.set(x,y,30,30);
        } else if (type.equals("2")){
            rect.set(x,y,30,22);
        } else if (type.equals("3")){
            rect.set(x,y,30,20);
        }
    }

    public Rectangle getRect(){
        return rect;
    }

    public String getType(){
        return this.type;
    }

    public int getX(){
        return Math.round(this.x);
    }

    public int getY(){
        return Math.round(this.y);
    }

    public void shiftX(int dx){
        this.x+=dx;
        rect.setX(this.x+dx);
    }
    public void shiftY(int dy){
        this.y+=dy;
        rect.setY(this.y+=dy);
    }
}

class Laser {
    private int x, y, dir;
    private boolean active=true;
    private Rectangle rect = new Rectangle();

    public Laser(int x, int y, int dir){
        this.x=x;
        this.y=y;
        this.dir=dir;
        rect.set(x,y,3,21);
    }

    public Rectangle getRect(){
        return rect;
    }

    public boolean getActive(){
        return active;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void update(){
        y+=dir;
        rect.setY(y);
        if (y>Gdx.graphics.getHeight()||y<0){
            active=false;
        }
    }
}
