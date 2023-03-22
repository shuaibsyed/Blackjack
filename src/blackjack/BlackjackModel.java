package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import deckOfCards.*;

public class BlackjackModel {

	//Instance Variables
	private ArrayList<Card> dealer;
	private ArrayList<Card> player;
	private Deck deck;

	//Getters
	public ArrayList<Card> getDealerCards() {
		ArrayList<Card> copy = new ArrayList<Card>(this.dealer);
		return copy;
	}

	public ArrayList<Card> getPlayerCards() {
		ArrayList<Card> copy = new ArrayList<Card>(this.player);
		return copy;
	}

	//Setters
	public void setDealerCards(ArrayList<Card> cards) {
		this.dealer = new ArrayList<Card>(cards);
	}

	public void setPlayerCards(ArrayList<Card> cards) {
		this.player = new ArrayList<Card>(cards);
	}

	/* This method creates a new deck and then shuffles it */
	public void createAndShuffleDeck(Random random) {
		this.deck = new Deck();
		this.deck.shuffle(random);
	}

	/* This method deals the 2 initial cards to the dealer. */
	public void initialDealerCards() {
		this.dealer = new ArrayList<Card>();
		this.dealer.add(deck.dealOneCard());
		this.dealer.add(deck.dealOneCard());
	}

	/* This method deals the 2 initial cards to the player */
	public void initialPlayerCards() {
		this.player = new ArrayList<Card>();
		this.player.add(deck.dealOneCard());
		this.player.add(deck.dealOneCard());
	}

	/* This method deals 1 card to the dealer */
	public void dealerTakeCard() {
		this.dealer.add(deck.dealOneCard());
	}

	/* This method deals 1 card to the player */
	public void playerTakeCard() {
		this.player.add(deck.dealOneCard());
	}

	/* This method evaluates the hand and returns an ArrayList with the value 
	 * of the hand.
	 * If the hand contains an Ace it returns both possible values if they both
	 * are under 21. If one of them or both of them are over 21 it returns the 
	 * smaller of the 2.
	 */
	public static ArrayList<Integer> possibleHandValues(ArrayList<Card> hand) {
		ArrayList<Integer> handValue = new ArrayList<Integer>();
		Integer value = 0;
		Integer alternateValue = 0;
		for(Card card : hand) {
			if(card.getRank() == Rank.ACE) {
				value += card.getRank().getValue();
				alternateValue += 11;
			} else {
				value += card.getRank().getValue();
				alternateValue += card.getRank().getValue();
			}
		}

		if(value == alternateValue) {
			handValue.add(value);
		} else if(value > 21 && alternateValue > 21) {
			handValue.add((value <= alternateValue)? value : alternateValue);
		} else if(value > 21 || alternateValue > 21) {
			handValue.add((value <= alternateValue)? value : alternateValue);
		} else {
			handValue.add(value);
			handValue.add(alternateValue);
		}

		Collections.sort(handValue);
		return handValue;
	}

	/* This method assesses the hand and returns 1 of 4 things
	 * INSUFFICENT_CARDS if the hand has less than 2 cards.
	 * NATURAL_BLACKJACK if the hand is 21 and is only 2 cards.
	 * BUST if the hand value is more than 21.
	 * NORMAL if none of the other categories apply.
	 */
	public static HandAssessment assessHand(ArrayList<Card> hand) {
		if(hand.size() < 2) {
			return HandAssessment.INSUFFICIENT_CARDS;
		} else if(possibleHandValues(hand).contains(21) && hand.size() == 2) {
			return HandAssessment.NATURAL_BLACKJACK;
		} else if(possibleHandValues(hand).get(0) > 21) {
			return HandAssessment.BUST;
		} else {
			return HandAssessment.NORMAL;
		}
	}

	/* This method looks at both the dealer's hand and player's hand and 
	 * determines the game's result.
	 * Returns NATURAL_BLACKJACK if the player has a natural blackjack 
	 * and the dealer doesn't.
	 * Returns PUSH if both the player and dealer have the same hand value or if
	 * both of them have natural blackjacks.
	 * Returns PLAYER_WON if the player's hand value is greater than the dealer
	 * or if the dealer has busted.
	 * Returns PLAYER_LOST if the player has busted or the dealer's hand value
	 * is greater than the player.
	 */
	public GameResult gameAssessment() {
		if(assessHand(player) == HandAssessment.NATURAL_BLACKJACK && 
				assessHand(dealer) != HandAssessment.NATURAL_BLACKJACK) {
			return GameResult.NATURAL_BLACKJACK;
		} else if(assessHand(player) == HandAssessment.NATURAL_BLACKJACK &&
				assessHand(dealer) == HandAssessment.NATURAL_BLACKJACK) {
			return GameResult.PUSH;
		} else {
			if(assessHand(player) == HandAssessment.BUST) {
				return GameResult.PLAYER_LOST;
			} else if(assessHand(dealer) == HandAssessment.BUST) {
				return GameResult.PLAYER_WON;
			} else {
				if(possibleHandValues(player).size() == 1 && 
						possibleHandValues(dealer).size() == 1) {
					if(possibleHandValues(player).get(0) > 
					possibleHandValues(dealer).get(0)) {
						return GameResult.PLAYER_WON;
					} else if(possibleHandValues(player).get(0) < 
							possibleHandValues(dealer).get(0)) {
						return GameResult.PLAYER_LOST;
					} else {
						return GameResult.PUSH;
					}
				} else if(possibleHandValues(player).size() == 2 && 
						possibleHandValues(dealer).size() == 1) {
					if(possibleHandValues(player).get(1) > 
					possibleHandValues(dealer).get(0)) {
						return GameResult.PLAYER_WON;
					} else if(possibleHandValues(player).get(1) < 
							possibleHandValues(dealer).get(0)) {
						return GameResult.PLAYER_LOST;
					} else {
						return GameResult.PUSH;
					}
				} else if(possibleHandValues(player).size() == 1 && 
						possibleHandValues(dealer).size() == 2) {
					if(possibleHandValues(player).get(0) > 
					possibleHandValues(dealer).get(1)) {
						return GameResult.PLAYER_WON;
					} else if(possibleHandValues(player).get(0) < 
							possibleHandValues(dealer).get(1)) {
						return GameResult.PLAYER_LOST;
					} else {
						return GameResult.PUSH;
					}
				} else {
					if(possibleHandValues(player).get(1) > 
					possibleHandValues(dealer).get(1)) {
						return GameResult.PLAYER_WON;
					} else if(possibleHandValues(player).get(1) < 
							possibleHandValues(dealer).get(1)) {
						return GameResult.PLAYER_LOST;
					} else {
						return GameResult.PUSH;
					}
				}
			}
		}
	}

	/* This method determines whether or not the dealer should take cards.
	 * Returns true if the dealer's hand value is less than 16 or if the 
	 * dealer's hand can be valued at 7 or 17.
	 * Returns false if the dealer's hand value is greater than 18 or if the 
	 * hand can only be valued at 17.	 
	 * */
	public boolean dealerShouldTakeCard() {
		if(possibleHandValues(dealer).size() == 1) {
			if(possibleHandValues(dealer).get(0) <= 16) {
				return true;
			} else {
				return false;
			}
		} else {
			if(possibleHandValues(dealer).get(1) <= 16) {
				return true;
			} else if(possibleHandValues(dealer).get(0) == 7 && 
					possibleHandValues(dealer).get(1) == 17) {
				return true;
			} else {
				return false;
			}
		}
	}
}
