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

public class Pawn extends Piece{
	
	private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
	}
	
	public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAllience().getDirection() * currentCandidateOffset);
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				
				if(this.pieceAlliance.isPawnPromotionSuare(candidateDestinationCoordinate)) {
					legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
				} else{
					legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 16 && this.isFirstMove() && 
					((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAllience().isBlack()) || 
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAllience().isWhite()))) {
				
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAllience().getDirection() * 8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
				   !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 7 &&
					  !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
						(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance != pieceOnCandidate.getPieceAllience()) {
						if(this.pieceAlliance.isPawnPromotionSuare(candidateDestinationCoordinate)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAllience()) {
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
			} else if(currentCandidateOffset == 9 &&
					  !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
					   (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance != pieceOnCandidate.getPieceAllience()) {
						if(this.pieceAlliance.isPawnPromotionSuare(candidateDestinationCoordinate)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
						} else {
							legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					} 
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAllience()) {
							legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
			}
			
		}
		
		return Collections.unmodifiableCollection(legalMoves);
	}
	
	@Override
    public String toString() {
    	return PieceType.PAWN.toString();
    }
	
	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getMovedPiece().getPieceAllience(), move.getDestinationCoordinate());
	}
	
	public Piece getPromotionPiece() {
		return new Queen(this.pieceAlliance, this.piecePosition, false);
	}
    
}
