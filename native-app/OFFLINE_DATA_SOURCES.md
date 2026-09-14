# Wild Rift Assistant — Offline Data Strategy

## Reference sources

The product design uses these requested Wild Rift references for matchup, draft, coaching, build and gameplay concepts:

1. https://lolwildriftbuild.com/category/tips-tricks/
2. https://wildriftcore.com/en/guides/
3. https://wildriftcore.com/en/counter-picks/
4. https://wildriftcore.com/en/draft/?lane=DRAGON
5. https://wildriftcore.com/en/coach/
6. https://www.wildriftmeta.com/
7. https://www.wildriftguides.com/

## Data rules

- The app is **offline-first**. Runtime recommendations must work from bundled data.
- Every future imported dataset should carry `patch`, `source`, and `updatedAt` metadata.
- Sources can represent different patch snapshots. Do not silently merge conflicting values as if they were one authoritative live dataset.
- Counter recommendations are decision support, not an automatic-win claim.
- Item recommendations must react to enemy champion/team threats and the player's champion, rather than blindly returning a fixed build.

## Decision model

`Lane -> Player Champion -> Enemy Champions -> Enemy Team Threats -> Threat Profile -> Counter Score -> Item Counter -> Build Adjustment -> Gameplay Plan -> Macro/Objective Advice`

Threat dimensions currently supported by the engine include:

- healing
- shields
- magic burst / AP
- physical burst / AD
- armor stacking / frontline
- magic resistance
- crowd control
- assassin threat
- auto-attack pressure
- mixed damage

## Product roadmap

1. Replace seed-only champion/item records with bundled structured JSON.
2. Add a five-slot enemy team selector with duplicate prevention.
3. Add role-aware champion filtering.
4. Attach patch/source metadata to every recommendation.
5. Expand item compatibility rules by champion class and damage profile.
6. Add concise gameplay and objective recommendations based on the same threat profile.
