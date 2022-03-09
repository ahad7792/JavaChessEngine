package com.ahad.chess.engine.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
	
	public final static boolean[] FIRST_COLUMN = initColumn(0);
    public final static boolean[] SECOND_COLUMN = initColumn(1);
    public final static boolean[] THIRD_COLUMN = initColumn(2);
    public final static boolean[] FOURTH_COLUMN = initColumn(3);
    public final static boolean[] FIFTH_COLUMN = initColumn(4);
    public final static boolean[] SIXTH_COLUMN = initColumn(5);
    public final static boolean[] SEVENTH_COLUMN = initColumn(6);
    public final static boolean[] EIGHTH_COLUMN = initColumn(7);
	
	public final static boolean[] EIGHTTH_RANK = initRow(0);
	public final static boolean[] SEVENTH_RANK = initRow(8);
	public final static boolean[] SIXTH_RANK = initRow(16);
	public final static boolean[] FIFTH_RANK = initRow(24);
	public final static boolean[] FOURTH_RANK = initRow(32);
	public final static boolean[] THIRD_RANK = initRow(40);
	public final static boolean[] SECOND_RANK = initRow(48);
	public final static boolean[] FIRST_RANK = initRow(56);
	
	public static final String[] ALGEBRIC_NOTATION = initializeAlgebricNotation();
	public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
	
	public final static int NUM_TILES = 64;
	final static int NUM_TILES_PER_ROW = 8;

	private BoardUtils() {
		throw new RuntimeException("You cannot instantiate me!");
	}
	
	private static String[] initializeAlgebricNotation() {
        String[] finalArray = new String[64];
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int count = 0;
        for(int x : numbers) {
            for (String y : letters) {
                finalArray[count] = y + x;
                        count++;
            }
        }
        return finalArray;
    }

	private static boolean[] initRow(int rowNumber) {
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[rowNumber] = true;
			rowNumber++; 
		} while(rowNumber % NUM_TILES_PER_ROW != 0);
		return row;
	}
	
	private static boolean[] initColumn(int columnNumber) {
		final boolean[] column = new boolean[NUM_TILES];
		do {
			column[columnNumber] = true;
			columnNumber += NUM_TILES_PER_ROW;
		} while (columnNumber < NUM_TILES);
		return column;
	}

	public static boolean isValidTileCoordinate(int coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILES;
	}
	
	public static int getCoordinateAtPosition(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	
	public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRIC_NOTATION[coordinate];
    }
	
	private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

}
