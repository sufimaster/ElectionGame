package com.election.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.election.game.States.GameState;
import com.election.game.camera.OrthographicCameraMovementWrapper;
import com.election.game.maps.TownMap;
import com.election.game.render.SpriteAndTiledRenderer;
import com.election.game.sprites.Candidate;

public class OutsideScreen implements Screen, InputProcessor {


	public Candidate candidate;
	BitmapFont font = new BitmapFont();
	//private ArrayList<Electorate> electorate;
	
	
		
	private boolean userOutside = false;
	//boolean for switching between camera and character movement
	private boolean moveCamera = false;
	private boolean cameraAtMapEdge = false;
	
	public OrthographicCamera hudCam;
	
	public OrthographicCameraMovementWrapper worldCam;	
	public SpriteAndTiledRenderer mapRenderer;
	
	public TownMap tileMap;
	
	
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
	
	private String currentMap;
	public TownMap currentTownMap;
	
	private float elapsedTime;	
	float alphaBlend =0f;
	private String exitedHouseId;
	private Texture battleBackground;
	
	
	public OutsideScreen(final ElectionGame gameObj) {
				
		mousePos = new Vector3();
		mouseRegion = new Vector2();
		
		//create main character and position in middle of map
		//TODO: this needs to change so that the guy is in the map 
		candidate = new Candidate( new Texture(Gdx.files.internal(Constants.PC_IMG_SRC)));
		//candidate.setPosition(5,5);
		candidate.setSize(1f, 2f);		
		
		//battle screen init
		battleBackground = new Texture(Gdx.files.internal(Constants.BATTLE_BG));
		
		//prevPosition = new Vector2(Constants.VIRTUAL_HEIGHT/2, Constants.VIRTUAL_HEIGHT/2);
		
		//Create new orthographic camera and look at candidate location
		//worldCam = new OrthographicCameraMovementWrapper();
		worldCam = new OrthographicCameraMovementWrapper(true, Constants.RES_1024_800[0], Constants.RES_1024_800[1]);
		
		hudCam = new OrthographicCamera();
		
		
		currentMap = Constants.MAP_OUTSIDE_WORLD_PREFIX + "1";
		//create new town map by loading map of town from static file
		//add the sprite to the map so it is rendered with it
		
			
		tileMap = ElectionGame.GAME_OBJ.mapHandler.getMap(Constants.MAP_MOMS_HOUSE);
		tileMap.activate();
		currentTownMap =tileMap;
		initCandidatePosition();

		//tileMap = Constants.tiledMaps.get(currentMap);
		
		//create a new map renderer
		mapRenderer = new SpriteAndTiledRenderer(tileMap, worldCam, Constants.UNIT_SCALE);
	
		//rectangle for the entire game space
		gameSpace = new Rectangle(0,0, tileMap.mapWidth, tileMap.mapHeight);
		
		//render these using the mapRenderer
		//mapRenderer.setSprites(electorate);
		mapRenderer.setCandidate(candidate);

		debugInfo = new DebugInfo();
		worldCam.setLookAt(candidate.getX(), candidate.getY());
	}
	
	@Override
	public void resize(int width, int height){
		
		worldCam.setToOrtho(false, Constants.VIRTUAL_HEIGHT *width/(float)height, Constants.VIRTUAL_HEIGHT);
		hudCam.setToOrtho(false, width, height);
	}
	
	private void initCandidatePosition() {
		RectangleMapObject obj =  (RectangleMapObject) tileMap.getMapObjects(Constants.MAP_OBJ_PHYSICS_LAYER).get("door");		
		candidate.setPosition(obj.getRectangle().x, obj.getRectangle().y+obj.getRectangle().height);
		
		//need to set this so camera doesn't pan to candidate inside the house
		prevPosition = new Vector2(candidate.getX(), candidate.getY());
		userOutside = false;
	}
	
