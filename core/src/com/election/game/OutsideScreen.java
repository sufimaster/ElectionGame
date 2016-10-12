package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.election.game.States.GameState;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.render.DebugRenderer;
import com.election.game.render.SpriteAndTiledRenderer;
import com.election.game.sprites.Candidate;

public class OutsideScreen implements Screen, InputProcessor {

	private static final int CANDIDATE_MOVE_W_KEY = Keys.W;
	private static final int CANDIDATE_MOVE_S_KEY = Keys.S;
	private static final int CANDIDATE_MOVE_A_KEY = Keys.A;
	private static final int CANDIDATE_MOVE_D_KEY = Keys.D;
	private static final int NUM_TYPES_PEOPLE = 3;

	private Candidate candidate;
	BitmapFont font = new BitmapFont();
	private ArrayList<Electorate> electorate;
	
	
		
	//boolean for switching between camera and character movement
	private boolean moveCamera = false;

	private boolean cameraAtMapEdge = false;
	
	private OrthographicCameraMovementWrapper camera;	
	private SpriteAndTiledRenderer mapRenderer;
	
	private TiledMap townMap;
	
	private int mapPixelWidth;
	private int mapPixelHeight;
	
	
	//giggle
	private Region [][] regions;
	
	private Vector2 prevPosition;
	
	public static float BOUNDARY_PERCENTAGE = 0.2f;
	
	
	public Rectangle gameSpace;
	private boolean interactBtn=false;
	private Electorate interactedElector;

	public OutsideScreen(final ElectionGame gameObj) {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		
		camera = new OrthographicCameraMovementWrapper(false, w/2, h/2);
		//camera.source.setToOrtho(false, w, h);
		
		//main character
		candidate = new Candidate( new Texture(Gdx.files.internal("man.png")) );
		candidate.sprite.setPosition(w/2, h/2);
		prevPosition = new Vector2(candidate.sprite.getX(), candidate.sprite.getY());
		camera.source.position.set(candidate.sprite.getX(), candidate.sprite.getY(),0);
		
				
		float unitScale = 1 / 1f;
		townMap = 	new TmxMapLoader().load("town.tmx");
		mapRenderer = new SpriteAndTiledRenderer(townMap, camera, unitScale);
		
		MapProperties prop = townMap.getProperties();

		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		mapPixelWidth = mapWidth * tilePixelWidth;
		mapPixelHeight = mapHeight * tilePixelHeight;

		gameSpace = new Rectangle(0,0, mapPixelWidth, mapPixelHeight);
		
		//people that candidate can convince to vote for him
		initRegions(mapPixelWidth, mapPixelHeight);
		initElectorate();		
		//render these using the mapRenderer
		mapRenderer.setSprites(electorate);
		mapRenderer.setCandidate(candidate);

		
	}

	private void initRegions(int mapWidth, int mapHeight) {
		
		int numTilesX = mapWidth/Constants.TILE_SIZE;
		int numTilesY = mapHeight/Constants.TILE_SIZE;
		
		
		regions = new Region[numTilesX][numTilesY];
		
		for (int i = 0; i < numTilesX; i++) {

			for (int j = 0; j < numTilesY; j++) {
				
				
				regions[i][j] = new Region(i*Constants.TILE_SIZE, j*Constants.TILE_SIZE);
				
				
			}
			
		}
		
				
	}
	
	
	private Region getRegion(int xLoc, int yLoc){
		
		int xIdx= xLoc/Constants.TILE_SIZE;
		int yIdx= yLoc/Constants.TILE_SIZE;
		
		
		Region region = regions[xIdx][yIdx];
		
		return region;
		
		
	}
	

