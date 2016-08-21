package com.election.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.election.game.ElectionGame.GameState;
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
	private boolean interactBtn;
	private Electorate interactedElector;

	
	
	public OutsideScreen(final ElectionGame gameObj) {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		
		camera = new OrthographicCameraMovementWrapper(false, w, h);
		//camera.source.setToOrtho(false, w, h);
		
		//main character
		candidate = new Candidate( new Texture(Gdx.files.internal("man.png")) );
		candidate.sprite.setPosition(w/2, h/2);
		prevPosition = new Vector2(candidate.sprite.getX(), candidate.sprite.getY());
		
		//anotherDude = new Texture(Gdx.files.internal("man.png"));
		
				
		//float unitScale = 1 / 1f;
		//townMap = new TownMap("town.tmx");
		townMap = 	new TmxMapLoader().load("town.tmx");
		mapRenderer = new SpriteAndTiledRenderer(townMap, camera.source);
		
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
			updateDialog(delta);
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
	}

	private void updatePaused(float delta){
		Gdx.gl.glClearColor(.3f, .2f, .4f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 

		//camera.source.update();
	}
	
	private void updateDialog(float delta) {
		
	}

	private void checkCollisions(float delta) {

						
		checkMapCollisions(delta);
		
		checkCameraBounds(delta );
		
		checkElectorCollisions(delta);
	
	}
	
	//see if candidate is intersecting any of the electors
	private void checkElectorCollisions(float delta) {

		Region region = getRegion((int)candidate.sprite.getX(), (int)candidate.sprite.getY());
		
		for (Electorate elector : region.electorsInRegion) {
			
			if( candidate.sprite.getBoundingRectangle().overlaps(elector.sprite.getBoundingRectangle())){
						
				elector.hit= true;
				Gdx.app.log(System.class.getName(), "Candidate intersects " + elector.id);
				
				if(interactBtn){
					
					interactedElector = elector;
					
				}else{
					interactedElector = null;
				}

			}else{
				elector.hit = false;
			}
	
		}
		
	}

	private void checkCameraBounds(float delta) {
		
		Rectangle candBound =  candidate.sprite.getBoundingRectangle();
		
		Rectangle movementRegion = camera.boundsRect;
		
		
		if( ! movementRegion.contains(candBound)){
									
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
		/*if( !gameSpace.contains(camera.cameraRect)){
			cameraAtMapEdge= true;
			camera.resetCamera();
		}
		
		
			
		//if candidate has gone into boundary region, start moving the camera and the character
		
		//if candidate's bounding rect top left point has gone above candidate boundary line (i.e., upper 80th percentile of viewport)
		if( candBound.getY()+candBound.height >= camera.source.position.y + (Constants.CAM_MOVE_SCREEN_PERCENTAGE * camera.source.viewportHeight)/2 ){
			
			if( !cameraAtMapEdge ){			
				moveCamera = true;
				//camera.setMoveUp( true );
			}
			
		}else if( candBound.getY() <= camera.source.position.y - (Constants.CAM_MOVE_SCREEN_PERCENTAGE * camera.source.viewportHeight)/2 ){
			
			if( !cameraAtMapEdge ){
				moveCamera = true;
				//camera.setMoveDown(true);
			}
			
			
		}else if( candBound.getX() +candBound.width >= camera.source.position.x + (Constants.CAM_MOVE_SCREEN_PERCENTAGE*camera.source.viewportWidth)/2  ){
				
			if( !cameraAtMapEdge ){
				moveCamera = true;
				//camera.setMoveRight(true);
			}
				
			
		}else if(   candBound.getX() <= camera.source.position.x - (Constants.CAM_MOVE_SCREEN_PERCENTAGE*camera.source.viewportWidth)/2 ){
			
			if( !cameraAtMapEdge ){
				moveCamera = true;
				//camera.setMoveLeft( true);
			}


		} else{
			cameraAtMapEdge = false;
			moveCamera = false;
		}
		
		

		
		//check to see if camera is within the game world
		if( camera.source.position.x - camera.source.viewportWidth/2 <  0 ){
			
			cameraAtMapEdge =true;
			camera.source.position.set( camera.source.viewportWidth/2 , camera.source.position.y, 0);
			//moveCamera=false;
			//Gdx.app.log(this.getClass().getSimpleName(), "Camera has moved too left");
		}else if( camera.source.position.x + camera.source.viewportWidth/2 > mapPixelWidth ){
			cameraAtMapEdge =true;
			camera.source.position.set(mapPixelWidth - camera.source.viewportWidth/2 , camera.source.position.y , 0);
			//moveCamera=false;
			//Gdx.app.log(this.getClass().getSimpleName(), "Camera has moved too right");
		}else if( camera.source.position.y + camera.source.viewportHeight/2 >  mapPixelHeight ){
			cameraAtMapEdge =true;
			camera.source.position.set(camera.source.position.x, mapPixelHeight - camera.source.viewportHeight/2, 0);
			//moveCamera=false;
			//Gdx.app.log(this.getClass().getSimpleName(), "Camera has moved too high");
		}else if( camera.source.position.y  - camera.source.viewportHeight/2 <  0){
			cameraAtMapEdge =true;
			camera.source.position.set(camera.source.position.x, camera.source.viewportHeight/2, 0);
			//moveCamera=false;
			//Gdx.app.log(this.getClass().getSimpleName(), "Camera has moved low");
		}
		
		*/
		
	}

	
	
	private void updateCandidate(float delta) {
        
	
		
		//if the candidate has reached edge boundary of viewport, 
		//allow camera to move up/left/right/down to move viewport by calling its update method
		//otherwise only candidate should move position, so call candidate update method
		//if(camera.isMovable()){
		/*if(moveCamera){
			camera.update(delta);
			mapRenderer.setView(camera.source);		
			candidate.update(delta);
		}else{*/
			candidate.update(delta);
		//}
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

	
	
	private void renderSprites(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 

		camera.source.update();
		mapRenderer.setView(camera.source);		
		mapRenderer.render();

		
		DebugRenderer.DrawDebugCameraScrollBounds( camera);
		
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
				Gdx.app.log(Class.class.getName(), "Candidate Box Position: [" + candidate.sprite.getBoundingRectangle().getX() + ", " + candidate.sprite.getBoundingRectangle().getY() + "]");
				Gdx.app.log(Class.class.getName(), "Camera Position: [" + camera.source.position.x + ", " + camera.source.position.y + "]");
				ElectionGame.GAME_OBJ.isdebug = !ElectionGame.GAME_OBJ.isdebug;
				break;		
		}
		
		//after you keydown to move in any direction, 
		//first set the camera movement to false, 
		//so the character can move around without the camera moving around (in the opposite direction)
		//each render cycle will call the border check to determine if the 
		//character has moved to a particular point in the screen to scroll the map
		/*if( prevKeyCode == keycode){
			moveCamera = true;
		}else{*/
			//moveCamera = false;
		//}
		
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
				interactBtn = true;
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
		
		
		//moveCamera = false;

		return true;
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
