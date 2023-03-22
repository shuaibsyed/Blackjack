package deckOfCards;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	//Instance Variable
	private ArrayList<Card> cards;
	
	/*Constructor that sets the cards in order from Ace of Spades to 
	 * King of Diamonds.
	 */
	public Deck() {
		this.cards = new ArrayList<Card>();
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				this.cards.add(new Card(rank, suit));
			}
		}
	}
	
	/* This method shuffles the deck */
	public void shuffle(Random randomNumberGenerator) {
		Collections.shuffle(this.cards, randomNumberGenerator);
	}
	
	/* This method removes one card from the front of the deck */
	public Card dealOneCard() {
		return this.cards.remove(0);
	}
}
