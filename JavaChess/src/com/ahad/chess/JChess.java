package com.ahad.chess;

import com.ahad.chess.engine.board.Board;
import com.ahad.chess.gui.Table;

public class JChess {

	public static void main(String[] args) {
		
		Board board = Board.createStandardBoard();
		
		System.out.println(board);

		Table table = new Table();
	}

}
