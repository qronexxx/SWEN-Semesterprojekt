package at.technikum_wien.app.business;

import at.technikum_wien.app.modles.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Controller - Controller Interface abstrakte mothoden von get...


@Getter
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
        System.out.println(battleLog);
        if (roundCount == MAX_ROUNDS) {
            battleLog.add("The battle ended in a draw after 100 rounds.");
            return "Draw";
        } else if (player1.getDeck().isEmpty() && player2.getDeck().isEmpty()) {
            battleLog.add("The battle ended in a draw.");
            return "Draw";
        } else if (player1.getDeck().isEmpty()) {
            battleLog.add(player2.getUsername() + " wins the battle!");
            updateStats(player2, player1);
            return player2.getUsername() + " wins!";
        } else {
            battleLog.add(player1.getUsername() + " wins the battle!");
            updateStats(player1, player2);
            return player1.getUsername() + " wins!";
        }
    }

    public void battleRound(Card card1, Card card2, int index1, int index2) {
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
        Random rand = new Random();
        if (rand.nextBoolean()) {
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

    public double calculateDamage(Card card1, Card card2) {
        Element element1 = card1.getElement();
        Element element2 = card2.getElement();

        switch (element1) {
            case WATER:
                if (element2 == Element.FIRE) {
                    return card1.getDamage() * 2;
                } else if (element2 == Element.REGULAR) {
                    return card1.getDamage() * 0.5;
                } else {
                    return card1.getDamage();
                }
            case FIRE:
                if (element2 == Element.REGULAR) {
                    return card1.getDamage() * 2;
                } else if (element2 == Element.WATER) {
                    return card1.getDamage() * 0.5;
                } else {
                    return card1.getDamage();
                }
            case REGULAR:
            default:
                return card1.getDamage();
        }
    }

    public boolean isSpecialRule(Card card1, Card card2) {
        String name1 = card1.getName();
        String name2 = card2.getName();

        return (name1.equalsIgnoreCase("Goblin") && name2.equalsIgnoreCase("Dragon")) ||
                (name1.equalsIgnoreCase("Ork") && name2.equalsIgnoreCase("Wizard")) ||
                (name1.equalsIgnoreCase("Knight") && (card2.getName().startsWith("Water")) && card2 instanceof SpellCard) ||
                (card1 instanceof SpellCard && name2.equalsIgnoreCase("Kraken")) ||
                (name1.equalsIgnoreCase("Dragon") && name2.equalsIgnoreCase("FireElf"));
    }

    private int getRandomCardIndex(Deck deck) {
        Random rand = new Random();
        return rand.nextInt(deck.size());
    }

    private void updateStats(User winner, User loser) {
        winner.setElo(winner.getElo() + 3);
        winner.setWins(winner.getWins() + 1);

        loser.setElo(loser.getElo() - 5);
        loser.setLosses(loser.getLosses() + 1);
    }
}
