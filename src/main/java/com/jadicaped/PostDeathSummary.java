package com.jadicaped;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDeathSummary {
    private static final int MAX_CONFIDENCE = 100;
    private static final int CONFIDENCE_DIVISOR = 3;

    @Builder.Default
    private final String timeSurvived = "00:00";
    @Builder.Default
    private final int waveReached = 0;
    @Builder.Default
    private final String finalHitType = "N/A";
    @Builder.Default
    private final int finalHitDamage = 0;
    @Builder.Default
    private final String prayerStateAtDeath = "N/A";
    @Builder.Default
    private final int hpAtDeath = 0;
    @Builder.Default
    private final int prayerAccuracyPercent = 0;
    @Builder.Default
    private final int missedPrayers = 0;
    @Builder.Default
    private final int ticksUnprotected = 0;
    @Builder.Default
    private final int longestUnprotectedStreak = 0;
    @Builder.Default
    private final int totalPrayerSwitches = 0;
    @Builder.Default
    private final int totalDamageTaken = 0;
    @Builder.Default
    private final int damageWhileProtected = 0;
    @Builder.Default
    private final int damageWhileUnprotected = 0;
    @Builder.Default
    private final int damageAvoidedDueToCorrectPrayer = 0;
    @Builder.Default
    private final int brewsUsed = 0;
    @Builder.Default
    private final int restoresUsed = 0;
    @Builder.Default
    private final int totalPrayerDrain = 0;
    @Builder.Default
    private final int overheadUptimePercent = 0;
    @Builder.Default
    private final int tilesMoved = 0;
    @Builder.Default
    private final String movementFrequency = "N/A";
    @Builder.Default
    private final int timesStoodStillTooLong = 0;
    @Builder.Default
    private final int unnecessaryMovement = 0;
    @Builder.Default
    private final int prayerMistakes = 0;
    @Builder.Default
    private final int movementMistakes = 0;
    @Builder.Default
    private final int inventoryMistakes = 0;
    @Builder.Default
    private final int confidenceScore = 0;

    public static PostDeathSummary empty() {
        return PostDeathSummary.builder()
            .timeSurvived("No attempt recorded")
            .waveReached(0)
            .finalHitType("N/A")
            .finalHitDamage(0)
            .prayerStateAtDeath("N/A")
            .hpAtDeath(0)
            .prayerAccuracyPercent(0)
            .missedPrayers(0)
            .ticksUnprotected(0)
            .longestUnprotectedStreak(0)
            .totalPrayerSwitches(0)
            .totalDamageTaken(0)
            .damageWhileProtected(0)
            .damageWhileUnprotected(0)
            .damageAvoidedDueToCorrectPrayer(0)
            .brewsUsed(0)
            .restoresUsed(0)
            .totalPrayerDrain(0)
            .overheadUptimePercent(0)
            .tilesMoved(0)
            .movementFrequency("N/A")
            .timesStoodStillTooLong(0)
            .unnecessaryMovement(0)
            .prayerMistakes(0)
            .movementMistakes(0)
            .inventoryMistakes(0)
            .confidenceScore(0)
            .build();
    }

    public int getConfidenceScore() {
        return confidenceScore > 0 ? confidenceScore : calculateConfidenceScore();
    }

    public static int calculateConfidenceScore(PostDeathSummary summary) {
        final int accuracy = summary.prayerAccuracyPercent;
        final int movement = Math.max(0, MAX_CONFIDENCE - (summary.unnecessaryMovement * 3
            + summary.timesStoodStillTooLong * 10));
        final int resources = Math.max(0, MAX_CONFIDENCE - (summary.inventoryMistakes * 12
            + summary.prayerMistakes * 8));
        return Math.max(0, Math.min(MAX_CONFIDENCE, (accuracy + movement + resources) / CONFIDENCE_DIVISOR));
    }

    private int calculateConfidenceScore() {
        return calculateConfidenceScore(this);
    }

    public String toPlainText() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Post-Death Sidebar Summary\n");
        builder.append("===========================\n\n");
        builder.append("Attempt Summary\n");
        builder.append("- Time survived: ").append(timeSurvived).append("\n");
        builder.append("- Wave reached: ").append(waveReached).append("\n");
        builder.append("- Final hit type: ").append(finalHitType).append("\n");
        builder.append("- Final hit damage: ").append(finalHitDamage).append("\n");
        builder.append("- Prayer state at death: ").append(prayerStateAtDeath).append("\n");
        builder.append("- HP at death: ").append(hpAtDeath).append("\n\n");

        builder.append("Prayer Performance\n");
        builder.append("- Prayer accuracy %: ").append(prayerAccuracyPercent).append("\n");
        builder.append("- Missed prayers: ").append(missedPrayers).append("\n");
        builder.append("- Ticks unprotected: ").append(ticksUnprotected).append("\n");
        builder.append("- Longest unprotected streak: ").append(longestUnprotectedStreak).append("\n");
        builder.append("- Total prayer switches: ").append(totalPrayerSwitches).append("\n\n");

        builder.append("Damage Breakdown\n");
        builder.append("- Total damage taken: ").append(totalDamageTaken).append("\n");
        builder.append("- Damage while protected: ").append(damageWhileProtected).append("\n");
        builder.append("- Damage while unprotected: ").append(damageWhileUnprotected).append("\n");
        builder.append("- Damage avoided due to correct prayer: ")
            .append(damageAvoidedDueToCorrectPrayer)
            .append("\n\n");

        builder.append("Resource Usage\n");
        builder.append("- Brews used: ").append(brewsUsed).append("\n");
        builder.append("- Restores used: ").append(restoresUsed).append("\n");
        builder.append("- Total prayer drain: ").append(totalPrayerDrain).append("\n");
        builder.append("- Overhead uptime %: ").append(overheadUptimePercent).append("\n\n");

        builder.append("Movement Summary\n");
        builder.append("- Tiles moved: ").append(tilesMoved).append("\n");
        builder.append("- Movement frequency: ").append(movementFrequency).append("\n");
        builder.append("- Times stood still too long: ").append(timesStoodStillTooLong).append("\n");
        builder.append("- Unnecessary movement: ").append(unnecessaryMovement).append("\n\n");

        builder.append("Mistake Breakdown\n");
        builder.append("- Prayer mistakes: ").append(prayerMistakes).append("\n");
        builder.append("- Movement mistakes: ").append(movementMistakes).append("\n");
        builder.append("- Inventory mistakes: ").append(inventoryMistakes).append("\n\n");

        builder.append("Confidence Score\n");
        builder.append("- Score: ").append(getConfidenceScore()).append('/').append(MAX_CONFIDENCE).append("\n");
        return builder.toString();
    }
}
