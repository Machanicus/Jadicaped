package com.jadicaped;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PostDeathSummaryTest {
    private static final int TEST_WAVE = 16;
    private static final int TEST_FINAL_DAMAGE = 42;
    private static final int TEST_ACCURACY = 88;
    private static final int TEST_PRAYER_SWITCHES = 18;
    private static final int TEST_DAMAGE_TAKEN = 946;
    private static final int TEST_DAMAGE_PROTECTED = 542;
    private static final int TEST_DAMAGE_UNPROTECTED = 404;
    private static final int TEST_DAMAGE_AVOIDED = 230;
    private static final int TEST_HP_AT_DEATH = 11;
    private static final int TEST_PRAYER_DRAIN = 178;
    private static final int TEST_OVERHEAD_UPTIME = 76;
    private static final int TEST_TILES_MOVED = 318;
    private static final int TEST_UNNECESSARY_MOVEMENT = 12;
    private static final int TEST_CONFIDENCE = 87;

    @Test
    void exportsSummaryAsPlainTextAndComputesConfidence() {
        final PostDeathSummary summary = PostDeathSummary.builder()
            .timeSurvived("03:14")
            .waveReached(TEST_WAVE)
            .finalHitType("Magic")
            .finalHitDamage(TEST_FINAL_DAMAGE)
            .prayerStateAtDeath("Protect from Magic")
            .hpAtDeath(TEST_HP_AT_DEATH)
            .prayerAccuracyPercent(TEST_ACCURACY)
            .missedPrayers(3)
            .ticksUnprotected(4)
            .longestUnprotectedStreak(2)
            .totalPrayerSwitches(TEST_PRAYER_SWITCHES)
            .totalDamageTaken(TEST_DAMAGE_TAKEN)
            .damageWhileProtected(TEST_DAMAGE_PROTECTED)
            .damageWhileUnprotected(TEST_DAMAGE_UNPROTECTED)
            .damageAvoidedDueToCorrectPrayer(TEST_DAMAGE_AVOIDED)
            .brewsUsed(4)
            .restoresUsed(2)
            .totalPrayerDrain(TEST_PRAYER_DRAIN)
            .overheadUptimePercent(TEST_OVERHEAD_UPTIME)
            .tilesMoved(TEST_TILES_MOVED)
            .movementFrequency("Moderate")
            .timesStoodStillTooLong(1)
            .unnecessaryMovement(TEST_UNNECESSARY_MOVEMENT)
            .prayerMistakes(2)
            .movementMistakes(3)
            .inventoryMistakes(1)
            .confidenceScore(TEST_CONFIDENCE)
            .build();

        final String text = summary.toPlainText();

        assertTrue(text.contains("Attempt Summary"));
        assertTrue(text.contains("Wave reached: 16"));
        assertTrue(text.contains("Final hit type: Magic"));
        assertEquals(TEST_CONFIDENCE, summary.getConfidenceScore());
    }
}
