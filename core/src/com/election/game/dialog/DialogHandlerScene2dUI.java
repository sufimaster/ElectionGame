package com.election.game.dialog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class DialogHandlerScene2dUI extends Game{

	
	private static String longText = "testasdfsadsfasdfasdfasefawafweawfesdcviouahweoaw[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasdfweawfesdcviouahweoaw[q43rn2908fnpoinq0fawafweawfesdcviouahweoaw[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasdfsadsfasdfasdfasefawafweawfesdcviouahweoaw[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasdfsadsfasdfasdfasefawafweawfesdcviouahweoaw[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "q43rn2908fnpoinq0w398f4p394fnq3p9[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasdfsadsfasdfasdfasefawafweawfesdcviouahweoaw[rov3948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasdfsadsfasdfasdfasefawafweawfesdcviouahweoaw[q43rn2908fnpoinq0w398f4p394fnq3p9[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9" +
									 "testasd[q40mpvciundaoy420n78	q43rn2cviouahweoaw[q43rn2908fnpoinq0948jq09v5jnw8734j340qjm[q40mpvciundaoy420n78	q43rn2908fnpoinq0w398f4p394fnq3p9";
	
	
	
	private Stage stage;
	private Window dialogDisplay;
	
	private Window dialogSelection;
	Skin skin;
	
	float timeToDrawLetter = .3f;



	private float percentage;



	private Label label;



	private Dialog dialog;
	
	//public DialogHandlerScene2dUI(){
		
		//camera = new OrthographicCamera(Gdx.graphics.getWidth()/1.5f, Gdx.graphics.getHeight()/2);
		
		//create();
		
	//}
	
	
	public void create () {
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		Gdx.input.setInputProcessor(stage);

		createDialogDisplay();
		createDialogSelection();
			
	}

	private void createDialogDisplay() {
		dialogDisplay = new Window("Dialog", skin);
		dialogDisplay.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		//dialogDisplay.setDebug(true);
		dialogDisplay.setPosition(100, 100);
		
		stage.addActor(dialogDisplay);
				
		Table table = new Table();
		table.debug();
		table.center().left();		
		
		final ScrollPane scroll = new ScrollPane(table, skin);
		scroll.setFadeScrollBars(false);
		//scroll.setDebug(true);
		
		
		label = new Label(longText, skin);	
		label.setWrap(true);
		label.setFillParent(true);
		
		table.row();
		table.add( label);
		
		TextButton closeButton = new TextButton("X", skin);
		closeButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				closeDialog();
				
			}

			
		});
		
		dialogDisplay.getTitleTable().add(closeButton);
		
		
		dialogDisplay.add(scroll).expand().fill().colspan(4);
		dialogDisplay.row().space(10).padBottom(10);
		TextButton continueButton = new TextButton("Ok", skin);
		continueButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// change state to DIALOG_MAKING_SELECTION
				
			}
		});
		
		dialogDisplay.add(continueButton);
		
		
	}
	
	
	private void closeDialog() {
		dialogDisplay.remove();
		dialogSelection.remove();
		
	}
	
	
	public void createDialogSelection(){
		
		dialogSelection = new Window("Choices", skin);
		dialogSelection.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/2);
		dialogSelection.setDebug(false);
		dialogSelection.setPosition( 20 + dialogDisplay.getX() + dialogDisplay.getWidth(), 100);
		
		stage.addActor(dialogSelection);
				
		Table table = new Table();
		table.debug();
		//table.top().left();		
		//table.setHeight(stage.getHeight());

		final ScrollPane scroll = new ScrollPane(table, skin);		
		scroll.setFadeScrollBars(false);
		scroll.setDebug(true);
		
		Label choice1 = new Label("First Choice. dfhusfiuwheiorisguergiuhserguhergiuhueghetyr5eh5475h 766ruig6b75yvctq3 tw43qwt35 yt46y654 ye567h546 gh546rg", skin);			
		choice1.setWrap(true);
		choice1.setAlignment(Align.left);
		//choice1.setFillParent(true);
		
		Label choice2 = new Label("Second Choice.dfhusfiuwheiorisguergiuhserguhergiuhueghetyr5eh5475hdfhusfiuwheiorisguergiuhserguhergiuhueghetyr5eh5475hdfhusfiuwheiorisguergiuhserguhergiuhueghetyr5eh5475h", skin);			
		choice2.setWrap(true);
		choice2.setAlignment(Align.left);
		//choice2.setFillParent(true);
		
		Label choice3 = new Label("Third Choice. 98h9f8494j90q23j 98 q394fh3q94 th23943qgh46h37894tr3j809r  0394rj 0q394rj 0q394 j093q4 jr0934jr 0q394 jr", skin);			
		choice3.setWrap(true);
		choice3.setAlignment(Align.left);
		//choice3.setFillParent(true);
		
		Label choice4 = new Label("Fourth Choice.29	y0r9 4powkj[qK {PK 02P34W JR0T934 JT349JT0943WJ P0j  	-09pjpoj- j-e3p50j39t309", skin);			
		choice4.setWrap(true);
		choice4.setAlignment(Align.left);
		//choice4.setFillParent(true);
		
		
		
		//table.padTop(100);
		table.add( choice1).expandY().growX().top().left();
		table.row().pad(15);
		table.add( choice2).expandY().growX().top().left();
		table.row().pad(15);
		table.add( choice3).expandY().growX().top().left();
		table.row().pad(15);
		table.add( choice4).expandY().growX().top().left();
		
		dialogSelection.add(scroll).expand().fill().colspan(1);


	
	}
	
	public void render () {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		percentage = deltaTime/timeToDrawLetter;
		int length =(int) percentage * longText.length();
		//label.setText( longText.substring(0, length) );	
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);

		// Gdx.gl.glViewport(100, 100, width - 200, height - 200);
		// stage.setViewport(800, 600, false, 100, 100, width - 200, height - 200);
	}

	public void dispose () {
		stage.dispose();
	}

	public boolean needsGL20 () {
		return false;
	}
}
	

