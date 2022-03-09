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
import com.ahad.chess.engine.board.Move.MajorAttackMove;
import com.ahad.chess.engine.board.Move.MajorMove;
import com.ahad.chess.engine.pieces.Piece.PieceType;

public class Rook extends Piece{

	public Rook( Alliance pieceAlliance, int piecePosition) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, true);
	}
	
	public Rook(final Alliance pieAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.ROOK, piecePosition, pieAlliance, isFirstMove);
	}
	
	private final static int[] CANDIDATE_MOVE_COORDINATES = {-8, -1, 1, 8};

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                    isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                	final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    else {

                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAllience();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int canditateOffset) {
    	return (BoardUtils.FIRST_COLUMN[currentPosition] &&
    			(canditateOffset == -1));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int canditateOffset) {
    	return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
    			(canditateOffset == 1);
    }
    
    @Override
    public String toString() {
    	return PieceType.ROOK.toString();
    }
    
    @Override
	public Rook movePiece(final Move move) {
		return new Rook(move.getMovedPiece().getPieceAllience(), move.getDestinationCoordinate());
	}

}
