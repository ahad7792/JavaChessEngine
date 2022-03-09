package com.ahad.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.ahad.chess.engine.board.Move;
import com.ahad.chess.engine.pieces.Piece;
import com.ahad.chess.gui.Table.MoveLog;

public class TakenPiecesPanel extends JPanel{
	
	private final JPanel northPanel;
	private final JPanel southPanel;
	
	private static final Color PANEL_COLOR = Color.decode("0xFDFE6");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	
	public TakenPiecesPanel() {
		super(new BorderLayout());
		this.setBackground(PANEL_COLOR);
		this.setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8, 2));
		this.southPanel = new JPanel(new GridLayout(8, 2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
		
	}
	
	public void redo(final MoveLog moveLog) {
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		
		final List<Piece> whiteTakePieces = new ArrayList<>();
		final List<Piece> blackTakerPieces = new ArrayList<>();
		
		for(final Move move : moveLog.getMoves()) {
			if(move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if(takenPiece.getPieceAllience().isWhite()) {
					whiteTakePieces.add(takenPiece);
				} else if(takenPiece.getPieceAllience().isBlack()) {
					blackTakerPieces.add(takenPiece);
				} else {
					throw new RuntimeException("Should not reach here!");
				}
			}
		}
		
		Collections.sort(whiteTakePieces, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
			}
			
		});
		
		Collections.sort(blackTakerPieces, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
			}
			
		});
		
		for(final Piece takePiece : whiteTakePieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("art/" + 
														takePiece.getPieceAllience().toString().substring(0,1) + "" +
														takePiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.southPanel.add(imageLabel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(final Piece takePiece : blackTakerPieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("art/" + 
														takePiece.getPieceAllience().toString().substring(0,1) + "" +
														takePiece.toString()));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel();
				this.southPanel.add(imageLabel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		validate();
	}

}
