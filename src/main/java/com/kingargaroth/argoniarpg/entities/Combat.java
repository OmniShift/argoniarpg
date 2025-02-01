package com.kingargaroth.argoniarpg.entities;

import com.kingargaroth.argoniarpg.entities.helpers.Enemy;
import com.kingargaroth.argoniarpg.entities.helpers.EnemyConverter;
import com.kingargaroth.argoniarpg.entities.helpers.Spell;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.validation.annotation.Validated;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
@Entity
@Table(name = "combat")
@Slf4j
public class Combat implements Serializable {

    @Serial
    private static final long serialVersionUID = 7436331106871271416L;

    @Id
    @GeneratedValue
    private long combatId;

    private Instant createdTs;

    private Instant conclusionTs;

    private CombatState combatState = CombatState.IN_PREPARATION;

    @Convert(converter = EnemyConverter.class)
    private Enemy enemy;

    private List<UserCharacter> combatants = new ArrayList<>();

    private List<CombatEvent> combatEvents =  new ArrayList<>();

    private String seed;

    public Combat() {
        this.seed = RandomStringUtils.secure().next(20, true, true);
    }

    public CombatConclusion conclusion() {
        if (combatEvents.isEmpty()) {
            return new CombatConclusion(new ArrayList<>(), "Preparations are still underway...");
        } else if (enemy.getHp() > 0) {
            String conclusionString = """
                    Charred corpses litter the battlefield. It seems the %s has vanquished its attackers.
                    It snatches some livestock and virgins into it's snapping jaws and flies away...
                    it's a good job there's respawn points in the Kingdom!""";
            return new CombatConclusion(new ArrayList<>(), conclusionString);
        } else {
            List<UserCharacter> survivors = combatants.stream().filter(c -> c.getHp() > 0).toList();
            List<String> survivorNames = survivors.stream().map(UserCharacter::getName).toList();
            String survivorsString = String.join(" and ",
                    String.join(", ", survivorNames.subList(0, survivorNames.size() - 1)),
                    survivorNames.getLast());
            String isAreString = survivorNames.size() > 1 ? "are" : "is";
            String conclusionString = """
                    The fight is fierce. Swords swing, spells fly all around, and a final blow is struck.
                    After a long battle, %s lies on the ground defeated. Looking around the battlefield, %s %s still alive.
                    They grab their loot and return to the kingdom."""
                    .formatted(enemy.getName(), survivorsString, isAreString);
            return new CombatConclusion(new ArrayList<>(), conclusionString);
        }
    }

    public enum CombatState {
        /** Players can join */
        IN_PREPARATION,
        /** Bot has sent the start command. Players cannot join. Combat data is still in memory */
        CONCLUDED,
        /** Bot has retrieved the combat results. Combat data is removed from memory */
        VIEWED;
    }

    public static class CombatEvent {
        final Combatant actor;
        final Combatant target;
        final CombatAction action;
        final Spell usedSpell;
        final Integer effectStrength;
        String description;

        /**
         * @param actor the entity taking the action
         * @param target the target of the action. Is null if {@code action} is a summon spell)
         * @param action the action being performed
         * @param usedSpell the spell cast. Is null if {@code action != CAST_SPELL}
         * @param effectStrength the strength of the effect of the action (e.g. damage, healing, number of summons)
         */
        public CombatEvent(Combatant actor, Combatant target, CombatAction action, Spell usedSpell, Integer effectStrength) {
            this.actor = actor;
            this.target = target;
            this.action = action;
            this.usedSpell = usedSpell;
            this.effectStrength = effectStrength;

            String effectString = generateEffectString().replaceAll("\\s+", " ");
            this.description = "{actorName} {action} {effect}"
                    .replace("{actorName}", actor.getName())
                    .replace("{action}", action.resolve(usedSpell))
                    .replace("{effect}", effectString)
                    .trim();
            // Alice attacks Bob for 1 damage
            // Alice casts fireball on Bob for 1 damage
            // Alice casts heal on Bob for 1 health
            // Alice casts summon skeletons, adding 3 skeletons to the fray
        }

        private String generateEffectString() {
            if (target == null) {
                if (usedSpell == null || usedSpell.getEffectString() == null) {
                    return "";
                }
                return ", %s".formatted(usedSpell.getEffectString().replace("{effectStrength}", effectStrength.toString()));
            } else if (action == CombatAction.ATTACK) {
                return "%s for %d damage".formatted(target.getName(), effectStrength);
            } else {
                return switch (usedSpell) {
                    case FIREBALL -> "on %s for %d damage".formatted(target.getName(), effectStrength);
                    case HEAL -> "on %s for %d health".formatted(target.getName(), effectStrength);
                    default -> {
                        log.warn("Cannot generate effect string with action [%s], spell [%s], target [%s])".formatted(action, usedSpell, target));
                        yield "";
                    }
                };
            }
        }
    }

    @AllArgsConstructor
    public enum CombatAction {
        ATTACK("attacks"),
        CAST_SPELL("casts %s");

        private final String descriptionString;

        public String resolve(Spell spell) {
            switch (this) {
                case CAST_SPELL -> {
                    return this.descriptionString.formatted(spell.getName());
                }
            }
            return descriptionString;
        }
    }

    @AllArgsConstructor
    public static class CombatConclusion {
        private List<UserCharacter> survivors;
        private String conclusionString;
    }
}
