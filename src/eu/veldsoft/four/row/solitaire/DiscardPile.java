/*
 This file is a part of Four Row Solitaire

 Copyright (C) 2010-2014 by Matt Stephen, Todor Balabanov, Konstantin Tsanov, Ventsislav Medarov

 Four Row Solitaire is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Four Row Solitaire is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with FourRowSolitaire.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.veldsoft.four.row.solitaire;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * Class: DiscardPile
 * 
 * Description: The DiscardPile class manages the stack of Cards discarded from
 * the deck.
 * 
 * @author Matt Stephen, Todor Balabanov, Konstantin Tsanov, Ventsislav Medarov
 */
public class DiscardPile extends CardStack {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Cards left from the last draw from the deal deck.
	 */
	private int cardsLeftFromDraw = 0;

	/**
	 * Returns the cards left from the last draw from the deal deck.
	 * 
	 * @return
	 */
	public int getNumViewableCards() {
		return cardsLeftFromDraw;
	}

	/**
	 * Sets the number of cards left from the last draw from the deck.
	 * 
	 * @param numViewableCards
	 */
	public void setView(int numViewableCards) {
		cardsLeftFromDraw = numViewableCards;
	}

	/**
	 * Adds a card to the pile of currently viewable cards.
	 * 
	 * @param card
	 */
	public void addCard(Card card) {
		cardsLeftFromDraw++;
		super.addCard(card);
	}

	/**
	 * Adds stack of cards to the pile of currently viewable cards.
	 * 
	 * @param stack
	 */
	public void addStack(CardStack stack) {
		for (int i = stack.length(); i > 0; i--) {
			Card card = stack.pop();
			addCard(card);
		}
	}

	/**
	 * Adds a card to the pile of currently viewable cards and returns the card
	 * added.
	 * 
	 * @param card
	 * 
	 * @return
	 */
	public Card push(Card card) {
		if (SolitaireBoard.drawCount == 1) {
			cardsLeftFromDraw = 0;
		}

		addCard(card);
		card.setSource("");
		return card;
	}

	/**
	 * Adds a stack of cards to the pile of currently viewable cards and returns
	 * the cards added.
	 * 
	 * @param stack
	 * 
	 * @return
	 */
	public CardStack push(CardStack stack) {
		if (SolitaireBoard.drawCount != 1
				|| (SolitaireBoard.drawCount == 1 && stack.length() == 1)) {
			cardsLeftFromDraw = 0;

			while (stack.isEmpty() == false) {
				push(stack.pop());
			}
		}

		return stack;
	}

	/**
	 * Pops cards out of the stack of viewable cards.
	 * 
	 * @return
	 */
	public synchronized Card pop() {
		Card card = super.pop();

		/*
		 * To make the display of multiple cards correct (After a player removes
		 * the top card of draw 3, it shouldn't display the top 3 cards).
		 */
		if (cardsLeftFromDraw > 0) {
			cardsLeftFromDraw--;
		} else {
			cardsLeftFromDraw = 0;
		}

		return card;
	}

	/**
	 * Used to undo the pop.
	 * 
	 * @return
	 */
	public synchronized Card undoPop() {
		return super.pop();
	}

	/**
	 * Returns a card located at the position of a mouse click.
	 * 
	 * @param p
	 * 
	 * @return
	 */
	public Card getCardAtLocation(Point p) {
		return peek();
	}

	/**
	 * Checks if a card move is valid.
	 * 
	 * @param card
	 * 
	 * @return
	 */
	public boolean isValidMove(Card card) {
		if (card.getSource().equals("Deck")) {
			return true;
		}

		return false;
	}

	/**
	 * The stack moves are always false.
	 * 
	 * @param stack
	 * 
	 * @return
	 */
	public boolean isValidMove(CardStack stack) {
		return false;
	}

	/**
	 * Paint procedure.
	 * 
	 * @param g
	 *            Graphic context.
	 */
	public void paint(Graphics g) {
		super.paint(g);

		if (isEmpty() == false && SolitaireBoard.drawCount == 1) {
			for (int i = 0; i < length(); i++) {
				Image image = getCardAtLocation(i).getImage();
				g.drawImage(image, 0, 0, null);
			}
		} else if (isEmpty() == false && SolitaireBoard.drawCount == 3) {
			if (cardsLeftFromDraw > 0) {
				for (int i = 0; i < length() - cardsLeftFromDraw + 1; i++) {
					Image image = getCardAtLocation(i).getImage();
					g.drawImage(image, 0, 0, null);
				}

				for (int i = length() - cardsLeftFromDraw + 1; i < length(); i++) {
					Image image = getCardAtLocation(i).getImage();

					if ((cardsLeftFromDraw == 3 && i == length() - 2)
							|| (cardsLeftFromDraw == 2 && i == length() - 1)) {
						g.drawImage(image, 15, 0, null);
					} else if (cardsLeftFromDraw == 3 && i == length() - 1) {
						g.drawImage(image, 30, 0, null);
					}
				}
			} else {
				for (int i = 0; i < length(); i++) {
					Image image = getCardAtLocation(i).getImage();
					g.drawImage(image, 0, 0, null);
				}
			}
		}
	}

	/**
	 * Returns the stack of available cards.
	 * 
	 * @return
	 */
	public CardStack getAvailableCards() {
		if (isEmpty() == true) {
			return (null);
		}

		CardStack stack = new CardStack();
		stack.addCard(peek());

		return stack;
	}
}