	private void loadMap(String mapId, float unitScale){
		
		//this should not be null. If there's a mapId, a map should be associated, I think
		TownMap newMap = ElectionGame.GAME_OBJ.mapHandler.getMap(mapId);

		if( newMap==null)
			return;
		
		currentMap = mapId;
		tileMap = newMap;
		
		tileMap.activate();
		currentTownMap = tileMap;
		//tileMap = Constants.tiledMaps.get(mapId);
		//tileMap.addSprite(candidate);
		
		//switching map from indoor to outdoor
		if( mapId.startsWith(Constants.MAP_OUTSIDE_WORLD_PREFIX)){
		
			mapRenderer.resetMap(tileMap, 1f/32f);

			//this means that you've started out in a house, so there is no prevdoor
			if ( prevDoor== null) {
				RectangleMapObject mapObj = (RectangleMapObject) currentTownMap.getMapObjects(Constants.MAP_OBJ_PHYSICS_LAYER).get(Constants.MAP_OBJ_EXIT_AREA + exitedHouseId);
				prevDoor = mapObj.getRectangle();
			}
			/*
			Region region = getRegion((int)prevDoor.getX(), (int)prevDoor.getY());
			*/
			
			//set the candidates position to the previous position he was before he went in the house, 
			//or the default position set when the game starts out in a house
			candidate.setPosition(prevDoor.getX(), prevDoor.getY());

			userOutside = true;
			
		}else{ //switching from outdoor map to indoor. The scale of indoor render should match the scale of the indoor map
			mapRenderer.resetMap(tileMap, 1/16f);
			
			initCandidatePosition();
			
		}
		
		//worldCam.setLookAt(candidate.getX(), candidate.getY());

		gameSpace.set(0, 0, currentTownMap.mapWidth, currentTownMap.mapHeight);
		ElectionGame.GAME_OBJ.state = GameState.MAP_TRANSITION;

		
	}
	

	
	Region getRegion(int xLoc, int yLoc){
		
		int xIdx= xLoc; ///Constants.TILE_SIZE;
		int yIdx= yLoc; ///Constants.TILE_SIZE;
		
		
		Region region = currentTownMap.regions[xIdx][yIdx];
		
		return region;
		
		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		
		switch( ElectionGame.GAME_OBJ.state) {
		
			case RUNNING:		
				renderRunningState(delta);
				//updateRunning(delta);		
				break;
			case PAUSED:
				renderPausedState(delta);
				//updatePaused(delta);
				break;
			case DIALOG:
				renderDialogState(delta);
				//updateRunning(delta);
				break;
			case BATTLE:
				renderBattleState(delta);
				//updateRunning(delta);
				break;				
			case MAP_TRANSITION:
				renderMapTransitionState(delta);
				//updateRunning(delta);
				break;
			default:
				break;
		}
			
	}
	
	private void renderBattleState(float delta) {

		checkCollisions(delta);		
		updateCamera(delta);
		renderBattleView(delta);
	}

	public void renderRunningState(float delta){
		updateMouseRegion();
		updateCandidate(delta);
		checkCollisions(delta);		
		updateCamera(delta);	
		renderGraphics(delta);
	}
	
	public void renderPausedState(float delta){
		updatePaused(delta);
	}
	
	public void renderDialogState(float delta){
		updateMouseRegion();
		updateCandidate(delta);
		checkCollisions(delta);		
		updateCamera(delta);	
		renderGraphics(delta);
		updateDialog(delta);
		
	}

	public void renderMapTransitionState(float delta){
		elapsedTime += delta;		

		
		if( elapsedTime < (Constants.MAP_TRANSITION_TIME/2) ){
			
			
			alphaBlend += delta * (1/(Constants.MAP_TRANSITION_TIME/2));
			if( alphaBlend >= 1){
				alphaBlend =1;
			}		
		}
		
		if( elapsedTime >= (Constants.MAP_TRANSITION_TIME/2) ){
			
			alphaBlend -= delta * (1/(Constants.MAP_TRANSITION_TIME/2));

			if( alphaBlend <= 0){
				alphaBlend = 0;
			}
			
			renderMap(delta);
			
		}

		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    ElectionGame.GAME_OBJ.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    ElectionGame.GAME_OBJ.shapeRenderer.setColor(new Color(0, 0, 0, alphaBlend));
	    ElectionGame.GAME_OBJ.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    ElectionGame.GAME_OBJ.shapeRenderer.end();
	    Gdx.gl.glDisable(GL20.GL_BLEND);
		


		renderDebugInfo(delta);

		
		if( elapsedTime >= Constants.MAP_TRANSITION_TIME){
			ElectionGame.GAME_OBJ.state = GameState.RUNNING;
			elapsedTime =0f;
		}
	}
	

