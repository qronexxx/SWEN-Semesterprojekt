package at.technikum_wien.app.modles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleField {
    private User player1;
    private User player2;
    private List<String> battleLog;
    private static final int MAX_ROUNDS = 100;

    public BattleField(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.battleLog = new ArrayList<>();
    }

    public String startBattle() {
        int roundCount = 0;

        while (roundCount < MAX_ROUNDS && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty()) {
            int index1 = getRandomCardIndex(player1.getDeck());
            int index2 = getRandomCardIndex(player2.getDeck());
            Card card1 = player1.getDeck().getCardByIndex(index1);
            Card card2 = player2.getDeck().getCardByIndex(index2);
            battleRound(card1, card2, index1, index2);
            roundCount++;
        }

        if (roundCount == MAX_ROUNDS) {
            battleLog.add("The battle ended in a draw after 100 rounds.");
            return "Draw";
        } else if (player1.getDeck().isEmpty() && player2.getDeck().isEmpty()) {
            battleLog.add("The battle ended in a draw.");
            return "Draw";
        } else if (player1.getDeck().isEmpty()) {
            battleLog.add(player2.getUsername() + " wins the battle!");
            updateElo(player2, player1);
            return player2.getUsername() + " wins!";
        } else {
            battleLog.add(player1.getUsername() + " wins the battle!");
            updateElo(player1, player2);
            return player1.getUsername() + " wins!";
        }
    }

    private void battleRound(Card card1, Card card2, int index1, int index2) {
        double damage1 = calculateDamage(card1, card2);
        double damage2 = calculateDamage(card2, card1);

        // Apply special rules
        if (isSpecialRule(card1, card2)) {
            battleLog.add(player1.getUsername() + "'s " + card1.getName() + " is affected by a special rule against " + player2.getUsername() + "'s " + card2.getName());
            damage1 = 0;
        } else if (isSpecialRule(card2, card1)) {
            battleLog.add(player2.getUsername() + "'s " + card2.getName() + " is affected by a special rule against " + player1.getUsername() + "'s " + card1.getName());
            damage2 = 0;
        }

        // Apply booster feature
        if (new Random().nextBoolean()) {
            damage1 *= 1.5;
            battleLog.add(player1.getUsername() + "'s " + card1.getName() + " received a damage booster!");
        } else {
            damage2 *= 1.5;
            battleLog.add(player2.getUsername() + "'s " + card2.getName() + " received a damage booster!");
        }

        if (damage1 > damage2) {
            battleLog.add(player1.getUsername() + "'s " + card1.getName() + " defeats " + player2.getUsername() + "'s " + card2.getName());
            player2.getDeck().deleteCard(index2);
            player1.getDeck().addCard(card2);
        } else if (damage2 > damage1) {
            battleLog.add(player2.getUsername() + "'s " + card2.getName() + " defeats " + player1.getUsername() + "'s " + card1.getName());
            player1.getDeck().deleteCard(index1);
            player2.getDeck().addCard(card1);
        } else {
            battleLog.add(player1.getUsername() + "'s " + card1.getName() + " draws with " + player2.getUsername() + "'s " + card2.getName());
        }
    }

    private double calculateDamage(Card card1, Card card2) {
        if (card1 instanceof MonsterCard && card2 instanceof MonsterCard) {
            return card1.getDamage();
        } else if (card1 instanceof SpellCard) {
            return card1.getDamage() * card1.getElement().getEffectivnessAgainst(card2);
        } else {
            return card1.getDamage();
        }
    }

    private boolean isSpecialRule(Card card1, Card card2) {
        return (card1.getName().equals("Goblin") && card2.getName().equals("Dragon")) ||
                (card1.getName().equals("Ork") && card2.getName().equals("Wizzard")) ||
                (card1.getName().equals("Knight") && card2 instanceof SpellCard && card2.getElement() instanceof WaterElement) ||
                (card1 instanceof SpellCard && card2.getName().equals("Kraken")) ||
                (card1.getName().equals("Dragon") && card2.getName().equals("FireElf"));
    }

    private int getRandomCardIndex(CardCollection deck) {
        Random rand = new Random();
        return rand.nextInt(deck.size());
    }

    private void updateElo(User winner, User loser) {
        winner.setElo(winner.getElo() + 3);
        loser.setElo(loser.getElo() - 5);
    }

    public List<String> getBattleLog() {
        return battleLog;
    }
}