	private void initElectorate() {
		
		electorate = new ArrayList<Electorate>();
		for (int i=0; i<Constants.ELECTORATE_COUNT_MAX; i++ ) {
			
			int personType = 1 + ElectionGame.randGen.nextInt(NUM_TYPES_PEOPLE);
					
				 
			int xLoc = (int)(ElectionGame.randGen.nextFloat() * mapPixelWidth);				
			int yLoc = (int)(ElectionGame.randGen.nextFloat() * mapPixelHeight);
			
			while( !TiledMapUtility.isElectorateSpace(townMap, xLoc, yLoc) ){
				
				xLoc = (int)(ElectionGame.randGen.nextFloat() * mapPixelWidth);				
				yLoc = (int)(ElectionGame.randGen.nextFloat() * mapPixelHeight);
				
			}
			
			
			
			
			
			Electorate elector = new Electorate( new Texture( Gdx.files.internal("person" + personType + ".png")) );
			elector.sprite.setPosition(xLoc, yLoc);			
			electorate.add(elector);
			
			Region region = regions[xLoc/Constants.TILE_SIZE][yLoc/Constants.TILE_SIZE];
			region.addElectors(elector);
			
		
			
		}
	}

	
	
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		
		
		
		
		switch( ElectionGame.GAME_OBJ.state) {
		
			case RUNNING:		
				updateRunning(delta);		
				break;
			case PAUSED:
				updatePaused(delta);
				break;
			case DIALOG:
				updateRunning(delta);
				break;
			default:
				break;
		}
			
	}
	
	private void updateRunning(float delta){
		updateCandidate(delta);
		checkCollisions(delta);		
		updateCamera(delta);	
		renderSprites(delta);
		renderHud(delta);

	}

	private void renderSprites(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 

		camera.source.update();
		mapRenderer.setView(camera.source);		
		mapRenderer.render();

		
		DebugRenderer.DrawDebugCameraScrollBounds( camera);
		
	}
	
	private void renderHud(float delta) {
		Matrix4 uiMatrix = camera.source.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		ElectionGame.GAME_OBJ.hudBatch.setProjectionMatrix(uiMatrix);
		ElectionGame.GAME_OBJ.hudBatch.begin();
		
			
			renderDebugInfo();
						
			//draw dialog box
			if( ElectionGame.GAME_OBJ.state == GameState.DIALOG){
				updateDialog(delta);
			}
			
		ElectionGame.GAME_OBJ.hudBatch.end();		
	}

	private void renderDebugInfo() {

		if(!ElectionGame.GAME_OBJ.isdebug){
			return;
		}
		
		
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "FPS:" + Gdx.graphics.getFramesPerSecond(), 10, Constants.WINDOWS_GAME_HEIGHT - 60 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "STATE:" + ElectionGame.GAME_OBJ.state, 10, Constants.WINDOWS_GAME_HEIGHT - 76 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Candidate Pos: [" + candidate.sprite.getBoundingRectangle().getX() + ", " + candidate.sprite.getBoundingRectangle().getY() + "]", 10, Constants.WINDOWS_GAME_HEIGHT - 92 );
		ElectionGame.GAME_OBJ.debugFont.draw(ElectionGame.GAME_OBJ.hudBatch, "Camera Position: [" + camera.source.position.x + ", " + camera.source.position.y + "]", 10, Constants.WINDOWS_GAME_HEIGHT - 108 );
		
		
	}

	private void updatePaused(float delta){
		Gdx.gl.glClearColor(.3f, .2f, .4f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 

		//camera.source.update();
	}
	
	private void updateDialog(float delta) {
		

		//draw dialog box
		if( interactBtn){
			//should only call this method once.
			ElectionGame.GAME_OBJ.dialogHandler.startDialog(interactedElector);
			interactBtn = false;
			interactedElector = null;
		}
		ElectionGame.GAME_OBJ.dialogHandler.draw(ElectionGame.GAME_OBJ.hudBatch, delta);
		
		//get the line of dialog for the interacted elector
		//DialogTree dialogTree = ElectionGame.GAME_OBJ.dialogHandler.getDialogTrees().get(interactedElector.id+"");
		//TODO: for testing, just get the lines for elector id 0
		//TODO: this should all be done in dialogObj, not here
		
        
	}

	private void checkCollisions(float delta) {

						
		checkMapCollisions(delta);
		
		checkCameraBounds(delta );
		
		checkElectorCollisions(delta);
	
		
		
	}
	
	//see if candidate is intersecting any of the electors
	private void checkElectorCollisions(float delta) {

		Region region = getRegion((int)candidate.sprite.getX(), (int)candidate.sprite.getY());
		
		if( region.electorsInRegion.size() == 0){
			interactedElector = null;
			return;
		}
		
		
		for (Electorate elector : region.electorsInRegion) {
			
			if( candidate.sprite.getBoundingRectangle().overlaps(elector.sprite.getBoundingRectangle())){
						
				elector.hit= true;
				//Gdx.app.log(System.class.getName(), "Candidate intersects " + elector.id);
			
				interactedElector = elector;
				
			}else{
				elector.hit = false;
				interactedElector = null;
				//toggleInteract();
			}
	
		}
		
	}

	private void checkCameraBounds(float delta) {
		
		Rectangle candBound =  candidate.sprite.getBoundingRectangle();
		
		Rectangle movementRegion = camera.boundsRect;
		
		
		if( ! movementRegion.overlaps(candBound)){
									
			if( !cameraAtMapEdge ){ 			
				moveCamera = true;			
				
				//Gdx.app.log(this.getClass().getName(), "Candidate is outside boundary region");
			}else{
				moveCamera = false;
			}
			
		}else{
			moveCamera = false;
			cameraAtMapEdge = false;
		}
		
	
	
		if(!gameSpace.contains(candBound)){
			candidate.resetPosition();
			moveCamera = false;
		}
		
		//TODO: this not working, or causes camera to try to catch up even if within the vertical bounds (but outside of horizontal bounds)
		//solution is to check if candidate is within x bounds and check x bounds seperately. 
		
		
	}

	
	
	private void updateCandidate(float delta) {
        
	
		candidate.update(delta);
		
				
	}
	
	private void updateCamera(float delta){
		
		if(moveCamera){
			//send camera an update on candidates position so it can move faster to catchup
			camera.update(delta, candidate);
			mapRenderer.setView(camera.source);	
		}
		
		
	}
	
	
	private void checkMapCollisions(float delta) {


		
		MapObjects mapObjects = townMap.getLayers().get("physics").getObjects();
 
		
		for (MapObject object : mapObjects){
		//for (MapObject object : townMap.mapObjs) {
			
			if (object instanceof TextureMapObject) {
                continue;
            }
			
            if (object instanceof RectangleMapObject) {

            	Rectangle rect = ((RectangleMapObject)object).getRectangle();
            	
            	if( rect.overlaps(candidate.sprite.getBoundingRectangle())){
            		
            		candidate.sprite.setPosition(prevPosition.x, prevPosition.y);
            		moveCamera= false;
            		/*String type = (String) object.getProperties().get("type");
        			System.out.println("Object type: " + type);*/	
            	}
            	
            	
            }
            else if (object instanceof PolygonMapObject) {

            	
            	Polygon polygon = ((PolygonMapObject)object).getPolygon();
            	if( polygon.getBoundingRectangle().overlaps( candidate.sprite.getBoundingRectangle()) ){
            		candidate.sprite.setPosition(prevPosition.x, prevPosition.y);
            		moveCamera= false;
            		/*String type = (String) object.getProperties().get("type");
        			System.out.println("Object type: " + type);	*/
            	}
            	
            }
            else if (object instanceof PolylineMapObject) {
            	
            	Polyline polyline = ((PolylineMapObject)object).getPolyline();
            	if( polyline.contains(candidate.sprite.getBoundingRectangle().x, candidate.sprite.getBoundingRectangle().y)){
            		candidate.sprite.setPosition(prevPosition.x, prevPosition.y);
            		moveCamera= false;
            		/*String type = (String) object.getProperties().get("type");
        			System.out.println("Object type: " + type);	*/
            	}
            	
            	
            	
            }
            else if (object instanceof CircleMapObject) {

            }
            else {
                continue;
            }

			
			
		}
		
		prevPosition.set( candidate.sprite.getX(), candidate.sprite.getY());
	
		
	}




	private void transitionToInsideScreen() {
		ElectionGame.GAME_OBJ.setScreen(new InsideScreen(candidate));
		dispose();
	}


	
	
	
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		//gameObj.setScreen(new OutsideScreen(gameObj));
		//dispose();
	}

	@Override
	public void dispose() {
		//candidate.sprite.getTexture().dispose();
		/*for (House house : houses) {
			house.sprite.getTexture().dispose();
		}
		*/
		
		townMap.dispose();
	}

	
	/*
	 * start moving when you press the key
	 * 
	 */
	
	@Override
	public boolean keyDown(int keycode) {
		
		//Gdx.app.log(this.getClass().getCanonicalName(), "keycode is " + Keys.toString(keycode));
		//Gdx.app.log(this.getClass().getCanonicalName(), "Delta time is " + Gdx.graphics.getDeltaTime());

		
		switch (keycode) {
			case CANDIDATE_MOVE_W_KEY:
				//candidate.moveY(-Gdx.graphics.getDeltaTime() );				
				candidate.setMoveUp(true);
				break;
				
				
			case CANDIDATE_MOVE_S_KEY:
				//candidate.moveX(-Gdx.graphics.getDeltaTime() );
				candidate.setMoveDown(true);
				break;
			
			case CANDIDATE_MOVE_A_KEY:
				//candidate.moveX(Gdx.graphics.getDeltaTime() );
				candidate.setMoveLeft(true);
				break;	
			
			case CANDIDATE_MOVE_D_KEY:
				//candidate.moveX(-Gdx.graphics.getDeltaTime() );
				candidate.setMoveRight(true);
				break;	
			case Keys.NUM_0:
				//candidate.moveX(-Gdx.graphics.getDeltaTime() );
				ElectionGame.GAME_OBJ.isdebug = !ElectionGame.GAME_OBJ.isdebug;
				break;		
		}
		
		
		
		return true;
	}

	/*
	 * keep moving until key moves up(non-Javadoc)
	 * @see com.badlogic.gdx.InputProcessor#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keycode) {


		switch (keycode) {
			case CANDIDATE_MOVE_W_KEY:
				candidate.setMoveUp(false);
				break;
				
				
			case CANDIDATE_MOVE_S_KEY:
				candidate.setMoveDown(false);
				break;
			
			case CANDIDATE_MOVE_A_KEY:
				candidate.setMoveLeft(false);
				break;	
			
			case CANDIDATE_MOVE_D_KEY:
				candidate.setMoveRight(false);
				break;	
			case Constants.CANDIDATE_INTERACT_KEY:
				toggleInteract();
				break;	
			case Keys.ESCAPE:
				Gdx.app.exit();
				break;
			case Keys.BACKSPACE:
				ElectionGame.GAME_OBJ.togglePause();		
				break;
			case Keys.ENTER:
				ElectionGame.toggleFullScreen();				
				break;
		}
		
		

		return true;
	}

	private void toggleInteract() {
		

		
		interactBtn = !interactBtn;
		
		
		
		
		
		if(interactBtn ){

			if( interactedElector != null){
				ElectionGame.GAME_OBJ.state = GameState.DIALOG;			
				Gdx.input.setInputProcessor(ElectionGame.GAME_OBJ.dialogHandler);
			}
			
		}else{
			ElectionGame.GAME_OBJ.state = GameState.RUNNING;
		}
		
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