	private void renderGraphics(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 
		renderMap(delta);
		renderSprites(delta);
		
		
		renderHud(delta);
	}

	private void renderBattleView(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 
		//renderMap(delta);
		renderBattleGraphics(delta);
		
		
		renderHud(delta);
	}
	
	
	private void updateMouseRegion() {

		mouseRegion.y = (int)mousePos.y;
		mouseRegion.x = (int)mousePos.x;
		
	}

	private void renderMap(float delta){

		worldCam.source.update();
		mapRenderer.setView(worldCam.source);		
		mapRenderer.render();

	}
	
	private void renderSprites(float delta) {
		
		ElectionGame.GAME_OBJ.batch.setProjectionMatrix(worldCam.source.combined);
		
		ElectionGame.GAME_OBJ.batch.begin();
		for (Electorate electorate2 : currentTownMap.electorate) {
			electorate2.draw(ElectionGame.GAME_OBJ.batch);
		}
		
		candidate.draw(ElectionGame.GAME_OBJ.batch);
		
		ElectionGame.GAME_OBJ.batch.end();

		
	}
	
	private void renderBattleGraphics(float delta) {
		
		ElectionGame.GAME_OBJ.batch.setProjectionMatrix(worldCam.source.combined);
		
		ElectionGame.GAME_OBJ.batch.begin();
		//draw battlefield
		ElectionGame.GAME_OBJ.batch.draw(battleBackground, 200, 200);//, Gdx.graphics.getWidth()- battleBackground.getWidth()/2, Gdx.graphics.getHeight() - battleBackground.getHeight()/2);
		
		
		//draw character sprite
		candidate.setSize(3, 3);
		candidate.draw(ElectionGame.GAME_OBJ.batch);
		
		//draw opponent sprite
		
		//draw battle data

		ElectionGame.GAME_OBJ.batch.end();

		
	}
	
	
	private void renderHud(float delta) {
		/*Matrix4 uiMatrix = hudCam.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Constants.WINDOWS_GAME_WIDTH, Constants.WINDOWS_GAME_HEIGHT);
		*/
		renderQuestInfo(delta);
		renderDebugInfo(delta);
		
	}

	private void renderQuestInfo(float delta) {


		ElectionGame.GAME_OBJ.questHandler.draw(delta);
		
	}

	private void renderDebugInfo(float delta) {
			
		debugInfo.render(this);
		
		
	}


