package com.ahad.chess.engine;

import com.ahad.chess.engine.Player.BlackPlayer;
import com.ahad.chess.engine.Player.Player;
import com.ahad.chess.engine.Player.WhitePlayer;
import com.ahad.chess.engine.board.BoardUtils;

public enum Alliance {
	
	WHITE {
		@Override
		public int getDirection() {
			return -1;
		}

		@Override
		public boolean isWhite() {
			return true;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, 
								   final BlackPlayer blackPlayer) {
			return whitePlayer;
		}

		@Override
		public int getOppositeDirection() {
			return 1;
		}

		@Override
		public boolean isPawnPromotionSuare(int position) {
			return BoardUtils.FIRST_RANK[position];
		}
	},
	BLACK {
		@Override
		public int getDirection() {
			return 1;
		}

		@Override
		public boolean isWhite() {
			return false;
		}

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, 
								   final BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public int getOppositeDirection() {
			return -1;
		}

		@Override
		public boolean isPawnPromotionSuare(int position) {
			return BoardUtils.EIGHTTH_RANK[position];
		}
	};
	
	public abstract int getDirection();
	public abstract int getOppositeDirection();
	public abstract boolean isWhite();
	public abstract boolean isBlack();
	public abstract boolean isPawnPromotionSuare(int position);
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);

}
