package com.ahad.chess.engine.pieces;

import java.util.Collection;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.board.Board;
import com.ahad.chess.engine.board.Move;

public abstract class Piece {	
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cachedHashedCode;
	
	Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		this.pieceType = pieceType;
		this.pieceAlliance = pieceAlliance;
		this.piecePosition = piecePosition;
		this.isFirstMove = isFirstMove;
		this.cachedHashedCode = cachedHashedCode();
	}
	
	private int cachedHashedCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
			   pieceAlliance == otherPiece.getPieceAllience() && isFirstMove == otherPiece.isFirstMove(); 
	}
	
	@Override
	public int hashCode() {
		return this.cachedHashedCode;
	}
	
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public abstract Piece movePiece(Move move);
	
	public PieceType getPieceType() {
		return this.pieceType;
	}

	public Alliance getPieceAllience() {
		return this.pieceAlliance;
	}

	public Integer getPiecePosition() {
		return this.piecePosition;
	}
	
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	public enum PieceType {
		
		PAWN("P", 100) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N", 300) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B", 330) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R", 500) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q", 900) {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K", 10000) {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue;
		
		private PieceType(String pieceName, final int pieceValue) {
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public int getPieceValue() {
			return this.pieceValue;
		}
		
		public abstract boolean isKing();

		public abstract boolean isRook();
	}
	
}
