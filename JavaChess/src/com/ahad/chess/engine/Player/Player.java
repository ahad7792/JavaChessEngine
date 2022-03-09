package com.ahad.chess.engine.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.board.Board;
import com.ahad.chess.engine.board.Move;
import com.ahad.chess.engine.board.MoveStatus;
import com.ahad.chess.engine.board.MoveTransition;
import com.ahad.chess.engine.pieces.King;
import com.ahad.chess.engine.pieces.Piece;

public abstract class Player {
	
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	
	public Player(final Board board,
				  final Collection<Move> legalMoves,
				  final Collection<Move> opponentMoves) {
		
		this.board = board;
		this.playerKing = establishedKing();
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
		this.legalMoves = Collections.unmodifiableCollection(Stream.concat(legalMoves.stream(), calculateKingCastles(legalMoves, opponentMoves).stream()).collect(Collectors.toList()));
	}
	
	protected static Collection<Move> calculateAttacksOnTile(Integer piecePosition, Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves) {
			if(piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return Collections.unmodifiableCollection(attackMoves);
	}
	
	public Collection<Move> getLegalMoves(){
		return this.legalMoves;
	}

	private Piece getPlayerKing() {
		return this.playerKing;
	}

	private King establishedKing() {
		for(final Piece piece : getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("Not a valid Board!");
	}
	
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isInStaleMate() {
		return !isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;
	}
	
	
	protected boolean hasEscapeMoves() {
		for(final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public MoveTransition makeMove(final Move move) {
		
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transitionBoard = move.execute();
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
			  transitionBoard.currentPlayer().getLegalMoves());
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);

}
