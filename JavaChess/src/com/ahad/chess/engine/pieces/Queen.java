package com.ahad.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.board.Board;
import com.ahad.chess.engine.board.BoardUtils;
import com.ahad.chess.engine.board.Move;
import com.ahad.chess.engine.board.Tile;
import com.ahad.chess.engine.board.Move.AttackMove;
import com.ahad.chess.engine.board.Move.MajorMove;
import com.ahad.chess.engine.pieces.Piece.PieceType;

public class Queen extends Piece{
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

	public Queen(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
	}
	public Queen(Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {
		super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
	}
	

	@Override
    public Collection<Move> calculateLegalMoves(final Board board) {
		
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate) ||
                    isEighthColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAllience();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
	private static boolean isFirstColumnExclusion(final int currentPosition, final int canditateOffset) {
    	return BoardUtils.FIRST_COLUMN[canditateOffset] &&
    			(canditateOffset == -1 || canditateOffset == -9 || canditateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int canditateOffset) {
    	return BoardUtils.EIGHTH_COLUMN[canditateOffset] &&
    			(currentPosition == -7 || canditateOffset == 1 || canditateOffset == 9);
    }
    
    @Override
    public String toString() {
    	return PieceType.QUEEN.toString();
    }
    
    @Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getMovedPiece().getPieceAllience(), move.getDestinationCoordinate());
	}
    
}