package com.theosirian.twokfortyeight;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

	private int[][] game;
	private Random rand;
	private int time, score, noMove;
	private Timer timer;
	private boolean gameLost = false, gameWon = false;

	public Game() {

		rand = new Random();
		score = 0;
		time = 0;
		game = new int[4][4];
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				incTime(1);
			}
		}, 0, 1000);
		nextClearSpace(rand);

	}

	public Game(int scoreR, int timeR, int[][] gameState) {

		rand = new Random();
		score = scoreR;
		time = timeR;
		game = gameState;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				incTime(1);
			}
		}, 0, 1000);
		nextClearSpace(rand);

	}

	public void onUpSwipe() {

		noMove = 0;
		int max = 0;

		for (int i = 0; i < game.length - 1; i++) {

			for (int j = 0; j < game[i].length - 1; j++) {

				if (game[i][j] > 0 && game[i][j] == game[i][j + 1]) {

					game[i][j] *= 2;
					game[i][j + 1] = 0;
					if (game[i][j] == 2048) {

						setWon();

					}

				} else if (game[i][j] == 0) {

					for (int k = j; k < game[i].length - 1; k++) {

						game[i][3 - k - 1] = game[i][3 - k];
						game[i][3 - k] = 0;

					}

					if (max < game.length - 1) {
						j--;
						max++;
					} else {
						max = 0;
						break;
					}

				} else {

					noMove += 1;

				}

			}

		}

		if (noMove == 4) {

			setLost();

		}

		nextClearSpace(rand);
	}

	public void onDownSwipe() {

		noMove = 0;
		int max = 0;

		for (int i = 0; i < game.length - 1; i++) {

			for (int j = 0; j < game[i].length - 1; j++) {

				if (game[i][3 - j] > 0 && game[i][3 - j] == game[i][3 - j + 1]) {

					game[i][3 - j] *= 2;
					game[i][3 - j + 1] = 0;
					if (game[i][3 - j] == 2048) {

						setWon();

					}

				} else if (game[i][3 - j] == 0) {

					for (int k = j; k < game[i].length - 1; k++) {

						game[i][k - 1] = game[i][k];
						game[i][k] = 0;

					}

					if (max < game[i].length - 1) {
						j--;
						max++;
					} else {
						max = 0;
						break;
					}

				} else {

					noMove += 1;

				}

			}

		}

		if (noMove == 4) {

			setLost();

		}

		nextClearSpace(rand);
	}

	public void onLeftSwipe() {

		noMove = 0;
		int max = 0;

		for (int i = 0; i < game.length - 1; i++) {

			for (int j = 0; j < game[i].length - 1; j++) {

				if (game[3 - j][i] > 0 && game[3 - j][i] == game[3 - j + 1][i]) {

					game[3 - j][i] *= 2;
					game[3 - j + 1][i] = 0;
					if (game[3 - j][i] == 2048) {

						setWon();

					}

				} else if (game[3 - j][i] == 0) {

					for (int k = j; k < game[i].length - 1; k++) {

						game[k - 1][i] = game[k][i];
						game[k][i] = 0;

					}

					if (max < game[i].length - 1) {
						j--;
						max++;
					} else {
						max = 0;
						break;
					}

				} else {

					noMove += 1;

				}

			}

		}

		if (noMove == 4) {

			setLost();

		}

		nextClearSpace(rand);
	}

	public void onRightSwipe() {

		noMove = 0;
		int max = 0;

		for (int i = 0; i < game.length - 1; i++) {

			for (int j = 0; j < game[i].length - 1; j++) {

				if (game[j][i] > 0 && game[j][i] == game[j + 1][i]) {

					game[j][i] *= 2;
					game[j + 1][i] = 0;
					if (game[j][i] == 2048) {

						setWon();

					}

				} else if (game[j][i] == 0) {

					for (int k = j; k < game[i].length - 1; k++) {

						game[3 - k - 1][i] = game[3 - k][i];
						game[3 - k][i] = 0;

					}

					if (max < game.length - 1) {
						j--;
						max++;
					} else {
						max = 0;
						break;
					}

				} else {

					noMove += 1;

				}

			}

		}

		if (noMove == 4) {

			setLost();

		}

		nextClearSpace(rand);
	}

	private void nextClearSpace(Random random) {

		int i = random.nextInt(4), j = random.nextInt(4);

		if (game[i][j] == 0) {

			game[i][j] = 2;

		} else {

			for (int k = 0; k < game.length; k++) {

				for (int l = 0; l < game.length; l++) {

					if (game[i][j] == 0) {

						nextClearSpace(random);
						return;

					}

				}

			}

			setLost();

		}

	}

	private void incTime(int increment) {

		time += increment;

	}

	public int getTime() {

		return time;

	}

	public String getTimeF() {

		final int hr = (time / 3600);

		final int rem = (time % 3600);

		final int mn = rem / 60;

		final int sec = rem % 60;

		String hrStr = (hr < 10 ? "0" : "") + hr;
		String mnStr = (mn < 10 ? "0" : "") + mn;
		String secStr = (sec < 10 ? "0" : "") + sec;

		return hrStr + "h" + mnStr + "m" + secStr + "s";

	}

	private void incScore(int increment) {

		score += increment;

	}

	public int getScore() {

		return score;

	}

	public int[][] getGameState() {

		return game;

	}

	private void setLost() {

		gameLost = true;

	}

	private void setWon() {

		gameWon = true;

	}

	public boolean isLost() {

		return gameLost;

	}

	public boolean isWon() {

		return gameWon;

	}
}
