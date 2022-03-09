package com.ahad.chess.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ahad.chess.engine.pieces.Piece;

public abstract class Tile {
	
	protected final int tileCoordinate;
	
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = creaAllPossibleEmptyTiles();
	
	private static Map<Integer, EmptyTile> creaAllPossibleEmptyTiles() {
		
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		
		return Collections.unmodifiableMap(emptyTileMap);
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	
	private Tile(int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	public static final class EmptyTile extends Tile{
		
		private EmptyTile(final int coordinate) {
			super(coordinate);
		}
		
		@Override
		public String toString() {
			return "-";
		}
		
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		
		@Override
		public Piece getPiece() {
			return null;
		}
	}
	
	public static final class OccupiedTile extends Tile {
		
		private final Piece pieceOntTile;
		
		private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
			
			super(tileCoordinate);
			this.pieceOntTile = pieceOnTile;
			
		}
		
		@Override
		public String toString() {
			return getPiece().getPieceAllience().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
		}
		
		@Override
		public boolean isTileOccupied() {
			return true;
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOntTile;
		}
	}

}
