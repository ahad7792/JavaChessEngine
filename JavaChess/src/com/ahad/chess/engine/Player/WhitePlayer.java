package com.ahad.chess.engine.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.board.Board;
import com.ahad.chess.engine.board.Move;
import com.ahad.chess.engine.board.Move.*;
import com.ahad.chess.engine.board.Tile;
import com.ahad.chess.engine.pieces.Piece;
import com.ahad.chess.engine.pieces.Rook;

public class WhitePlayer extends Player{

	public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
										  final Collection<Move> blackStandardLegalMoves) {

		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			//whites King side castle
			if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(61,  opponentLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(62,  opponentLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()){
						kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
					}
				}
			}
			if(!this.board.getTile(59).isTileOccupied() &&
			   !this.board.getTile(58).isTileOccupied() &&
			   !this.board.getTile(57).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(56);
				if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
				   Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
				   Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
				   rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
				}
			}
		}
		return Collections.unmodifiableCollection(kingCastles);
	}

}
