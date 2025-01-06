package app.business;

import at.technikum_wien.app.business.BattleField;
import at.technikum_wien.app.modles.Card;
import at.technikum_wien.app.modles.SpellCard;
import at.technikum_wien.app.modles.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import at.technikum_wien.app.modles.Element;

public class BattleLogikTest {
    private BattleField battleField;
    private Element element;

    @BeforeEach
    void setUp() {
        battleField = new BattleField(new User("Player1","pw"), new User("Player2","pw"));
    }

    @Test
    void testSpecialRule_GoblinVsDragon() {
        Card goblin = new Card("Goblin", 30);
        Card dragon = new Card("Dragon", 50);
        double dmgGoblin = battleField.calculateDamage(goblin, dragon);
        boolean special = battleField.isSpecialRule(goblin, dragon);
        if(special) dmgGoblin = 0;
        assertTrue(special);
        assertEquals(0.0, dmgGoblin);
    }

    @Test
    void testSpecialRule_WizardControlsOrk() {
        Card wizard = new Card("Wizard", 40);
        Card ork = new Card("Ork", 45);
        double dmgOrk = battleField.calculateDamage(ork, wizard);
        boolean special = battleField.isSpecialRule(ork, wizard);
        if(special) dmgOrk = 0;
        assertTrue(special);
        assertEquals(0.0, dmgOrk);
    }

    @Test
    void testSpecialRule_WaterSpellAgainstKnight() {
        Card knight = new Card("Knight", 55);
        Card waterSpell = new SpellCard("WaterSpell", 20, element);
        double dmgKnight = battleField.calculateDamage(knight, waterSpell);
        boolean special = battleField.isSpecialRule(knight, waterSpell);
        if(special) dmgKnight = 0;
        assertTrue(special);
        assertEquals(0.0, dmgKnight);
    }

    @Test
    void testSpecialRule_KrakenImmuneToSpell() {
        Card kraken = new Card("Kraken", 60);
        Card fireSpell = new SpellCard("FireSpell", 30, element);
        double dmgKraken = battleField.calculateDamage(fireSpell, kraken);
        boolean special = battleField.isSpecialRule(fireSpell, kraken);
        if(special) dmgKraken = 0;
        assertTrue(special);
        assertEquals(0, dmgKraken);
    }

    @Test
    void testSpecialRule_FireElfEvadesDragon() {
        Card elf = new Card("FireElf", 35);
        Card dragon = new Card("Dragon", 50);
        double dmgElf = battleField.calculateDamage(dragon, elf);
        boolean special = battleField.isSpecialRule(dragon, elf);
        if(special) dmgElf = 0;
        assertTrue(special);
        assertEquals(0, dmgElf);
    }

    @Test
    void testSpellDamage_WaterAgainstFire() {
        Card water = new SpellCard("WaterSpell", 20, element);
        Card fire = new SpellCard("FireSpell", 25, element);
        double dmgWater = battleField.calculateDamage(water, fire);
        boolean special = battleField.isSpecialRule(water, fire);
        assertFalse(special);
        assertEquals(40.0, dmgWater);
    }

    @Test
    void testSpellDamage_FireAgainstWater() {
        Card fire = new SpellCard("FireSpell", 25, element);
        Card water = new SpellCard("WaterSpell", 20, element);
        double dmgFire = battleField.calculateDamage(fire, water);
        boolean special = battleField.isSpecialRule(fire, water);
        assertFalse(special);
        assertEquals(12.5, dmgFire);
    }

    @Test
    void testNoElementEffect_MonsterVsMonster() {
        Card ork = new Card("Ork", 35);
        Card goblin = new Card("Goblin", 25);
        double dmgOrk = battleField.calculateDamage(ork, goblin);
        boolean special = battleField.isSpecialRule(ork, goblin);
        assertFalse(special);
        assertEquals(35.0, dmgOrk);
    }

    @Test
    void testMixed_FireSpellVsRegular_NoSpecial() {
        Card fireSpell = new SpellCard("FireSpell", 30, element);
        Card troll = new Card("Troll", 20);
        double dmgF = battleField.calculateDamage(fireSpell, troll);
        boolean special = battleField.isSpecialRule(fireSpell, troll);
        assertFalse(special);
        assertEquals(60.0, dmgF);
    }

