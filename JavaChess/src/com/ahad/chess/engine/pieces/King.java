package com.ahad.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.board.Board;
import com.ahad.chess.engine.board.BoardUtils;
import com.ahad.chess.engine.board.Move;
import com.ahad.chess.engine.board.Move.MajorMove;
import com.ahad.chess.engine.pieces.Piece.PieceType;
import com.ahad.chess.engine.board.Move.AttackMove;
import com.ahad.chess.engine.board.Tile;

public class King extends Piece{
	
	private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

	public King(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.KING, piecePosition, pieceAlliance, true);
	}
	public King(Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {
		super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {

		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
			   isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if(!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else{
					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieAlliance = pieceAtDestination.getPieceAllience();
					if(this.pieceAlliance != pieAlliance) {
						legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
					}
				}
			}
		}
		
		return Collections.unmodifiableCollection(legalMoves);
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
	}
	
	@Override
    public String toString() {
    	return PieceType.KING.toString();
    }
    
	@Override
	public King movePiece(final Move move) {
		return new King(move.getMovedPiece().getPieceAllience(), move.getDestinationCoordinate());
	}


}
