let userCharacter = null;

window.addEventListener('DOMContentLoaded', () => {
    let createCharName = document.getElementById('createCharName');
    let createCharBtn = document.getElementById('createCharBtn');
});

async function createChar() {
    let postCharacterBody = {
        "name": createCharName.value,
        "attack": 10,
        "defense": 10
    }
    const response = await fetch('/character', {
        method: 'POST',
        body: JSON.stringify(postCharacterBody),
        headers: {
            'Content-Type': 'application/json',
            'Twitch-Username': 'OmniShift89'
        }
    });
    userCharacter = await response.json();
    document.getElementById('twitchUsername').textContent = userCharacter.twitchUsername;
    document.getElementById('userCharacterName').textContent = userCharacter.name;
    document.getElementById('userCharacterLevel').textContent = userCharacter.level;
    document.getElementById('userCharacterAttack').textContent = userCharacter.attack;
    document.getElementById('userCharacterDefense').textContent = userCharacter.defense;
    document.getElementById('userCharacterGold').textContent = userCharacter.gold;
    document.getElementById('userCharacterSpells').textContent = userCharacter.spells;
    document.getElementById('userCharacterFeats').textContent = userCharacter.feats;
    document.getElementById('userCharacterEquipment').textContent = userCharacter.equipment;
    document.getElementById('createCharDiv').style.display = 'none';
    document.getElementById('userCharacterData').style.display = 'block';
}

async function addSpell() {
    const response = await fetch('/character/spell/add/random', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Twitch-Username': 'OmniShift89',
            'Character-Name': userCharacter.name
        }
    });
    userCharacter = await response.json();
    if (response.status == 400) {
        let addSpellBtn = document.getElementById('addSpellBtn');
        addSpellBtn.textContent = response.headers.get('Error-Message');
        addSpellBtn.disabled = true;
    } else {
        document.getElementById('userCharacterSpells').textContent = userCharacter.spells;
    }
}