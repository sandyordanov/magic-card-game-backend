package fontys.magiccardgame.domain;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private final long id;

    private Player player1;
    private Player player2;
    private boolean isGameOver = false;
    private Player winner;

    // Store the card requests for each player
    private final ConcurrentHashMap<Long, PlayCardRequest> pendingRequests = new ConcurrentHashMap<>();
    private final Set<Long> playersWhoPlayed = ConcurrentHashMap.newKeySet();

    public GameSession(Player player1, Player player2) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setHp(20);
        this.player2.setHp(20);
        this.player1.setHand(new ArrayList<>());
        this.player2.setHand(new ArrayList<>());

        this.player1.setDeckStack(new ArrayDeque<>(player1.getDeck().getCards()));
        this.player2.setDeckStack(new ArrayDeque<>(player2.getDeck().getCards()));
        drawInitialCards();
    }

    private void drawInitialCards() {
        for (int i = 0; i < 5; i++) {
            player1.getHand().add(player1.getDeckStack().pop());
            player2.getHand().add(player2.getDeckStack().pop());
        }
    }

    public synchronized void addPlayCardRequest(PlayCardRequest request) {
        if (playersWhoPlayed.contains(request.getPlayerId())) {
            throw new IllegalStateException("Player has already played a card this turn.");
        }
        pendingRequests.put(request.getPlayerId(), request);
        playersWhoPlayed.add(request.getPlayerId());

        if (pendingRequests.size() == 2) {
            executeTurn();
        }
    }

    private void executeTurn() {
        PlayCardRequest request1 = pendingRequests.get(player1.getId());
        PlayCardRequest request2 = pendingRequests.get(player2.getId());

        // Extract the cards being played
        Card card1 = request1.getCard();
        Card card2 = request2.getCard();

        // Calculate the health points after the attack
        int card1RemainingHp = card1.getHealthPoints() - card2.getAttackPoints();
        int card2RemainingHp = card2.getHealthPoints() - card1.getAttackPoints();

        // Apply remaining damage to players if card HP goes negative
        if (card1RemainingHp < 0) {
            player1.setHp(player1.getHp() + card1RemainingHp); // card1RemainingHp is negative
        }
        if (card2RemainingHp < 0) {
            player2.setHp(player2.getHp() + card2RemainingHp); // card2RemainingHp is negative
        }

        // Check for game over
        if (checkGameOver()) {
            isGameOver = true;
            winner = player1.getHp() > 0 ? player1 : player2;
        }

        // Clear the pending requests and reset the turn status
        pendingRequests.clear();
        playersWhoPlayed.clear();
    }

    private boolean checkGameOver() {
        return player1.getHp() <= 0 || player2.getHp() <= 0;
    }
}
