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
import com.ahad.chess.engine.board.Move.*;
import com.ahad.chess.engine.board.MoveStatus;

public final class Bishop extends Piece {

    public Bishop(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance, true);
	}
    public Bishop(Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
	}
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
	
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                    isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)) {
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
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
    	return (BoardUtils.FIRST_COLUMN[currentPosition] &&
    			((candidateOffset == -9) || (candidateOffset == 7)));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
    	return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
    			((candidateOffset == -7) || (candidateOffset == 9));
    }
    
    @Override
    public String toString() {
    	return PieceType.BISHOP.toString();
    }

	@Override
	public Bishop movePiece(final Move move) {
		return new Bishop(move.getMovedPiece().getPieceAllience(), move.getDestinationCoordinate());
	}
    
}
