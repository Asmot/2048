package com.theosirian.twokfortyeight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Game game;
	private TextView child, tv;
	private RelativeLayout board;
	private int[][] gameState;
	private static int current = -1;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 150;
	private static final int SWIPE_THRESHOLD_VELOCITY = 150;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {

			gameState = new int[4][4];

			for (int i = 0; i < gameState.length; i++) {

				for (int j = 0; j < gameState[i].length; j++) {

					gameState[i][j] = savedInstanceState.getInt("gs" + i + j);

				}

			}

			game = new Game(savedInstanceState.getInt("score"), savedInstanceState.getInt("time"), gameState);

			board = (RelativeLayout) findViewById(R.id.board);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			for (int i = 0; i < game.getGameState().length; i++) {

				for (int j = 0; j < game.getGameState()[i].length; j++) {

					child = (TextView) inflater.inflate(R.layout.text_view, null);

					board.addView(child);

					tv = (TextView) findViewById(R.id.text_view);
					tv.setId(nextId());
					if (i == 0 && j == 0) {

						tv.addRule(RelativeLayout.ALIGN_PARENT_TOP, true);
						tv.addRule(RelativeLayout.ALIGN_PARENT_LEFT, true);
						
					} else if ((i * 4) + j + 1 > 4 && j != 0){

						tv.addRule(RelativeLayout.BELOW, tv.getId() - 4);
						tv.addRule(RelativeLayout.RIGHT_OF, tv.getId() - 1);
						
					} else if ((i * 4) + j + 1 > 4 && j == 0){
						
						tv.addRule(RelativeLayout.BELOW, tv.getId() - 4);
						
					}

				}

			}

			updateTextView();

		} else {

			game = new Game();

			board = (RelativeLayout) findViewById(R.id.board);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			for (int i = 0; i < game.getGameState().length; i++) {

				for (int j = 0; j < game.getGameState()[i].length; j++) {

					child = (TextView) inflater.inflate(R.layout.text_view, null);

					board.addView(child);

					tv = (TextView) findViewById(R.id.text_view);
					tv.setId(nextId());

				}

			}

			updateTextView();

		}

	}

	private void resetId() {

		current = -1;

	}

	private int nextId() {

		current += 1;
		return current;

	}

	private int currentId() {

		return current;

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		gameState = game.getGameState();

		for (int i = 0; i < gameState.length; i++) {

			for (int j = 0; j < gameState[i].length; j++) {

				savedInstanceState.putInt("gs" + i + j, gameState[i][j]);

			}

		}

		savedInstanceState.putInt("score", game.getScore());
		savedInstanceState.putInt("time", game.getTime());

	}

	class MyGestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 150;

		private static final int SWIPE_MAX_OFF_PATH = 100;

		private static final int SWIPE_THRESHOLD_VELOCITY = 100;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			float dX = e2.getX() - e1.getX();

			float dY = e1.getY() - e2.getY();

			if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY
					&& Math.abs(dX) >= SWIPE_MIN_DISTANCE && !game.isLost()) {

				if (dX > 0) {

					game.onRightSwipe();
					updateTextView();

				} else {

					game.onLeftSwipe();
					updateTextView();

				}

				return true;

			} else if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY
					&& Math.abs(dY) >= SWIPE_MIN_DISTANCE && !game.isLost()) {

				if (dY > 0) {

					game.onUpSwipe();
					updateTextView();

				} else {

					game.onDownSwipe();
					updateTextView();

				}

				return true;

			}

			return false;

		}

	}

	private void updateTextView() {

		gameState = game.getGameState();

		for (int i = 0; i < gameState.length; i++) {

			for (int j = 0; j < gameState[i].length; j++) {

				tv = (TextView) findViewById((i * 4) + j + 1);
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		switch (paramMenuItem.getItemId()) {
		default:
			return super.onOptionsItemSelected(paramMenuItem);
		case R.id.action_restart:

			return true;
		case R.id.action_settings:
			//startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.action_quit:
			showQuitDialog();
			return true;
		}
	}

	public void showQuitDialog() {
		new AlertDialog.Builder(this).setTitle("Quit").setMessage("Do you really want to quit?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int which) {
						finish();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int which) {
						di.dismiss();
					}
				}).show();
	}

	public void showWinDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Congratulations!")
				.setMessage(
						"You've finished the game, your time is " + game.getTimeF() + ".\nDo you wish to play again?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int which) {
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int which) {
						finish();
					}
				}).show();
	}

}
