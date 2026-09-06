# KitPlugin für Paper Minecraft Server

Ein vollständiges Kit-System für deinen Paper Minecraft Server! 🎮

## Features

✅ **Spieler können Kits abholen** - Mit `/kit get <kit-name>`  
✅ **Cooldown-System** - Jedes Kit kann nur 1x pro Tag abgeholt werden  
✅ **Admin-Controls** - Nur OPs können Kits erstellen/ändern  
✅ **Einfache Verwaltung** - Kits aus deinem Inventar erstellen  
✅ **Farbige Nachrichten** - Schöne Chat-Ausgaben  
✅ **Konfigurierbar** - Alle Kits in `kits.yml` speichert  

## Installation

1. **JAR bauen:**
   ```bash
   mvn clean package
   ```

2. **Plugin ins Server-Verzeichnis:**
   - Die JAR aus `target/` in deinen `plugins/` Ordner kopieren
   - Server neu starten

3. **Konfiguration anpassen:**
   - Nach dem Start findest du `plugins/KitPlugin/kits.yml`
   - Dort kannst du Kits beliebig anpassen

## Befehle

### Für alle Spieler:
- `/kit get <kit-name>` - Kit abholen
- `/kit list` - Alle verfügbaren Kits anzeigen

### Nur für OPs:
- `/kit create <kit-name> [anzeigename]` - Neues Kit aus deinem Inventar erstellen
- `/kit delete <kit-name>` - Kit löschen
- `/kit reload` - Konfiguration neu laden

## Berechtigungen

- `kit.use` - Spieler dürfen Kits abholen (Standard: true)
- `kit.admin` - OPs dürfen Kits verwalten (Standard: op)
- `kit.reload` - Darf Konfiguration neu laden (Standard: op)

## Kits erstellen

### Mit Befehl:
1. Öffne dein Inventar
2. Füll es mit den Items, die im Kit sein sollen
3. Tippe: `/kit create meinkit §c⚔ Mein Kit`
4. Das Kit ist erstellt!

### Manuell in kits.yml:
```yaml
kits:
  meinkit:
    name: "§c⚔ Mein Kit"
    cooldown: 86400  # Sekunden (86400 = 24h)
    items:
      0:
        type: DIAMOND_SWORD
        amount: 1
      1:
        type: COOKED_BEEF
        amount: 32
```

## Standard Kits

Beim ersten Start werden 2 Kits erstellt:

- **starter** - Anfänger Kit mit Schwert, Spitzhacke und Essen
- **warrior** - Krieger Kit mit Rüstung und Schwert

## Autor

Entwickelt für **leonfree312** 🎮

---

**Brauchst du Hilfe?** Schreib eine Issue oder fork das Projekt!