    @Test
    void testSpellVsSpell_SameElement_Fire() {
        Card fire1 = new SpellCard("FireSpell", 20, element);
        Card fire2 = new SpellCard("FireSpell", 30, element);
        double dmgFire1 = battleField.calculateDamage(fire1, fire2);
        boolean special = battleField.isSpecialRule(fire1, fire2);
        assertFalse(special);
        assertEquals(20.0, dmgFire1);
    }

    @Test
    void testSpellVsSpell_SameElement_Water() {
        Card w1 = new SpellCard("WaterSpell", 25, element);
        Card w2 = new SpellCard("WaterSpell", 10, element);
        double dmgW1 = battleField.calculateDamage(w1, w2);
        boolean special = battleField.isSpecialRule(w1, w2);
        assertFalse(special);
        assertEquals(25.0, dmgW1);
    }

    @Test
    void testRegularVsRegular_SameMonster() {
        Card c1 = new Card("Troll", 40);
        Card c2 = new Card("Troll", 35);
        double dmgC1 = battleField.calculateDamage(c1, c2);
        boolean special = battleField.isSpecialRule(c1, c2);
        assertFalse(special);
        assertEquals(40.0, dmgC1);
    }

    @Test
    void testDragonVsDragon_NoSpecial() {
        Card d1 = new Card("Dragon", 50);
        Card d2 = new Card("Dragon", 45);
        double dmgD1 = battleField.calculateDamage(d1, d2);
        boolean special = battleField.isSpecialRule(d1, d2);
        assertFalse(special);
        assertEquals(50.0, dmgD1);
    }

    @Test
    void testWizardVsWizard_NoSpecial() {
        Card w1 = new Card("Wizard", 40);
        Card w2 = new Card("Wizard", 35);
        double dmgW1 = battleField.calculateDamage(w1, w2);
        boolean special = battleField.isSpecialRule(w1, w2);
        assertFalse(special);
        assertEquals(40.0, dmgW1);
    }

    @Test
    void testOrkVsOrk_NoSpecial() {
        Card o1 = new Card("Ork", 30);
        Card o2 = new Card("Ork", 50);
        double dmgO1 = battleField.calculateDamage(o1, o2);
        boolean special = battleField.isSpecialRule(o1, o2);
        assertFalse(special);
        assertEquals(30.0, dmgO1);
    }

    @Test
    void testGoblinVsGoblin_NoSpecial() {
        Card g1 = new Card("Goblin", 30);
        Card g2 = new Card("Goblin", 45);
        double dmgG1 = battleField.calculateDamage(g1, g2);
        boolean special = battleField.isSpecialRule(g1, g2);
        assertFalse(special);
        assertEquals(30.0, dmgG1);
    }

    @Test
    void testFireSpellVsRegularMonster_TwiceDamage() {
        Card fireSpell = new SpellCard("FireSpell", 25, element);
        Card regular = new Card("Knight", 30); // Knight is regular element
        double dmgF = battleField.calculateDamage(fireSpell, regular);
        boolean special = battleField.isSpecialRule(fireSpell, regular);
        // Fire vs Regular => *2
        assertFalse(special);
        assertEquals(50.0, dmgF);
    }

    @Test
    void testWaterSpellVsRegularMonster_HalfDamage() {
        Card waterSpell = new SpellCard("WaterSpell", 20, element);
        Card regular = new Card("Troll", 30);
        double dmgW = battleField.calculateDamage(waterSpell, regular);
        boolean special = battleField.isSpecialRule(waterSpell, regular);
        // Water vs Regular => *0.5
        assertFalse(special);
        assertEquals(10.0, dmgW);
    }

    @Test
    void testRegularMonsterVsSpell_NoElementEffect() {
        Card monster = new Card("Troll", 40);
        Card spell = new SpellCard("RegularSpell", 25, element);
        double dmgM = battleField.calculateDamage(monster, spell);
        boolean special = battleField.isSpecialRule(monster, spell);
        // If monster is regular and other is a spell with element regular => normal damage
        assertFalse(special);
        assertEquals(40.0, dmgM);
    }

    @Test
    void testWaterSpellVsWaterSpell_NoBoost() {
        Card w1 = new SpellCard("WaterSpell", 30, element);
        Card w2 = new SpellCard("WaterSpell", 40, element);
        double dmgW1 = battleField.calculateDamage(w1, w2);
        boolean special = battleField.isSpecialRule(w1, w2);
        // same element => no multiplier
        assertFalse(special);
        assertEquals(30.0, dmgW1);
    }
}