	private void updatePaused(float delta){
		Gdx.gl.glClearColor(.3f, .2f, .4f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 
		//camera.source.update();
	}
	
	private void updateDialog(float delta) {
		
		Timer.schedule(new Task() {
			
			@Override
			public void run() {

				//draw dialog box
				if( interactBtn){
					//should only call this method once.
					ElectionGame.GAME_OBJ.dialogHandler.startDialog(interactedElector);
					interactBtn = false;
					interactedElector = null;
				}
		
			}
		}, .3f);

		ElectionGame.GAME_OBJ.dialogHandler.draw(delta);
        
	}

	private void checkCollisions(float delta) {

						
		checkMapCollisions(delta);
		
		checkCameraBounds(delta );
		
		checkElectorCollisions(delta);
	
		
		
	}
	
	//see if candidate is intersecting any of the electors
	private void checkElectorCollisions(float delta) {

		Region region = getRegion((int)candidate.getX(), (int)candidate.getY()); //need to get sale of tilemap);
		
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

		interactedDoor = Constants.MAP_OBJ_DOOR_NONE;
		
		if( tileMap == null)
			return;
		
		//MapObjects mapObjects = tileMap.mapObjs;
		MapObjects mapObjects = tileMap.mapCollisionObjs;
		
		MapObject intersectedObj = null;
		
		
		for (MapObject object : mapObjects){
		//for (MapObject object : townMap.mapObjs) {
			
			String type =(String) object.getProperties().get(Constants.MAP_OBJ_OBJECT_TYPE);
			if( type == null || type.isEmpty() ) {
				continue;
			}
			
			if (object instanceof TextureMapObject) {
                continue;
            }
			
            if (object instanceof RectangleMapObject) {

            	
            	RectangleMapObject mapObj = (RectangleMapObject)object;
            	
            	//String type = (String) mapObj.getProperties().get(Constants.MAP_OBJ_OBJECT_TYPE);
            	        	
            	Rectangle rect = mapObj.getRectangle();//Utilities.scaleRectangle(mapObj.getRectangle(), mapRenderer.getUnitScale());

            	
            	if( rect.overlaps(candidate.getBoundingRectangle())){
            		intersectedObj = object; 	
            		//If its a door, and you are overlapping, let candidate move through, and set the ID for the interactedDoor
            		if( type.equals(Constants.MAP_OBJ_DOOR)){
            			if(userOutside){
            				
            				prevDoor = rect;
            				interactedDoor = (String) mapObj.getProperties().get(Constants.MAP_OBJ_DOOR_ID);
            			}else{
            				exitedHouseId = tileMap.id;
            				interactedDoor = Constants.MAP_OUTSIDE_WORLD_PREFIX+ (String) mapObj.getProperties().get(Constants.MAP_OBJ_DOOR_ID);
            			}
                	}else{  //if it is not a door, its a regular collision object - don't let the player pass through
                		//interactedDoor = Constants.MAP_OBJ_DOOR_NONE;
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
            		
            		intersectedObj = object; 	

            	}
      	
            }
            else if (object instanceof PolylineMapObject) {
            	
            	Polyline polyline = ((PolylineMapObject)object).getPolyline();
            	
            	if( polyline.contains(candidate.getBoundingRectangle().x, candidate.getBoundingRectangle().y)){
            		candidate.setPosition(prevPosition.x, prevPosition.y);
            		moveCamera= false;
            		/*String type = (String) object.getProperties().get("type");
        			System.out.println("Object type: " + type);	*/
            		
            		intersectedObj = object; 	

            	}
         	
            }
            else if (object instanceof CircleMapObject) {

            }
            else {
                continue;
            }
			
		}
		
		prevPosition.set( candidate.getX(), candidate.getY());
		/*
		 * if(intersectedObj!=null) { Gdx.app.log(getClass().getName(),
		 * intersectedObj.getName()); }
		 */		
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
			case Constants.CANDIDATE_MOVE_UP_KEY:
				//candidate.moveY(-Gdx.graphics.getDeltaTime() );				
				candidate.setMoveUp(true);
				break;
				
			case Constants.CANDIDATE_MOVE_DOWN_KEY:
				//candidate.moveX(-Gdx.graphics.getDeltaTime() );
				candidate.setMoveDown(true);
				break;
			
			case Constants.CANDIDATE_MOVE_LEFT_KEY:
				//candidate.moveX(Gdx.graphics.getDeltaTime() );
				candidate.setMoveLeft(true);
				break;	
			
			case Constants.CANDIDATE_MOVE_RIGHT_KEY:
				//candidate.moveX(-Gdx.graphics.getDeltaTime() );
				candidate.setMoveRight(true);
				break;	
			case Constants.KEY_DEBUG:
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
			case Constants.CANDIDATE_MOVE_UP_KEY:
				candidate.setMoveUp(false);
				break;
				
				
			case Constants.CANDIDATE_MOVE_DOWN_KEY:
				candidate.setMoveDown(false);
				break;
			
			case Constants.CANDIDATE_MOVE_LEFT_KEY:
				candidate.setMoveLeft(false);
				break;	
			
			case Constants.CANDIDATE_MOVE_RIGHT_KEY:
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
		
		interactBtn = true;
		
		if(interactBtn ){

			
			if( !Constants.MAP_OBJ_DOOR_NONE.equals( interactedDoor)){
				interactBtn = true;
				
				if( ElectionGame.GAME_OBJ.mapHandler.getMap(interactedDoor) == null) {
					return;
				}
				
				loadMap(interactedDoor, 1f/128f);
				worldCam.setLookAt(candidate.getX(), candidate.getY());
				return;
			}else{
				interactBtn = false;
			}
			
			if( interactedElector != null){
				interactBtn = true;
				//TODO: later check if the person has no dialog, or has some metadata indicating they 
				//are battleable, and then engage in battle, otherwise engage in dialog
				ElectionGame.GAME_OBJ.state = GameState.BATTLE;	
				
				/*
				 * TODO: uncomment this after testing battle screen is done, so you can engage in dialog.
				
				ElectionGame.GAME_OBJ.state = GameState.DIALOG;			
				Gdx.input.setInputProcessor(ElectionGame.GAME_OBJ.dialogHandler);
				*/
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
