let combatants = [];
let enemy = {
    "name": "xX_ModHater69_Xx",
    "maxHp": 300,
    "attack": 30,
    "defense": 5,
    "equipment": [],
    "spells": [],
    "feats": [],
    "gold": 0
}
generateCombatant(1);

function generateCombatant(i) {
    let combatant = {
        "characterId": i,
        "twitchUsername": "Mod " + i,
        "name": "Mod " + i,
        "level": 1,
        "hp": 50,
        "maxHp": 50,
        "attack": 20,
        "defense": 10,
        "equipment": [],
        "spells": [],
        "feats": [],
        "gold": 0
    }
    combatants.push(combatant);
}

function addCombatant() {
    generateCombatant(combatants.length + 1);
    let htmlBlob = `<span class="userCharacterDiv">
        <img src="images/character_sprite.png" alt="character sprite">
        <p>Mod ${combatants.length}</p>
    </span>`
    document.getElementById('userCharactersDiv').insertAdjacentHTML('beforeend', htmlBlob);
}

async function startCombat() {
    document.getElementById('combatLog').innerHTML = '';
    let htmlBlob = `<p>Combat starts</p>`;
    document.getElementById('combatLog').insertAdjacentHTML('afterbegin', htmlBlob);
    enemy.hp = enemy.maxHp;
    let lastAttackerIndex = null;
    while (enemy.hp > 0) {
        let livingCombatants = getLivingCombatants();
        if (lastAttackerIndex == null || lastAttackerIndex >= livingCombatants[livingCombatants.length - 1].index) {
            // enemy attacks
            let targetIndex = Math.floor(Math.random() * livingCombatants.length);
            let target = combatants[livingCombatants[targetIndex].index];
            let isDead = dealDamage(enemy, target);
            if (isDead) {
                document.getElementsByClassName('userCharacterDiv')[targetIndex].classList.add("isDead");
                if (livingCombatants.length === 1) {
                    htmlBlob = `<p>As the last combatant falls, ${enemy.name} takes a final look at the destruction they wrought. Satisfied, they march on to destinations unknown.</p>`;
                    document.getElementById('combatLog').insertAdjacentHTML('afterbegin', htmlBlob);
                }
            }
            lastAttackerIndex = -1;
        } else {
            // combatant attacks
            let attacker = null;
            for (let i = lastAttackerIndex + 1; i < combatants.length; i++) {
                if (combatants[i].hp <= 0) {
                    continue;
                }
                attacker = combatants[i];
                break;
            }
            let isDead = dealDamage(attacker, enemy);
            if (isDead) {
                let livingCambatants = getLivingCombatants();
                htmlBlob = `<p>As ${enemy.name} falls, ${livingCambatants.length} combatants raise their arms to the sky in unison and announce their victory to the gods.</p>`;
                if (livingCambatants.length == 1) {
                    htmlBlob = `<p>As ${enemy.name} falls, one lone survivor raises their arms to the sky and announce their victory to the gods.</p>`;
                }
                document.getElementById('combatLog').insertAdjacentHTML('afterbegin', htmlBlob);
                break;
            }
            lastAttackerIndex++;
        }
        await sleep(500);
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function getLivingCombatants() {
    let livingCombatants = [];
    let index = 0;
    for (let c in combatants) {
        if (combatants[c].hp > 0) {
            let livingC = combatants[c];
            livingC.index = index;
            livingCombatants.push(livingC);
        }
        index++;
    }
    return livingCombatants;
}

function dealDamage(attacker, target) {
    let damage = attacker.attack - target.defense;
    target.hp -= damage;
    let isDead = target.hp <= 0;
    addHitToLog(attacker, target, damage, isDead);
    return isDead;
}

function addHitToLog(attacker, defender, damage, isDead) {
    let deathMessage = '';
    let hp = defender.hp;
    if (isDead) {
        deathMessage = `, killing them`;
        hp = 0;
    }
    let textColor = `style="color: green"`;
    if (attacker.name === enemy.name) {
        textColor = `style="color: red"`;
    }
    let htmlBlob = `<p ${textColor}>${attacker.name} hit ${defender.name} for ${damage} damage${deathMessage}. They have ${hp} HP left.</p>`;
    document.getElementById('combatLog').insertAdjacentHTML('afterbegin', htmlBlob);
}