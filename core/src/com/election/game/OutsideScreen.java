package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

	public Candidate candidate;
	BitmapFont font = new BitmapFont();
	private ArrayList<Electorate> electorate;
	
	
		
	private boolean userOutside = true;
	//boolean for switching between camera and character movement
	private boolean moveCamera = false;
	private boolean cameraAtMapEdge = false;
	
	public OrthographicCamera hudCam;
	
	public OrthographicCameraMovementWrapper worldCam;	
	public SpriteAndTiledRenderer mapRenderer;
	
	public TownMap tileMap;
	
	//private int mapPixelWidth;
	//private int mapPixelHeight;
	
	
	//giggle
	Region [][] regions;
	
	private Vector2 prevPosition;
	
	public static float BOUNDARY_PERCENTAGE = 0.2f;
	
	
	public Rectangle gameSpace;
	private boolean interactBtn=false;
	private Electorate interactedElector;
	private DebugInfo debugInfo;
	private String interactedDoor = Constants.MAP_OBJ_DOOR_NONE;
	private Rectangle prevDoor;
	public Vector3 mousePos;
	Vector2  mouseRegion;

	public OutsideScreen(final ElectionGame gameObj) {

		mousePos = new Vector3();
		mouseRegion = new Vector2();
		
		//create main character and position in middle of map
		candidate = new Candidate( new Texture(Gdx.files.internal("man.png")));
		candidate.setPosition(5,5);
		candidate.sprite.setSize(1f, 1f);		
		
		prevPosition = new Vector2(Constants.VIRTUAL_HEIGHT/2, Constants.VIRTUAL_HEIGHT/2);
		
		//Create new orthographic camera and look at candidate location
		worldCam = new OrthographicCameraMovementWrapper();
		//camera = new OrthographicCameraMovementWrapper(false, w/2, h/2);
		//camera.source.position.set(candidate.getX(), candidate.getY(),0);
		
		hudCam = new OrthographicCamera();
		
		
		
		//create new town map by loading map of town from static file
		//add the sprite to the map so it is rendered with it
		//TODO: should be 1/128f since my tile size will be 128f and I want one unit in my game to equal 1 tile size		
		//right now tile size is 32pixels for town map. Need to fix that.
		float unitScale = 1f / 32f;
		tileMap = 	Constants.tiledMaps.get(Constants.MAP_OUTSIDE_WORLD_PREFIX + "1");
		//tileMap.addSprite(candidate);

		//create a new map renderer
		mapRenderer = new SpriteAndTiledRenderer(tileMap, worldCam, unitScale);
	
		//rectangle for the entire game space
		gameSpace = new Rectangle(0,0, tileMap.mapWidth, tileMap.mapHeight);
		
		//blocks of the world space, so we can easily calculate intersections
		initRegions(tileMap.mapWidth, tileMap.mapHeight);

		//people that candidate can convince to vote for him
		initElectorate();
		tileMap.addSprites(electorate);
		
		//render these using the mapRenderer
		//mapRenderer.setSprites(electorate);
		mapRenderer.setCandidate(candidate);

		debugInfo = new DebugInfo();
	}
	
	@Override
	public void resize(int width, int height){
		
		worldCam.setToOrtho(false, Constants.VIRTUAL_HEIGHT *width/(float)height, Constants.VIRTUAL_HEIGHT);
		hudCam.setToOrtho(false, width, height);
	}
	
	private void loadMap(String mapId, float unitScale){
		
		tileMap = Constants.tiledMaps.get(mapId);
		//tileMap.addSprite(candidate);
		
		
		if( mapId.startsWith(Constants.MAP_OUTSIDE_WORLD_PREFIX)){
		
			mapRenderer.resetMap(tileMap, 1f/32f);

			candidate.setPosition(prevDoor.getX(), prevDoor.getY());
			userOutside = true;
			
		}else{
			mapRenderer.resetMap(tileMap, 1f/128f);
			
			RectangleMapObject obj =  (RectangleMapObject) tileMap.getMapObjects(Constants.MAP_OBJ_PHYSICS_LAYER).get("door");		
			candidate.setPosition(obj.getRectangle().x, obj.getRectangle().y+obj.getRectangle().height);
			userOutside = false;
			
		}
		 
	}
	

	private void initRegions(int mapWidth, int mapHeight) {
		
		int numTilesX = mapWidth;//Constants.TILE_SIZE;
		int numTilesY = mapHeight;///Constants.TILE_SIZE;
		
		
		regions = new Region[numTilesX][numTilesY];
		
		for (int i = 0; i < numTilesX; i++) {

			for (int j = 0; j < numTilesY; j++) {
				
				
				regions[i][j] = new Region(i, j);
				
				
			}
			
		}
		
				
	}
	
	
	Region getRegion(int xLoc, int yLoc){
		
		int xIdx= xLoc; ///Constants.TILE_SIZE;
		int yIdx= yLoc; ///Constants.TILE_SIZE;
		
		
		Region region = regions[xIdx][yIdx];
		
		return region;
		
		
	}

	

	private void initElectorate() {
		
		
		MapLayer layer  = tileMap.tiledMap.getLayers().get(Constants.MAP_OBJ_NPC_LOCATION_LAYER);
		
		if( layer == null )		
			return;
		
		MapObjects mapObjects = layer.getObjects();
		
		
		
		
		
		
		electorate = new ArrayList<Electorate>();
		for (int i=0; i<Constants.ELECTORATE_COUNT_MAX; i++ ) {
		
			int randomIdx = ElectionGame.randGen.nextInt(mapObjects.getCount());		
			MapObject mapObject = mapObjects.get(randomIdx);
			
						
			if( mapObject instanceof RectangleMapObject ){
			
				RectangleMapObject obj = (RectangleMapObject) mapObject;
				
				int personType = 1 + ElectionGame.randGen.nextInt(NUM_TYPES_PEOPLE);
						
					 
				//int xLoc = (int)(ElectionGame.randGen.nextFloat() * tileMap.mapWidth);				
				//int yLoc = (int)(ElectionGame.randGen.nextFloat() * tileMap.mapHeight);
				
				Rectangle rect = obj.getRectangle();//Utilities.scaleRectangle(obj.getRectangle(),  mapRenderer.getUnitScale());	
				
				float xLoc = rect.x + (ElectionGame.randGen.nextFloat() *  rect.width);
				float yLoc = rect.y + (ElectionGame.randGen.nextFloat() * rect.height);
				
				
				Electorate elector = new Electorate( new Texture( Gdx.files.internal("person" + personType + ".png")) );
				
				/*Gdx.app.log(this.getClass().getName(), "Creating Elector at: (" + xLoc + ", " + yLoc + ")" );
				
				elector.sprite.setPosition(xLoc, yLoc);			
				electorate.add(elector);
				*/
				
				float regionX = xLoc;				
				float regionY = yLoc;
				Gdx.app.log(this.getClass().getName(), "Creating Elector at screen position: (" + regionX + ", " + regionY + ")" );

				elector.sprite.setPosition(regionX, regionY);
				electorate.add(elector);
				
				Gdx.app.log(this.getClass().getName(), "Region: (" + (int)regionX + ", " + (int)regionY + " )" );
				
				
				//electors going into wrong regions?
				Region region = regions[(int)regionX ][(int)regionY];
				region.addElectors(elector);
			
		
			
			}
			
			
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
		
		updateMouseRegion();
		updateCandidate(delta);
		checkCollisions(delta);		
		updateCamera(delta);	
		renderGraphics(delta);
		
		renderHud(delta);
		
		
		//draw dialog box
		if( ElectionGame.GAME_OBJ.state == GameState.DIALOG){
			updateDialog(delta);
		}

	}

	private void renderGraphics(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 
		renderMap(delta);
		renderSprites(delta);
		
	}

	private void updateMouseRegion() {

		
		//mouse position is set in pixels - top left is 0,0, bottom right is 1024, 800 (depending on screen width/height)
		//must convert from screen position to map position
		
		
		
		//take into account camera position
		
		//float regionY = (mousePos.y - worldCam.source.position.y)/Constants.VIRTUAL_HEIGHT;
		//float regionX =  (mousePos.x - worldCam.source.position.x)/ Constants.VIRTUAL_HEIGHT;
		
		mouseRegion.y = (int)mousePos.y;
		mouseRegion.x = (int)mousePos.x;
		
	}

	private void renderMap(float delta){
		worldCam.source.update();
		mapRenderer.setView(worldCam.source);		
		mapRenderer.render();


	}
	
	private void renderSprites(float delta) {
		
		ElectionGame.GAME_OBJ.batch.begin();
		
		candidate.draw();
				
		ElectionGame.GAME_OBJ.batch.end();

		
	}
	
	private void renderHud(float delta) {
		/*Matrix4 uiMatrix = hudCam.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		*/
		
		renderDebugInfo();
		
	}

	private void renderDebugInfo() {
			
		debugInfo.render(this);
		
		
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

		Region region = getRegion((int)candidate.getX(), (int)candidate.getY());
		
		if( region.electorsInRegion.size() == 0){
			interactedElector = null;
			return;
		}
		
		
		for (Electorate elector : region.electorsInRegion) {
			
			if( candidate.overlaps(elector.sprite.getBoundingRectangle())){
						
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
		
		Rectangle candBound =  candidate.getBoundingRectangle();
		
		Rectangle movementRegion = worldCam.boundsRect;
		
		
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
			worldCam.update(delta, candidate);
			mapRenderer.setView(worldCam.source);	
		}
		
		
	}
	
	
	private void checkMapCollisions(float delta) {

		/* 
		 *
		 * For the future: iterate through physics objects, add each
		 * to list of regions it overlaps to avoid so much collision checking
		 * 
		 */
		
		//Region region = getRegion((int)candidate.getX(), (int)candidate.getY());

		
		MapObjects mapObjects = tileMap.mapObjs;
 
		
		for (MapObject object : mapObjects){
		//for (MapObject object : townMap.mapObjs) {
			
			if (object instanceof TextureMapObject) {
                continue;
            }
			
            if (object instanceof RectangleMapObject) {

            	
            	RectangleMapObject mapObj = (RectangleMapObject)object;
            	
            	String type = (String) mapObj.getProperties().get(Constants.MAP_OBJ_OBJECT_TYPE);
            	        	
            	Rectangle rect = mapObj.getRectangle();//Utilities.scaleRectangle(mapObj.getRectangle(), mapRenderer.getUnitScale());

            	
            	if( rect.overlaps(candidate.getBoundingRectangle())){
            		
            		//If its a door, and you are overlapping, let candidate move through, and set the ID for the interactedDoor
            		if( type.equals(Constants.MAP_OBJ_DOOR)){
            			if(userOutside){
            				prevDoor = rect;
            				interactedDoor = (String) mapObj.getProperties().get(Constants.MAP_OBJ_DOOR_ID);
            			}else{
            				
            				interactedDoor = Constants.MAP_OUTSIDE_WORLD_PREFIX+ (String) mapObj.getProperties().get(Constants.MAP_OBJ_DOOR_ID);
            			}
                	}else{  //if it is not a door, its a regular collision object - don't let the player pass through
                		interactedDoor = Constants.MAP_OBJ_DOOR_NONE;
                		candidate.setPosition(prevPosition.x, prevPosition.y);
                		moveCamera= false;

                	}
                	
            	}
            	
            	
            }
            else if (object instanceof PolygonMapObject) {

            	
            	Polygon polygon = ((PolygonMapObject)object).getPolygon();
            	//polygon.setScale(mapRenderer.getUnitScale(), mapRenderer.getUnitScale());
            	
            	Rectangle boundingRect = candidate.getBoundingRectangle();
            	
            	if( polygon.contains(boundingRect.x, boundingRect.y) ||
            		polygon.contains( boundingRect.x, boundingRect.y + boundingRect.height) ||
            		polygon.contains( boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height) ||
            		polygon.contains( boundingRect.x + boundingRect.width, boundingRect.y )	 ){
            		
            		candidate.setPosition(prevPosition.x, prevPosition.y);
            		moveCamera= false;
            	}
            	
            	
            	
            }
            else if (object instanceof PolylineMapObject) {
            	
            	Polyline polyline = ((PolylineMapObject)object).getPolyline();
            	
            	if( polyline.contains(candidate.getBoundingRectangle().x, candidate.getBoundingRectangle().y)){
            		candidate.setPosition(prevPosition.x, prevPosition.y);
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
		
		prevPosition.set( candidate.getX(), candidate.getY());
	
		
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
		
		tileMap.tiledMap.dispose();
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
		

		
		//interactBtn = !interactBtn;
		interactBtn = true;
		
		
		
		
		
		if(interactBtn ){

			
			if( !Constants.MAP_OBJ_DOOR_NONE.equals( interactedDoor)){
				loadMap(interactedDoor, 1f/128f);
				worldCam.setLookAt(candidate.getX(), candidate.getY());
				return;
			}else{
				interactBtn = false;
			}
			
			if( interactedElector != null){
				ElectionGame.GAME_OBJ.state = GameState.DIALOG;			
				Gdx.input.setInputProcessor(ElectionGame.GAME_OBJ.dialogHandler);
			}else{
				interactBtn = false;
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

		
		
		
		mousePos.x = screenX;
		mousePos.y = screenY;
		
		worldCam.source.unproject(mousePos);
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
