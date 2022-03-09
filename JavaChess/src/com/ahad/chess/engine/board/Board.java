package com.ahad.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.loading.PrivateClassLoader;

import org.junit.validator.PublicClassValidator;

import com.ahad.chess.engine.Alliance;
import com.ahad.chess.engine.Player.BlackPlayer;
import com.ahad.chess.engine.Player.Player;
import com.ahad.chess.engine.Player.WhitePlayer;
import com.ahad.chess.engine.pieces.Bishop;
import com.ahad.chess.engine.pieces.King;
import com.ahad.chess.engine.pieces.Knight;
import com.ahad.chess.engine.pieces.Pawn;
import com.ahad.chess.engine.pieces.Piece;
import com.ahad.chess.engine.pieces.Queen;
import com.ahad.chess.engine.pieces.Rook;

public class Board {
	
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	
	private final Pawn enPassantPawn;
	
	public Board (final Builder builder) {
		this.gameBoard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
		this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	public Player blackPlayer() {
		return this.blackPlayer;
	}

	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return Collections.unmodifiableCollection(legalMoves);
	}

	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();
		
		for(final Tile tile : gameBoard) {
			if(tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.getPieceAllience() == alliance) {
					activePieces.add(piece);
				}
			}
		}
		return Collections.unmodifiableCollection(activePieces);
	}

	private static List<Tile> createGameBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return List.of(tiles);
	}

	public Tile getTile(int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	
    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
    }
	
    public Collection<Move> getAllLegalMoves(){
    	return Collections.unmodifiableCollection(Stream.concat(this.whitePlayer.getLegalMoves().stream(), 
    						 									this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList()));
    }
    
	public static class Builder {
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder () {
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece (Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
			
		}
	}

}
