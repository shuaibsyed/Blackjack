package tests;

import deckOfCards.*;
import blackjack.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import org.junit.Test;

public class PublicTests {

	@Test
	public void testDeckConstructorAndDealOneCard() {
		Deck deck = new Deck();
		for (int suitCounter = 0; suitCounter < 4; suitCounter++) {
			for (int valueCounter = 0; valueCounter < 13; valueCounter++) {
				Card card = deck.dealOneCard();
				assertEquals(card.getSuit().ordinal(), suitCounter);
				assertEquals(card.getRank().ordinal(), valueCounter);
			}
		}
	}
	
	/* This test will pass only if an IndexOutOfBoundsException is thrown */
	@Test (expected = IndexOutOfBoundsException.class)
	public void testDeckSize() {
		Deck deck = new Deck();
		for (int i = 0; i < 53; i++) {  // one too many -- should throw exception
			deck.dealOneCard();
		}
	}

	@Test
	public void testDeckShuffle() {
		Deck deck = new Deck();
		Random random = new Random(1234);
		deck.shuffle(random);
		assertEquals(new Card(Rank.KING, Suit.CLUBS), deck.dealOneCard());
		assertEquals(new Card(Rank.TEN, Suit.CLUBS), deck.dealOneCard());
		assertEquals(new Card(Rank.JACK, Suit.SPADES), deck.dealOneCard());
		for (int i = 0; i < 20; i++) {
			deck.dealOneCard();
		}
		assertEquals(new Card(Rank.SIX, Suit.CLUBS), deck.dealOneCard());
		assertEquals(new Card(Rank.FIVE, Suit.CLUBS), deck.dealOneCard());
		for (int i = 0; i < 24; i++) {
			deck.dealOneCard();
		}
		assertEquals(new Card(Rank.EIGHT, Suit.CLUBS), deck.dealOneCard());
		assertEquals(new Card(Rank.JACK, Suit.HEARTS), deck.dealOneCard());
		assertEquals(new Card(Rank.JACK, Suit.CLUBS), deck.dealOneCard());
	}
	
	@Test
	public void testGameBasics() {
		Random random = new Random(3723);
		BlackjackModel game = new BlackjackModel();
		game.createAndShuffleDeck(random);
		game.initialPlayerCards();
		game.initialDealerCards();
		game.playerTakeCard();
		game.dealerTakeCard();
		ArrayList<Card> playerCards = game.getPlayerCards();
		ArrayList<Card> dealerCards = game.getDealerCards();
		assertTrue(playerCards.get(0).equals(new Card(Rank.QUEEN, Suit.HEARTS)));
		assertTrue(playerCards.get(1).equals(new Card(Rank.SIX, Suit.DIAMONDS)));
		assertTrue(playerCards.get(2).equals(new Card(Rank.EIGHT, Suit.HEARTS)));
		assertTrue(dealerCards.get(0).equals(new Card(Rank.THREE, Suit.CLUBS)));
		assertTrue(dealerCards.get(1).equals(new Card(Rank.NINE, Suit.SPADES)));
		assertTrue(dealerCards.get(2).equals(new Card(Rank.FIVE, Suit.CLUBS)));		
	}
	
	@Test
	public void testPossibleHandValues() {
		ArrayList<Card> player1 = new ArrayList<Card>();
		player1.add(new Card(Rank.TWO, Suit.CLUBS));
		player1.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
		assertTrue(BlackjackModel.possibleHandValues(player1).get(0) == 10);
		ArrayList<Card> player2 = new ArrayList<Card>();
		player2.add(new Card(Rank.KING, Suit.CLUBS));
		player2.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
		assertTrue(BlackjackModel.possibleHandValues(player2).get(0) == 18);
		ArrayList<Card> player3 = new ArrayList<Card>();
		player3.add(new Card(Rank.ACE, Suit.CLUBS));
		player3.add(new Card(Rank.KING, Suit.DIAMONDS));
		player3.add(new Card(Rank.FIVE, Suit.HEARTS));
		assertTrue(BlackjackModel.possibleHandValues(player3).get(0) == 16);
		ArrayList<Card> player4 = new ArrayList<Card>();
		player4.add(new Card(Rank.ACE, Suit.CLUBS));
		player4.add(new Card(Rank.SIX, Suit.DIAMONDS));
		assertTrue(BlackjackModel.possibleHandValues(player4).get(0) == 7);
		assertTrue(BlackjackModel.possibleHandValues(player4).get(1) == 17);
		ArrayList<Card> player5 = new ArrayList<Card>();
		player5.add(new Card(Rank.KING, Suit.CLUBS));
		player5.add(new Card(Rank.JACK, Suit.DIAMONDS));
		player5.add(new Card(Rank.FOUR, Suit.SPADES));
		assertTrue(BlackjackModel.possibleHandValues(player5).get(0) == 24);
		ArrayList<Card> player6 = new ArrayList<Card>();
		player6.add(new Card(Rank.SEVEN, Suit.CLUBS));
		player6.add(new Card(Rank.ACE, Suit.DIAMONDS));
		player6.add(new Card(Rank.FOUR, Suit.SPADES));
		player6.add(new Card(Rank.TEN, Suit.SPADES));
		assertTrue(BlackjackModel.possibleHandValues(player6).get(0) == 22);
		ArrayList<Card> player7 = new ArrayList<Card>();
		player7.add(new Card(Rank.ACE, Suit.CLUBS));
		player7.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
		assertTrue(BlackjackModel.possibleHandValues(player7).get(0) == 11);
		assertTrue(BlackjackModel.possibleHandValues(player7).get(1) == 21);
	}
	
	@Test
	public void testHandAssessment() {
		ArrayList<Card> player1 = new ArrayList<Card>();
		player1.add(new Card(Rank.TWO, Suit.CLUBS));
		assertTrue(BlackjackModel.assessHand(player1) == 
				HandAssessment.INSUFFICIENT_CARDS);
		ArrayList<Card> player2 = new ArrayList<Card>();
		player2.add(new Card(Rank.KING, Suit.CLUBS));
		player2.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
		assertTrue(BlackjackModel.assessHand(player2) == HandAssessment.NORMAL);
		ArrayList<Card> player5 = new ArrayList<Card>();
		player5.add(new Card(Rank.KING, Suit.CLUBS));
		player5.add(new Card(Rank.JACK, Suit.DIAMONDS));
		player5.add(new Card(Rank.FOUR, Suit.SPADES));
		assertTrue(BlackjackModel.assessHand(player5) == HandAssessment.BUST);
		ArrayList<Card> player7 = new ArrayList<Card>();
		player7.add(new Card(Rank.ACE, Suit.CLUBS));
		player7.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
		assertTrue(BlackjackModel.assessHand(player7) == 
				HandAssessment.NATURAL_BLACKJACK);
		ArrayList<Card> player8 = new ArrayList<Card>();
		player8.add(new Card(Rank.SEVEN, Suit.CLUBS));
		player8.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
		player8.add(new Card(Rank.FOUR, Suit.CLUBS));
		assertTrue(BlackjackModel.assessHand(player8) == 
				HandAssessment.NORMAL);
	}
	
}
