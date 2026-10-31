# FRONTEND — Frontend Architecture

_Load when: working on the client._

---

## View Protocol

Backend sends **semantic data with a type tag**, not layout instructions.
Frontend decides presentation. Backend describes WHAT, not HOW.

```ts
// backend sends
{ type: 'AbilityScores', data: { scores: {...}, modifiers: {...} } }

// frontend maps type → component
const ViewTypes = {
  AbilityScores: AbilityScoreTable,
  Skills: SkillList,
}
```

`DisplayColumn` and other layout views from the original implementation are removed —
layout is the frontend's job.

Computed stats can ship their contribution breakdown as semantic data: the list of structured
contributions held transiently on the computed stat aspect is the source for a layered
"14 (10 base +2 race +2 ASI)" display.

---

## Custom Spell UI — Override Registry

For spells/actions with bespoke UI (more compact than the generic renderer),
the client maintains an override registry keyed by spell id:

```ts
// client/src/lib/overrides/index.ts
import HighIdentifyUI from "./spells/HighIdentify.svelte";

export const SpellUIOverrides: Record<string, Component> = {
  high_identify: HighIdentifyUI,
};
```

Auto-populated via `import.meta.glob('./spells/*.svelte', { eager: true })` if preferred.
Filename convention: `HighIdentify.svelte` → key `high_identify`.

Generic renderer used for everything not in the registry. Override is pure opt-in.

Override components receive `{ spell, actor, oncast, oncancel }` and call `oncast(inputs)`
to submit through the same `cast` method. UI is bespoke, contract is typed and shared.

---

## Client-side Content Loading

```ts
// vite resolves this at build time — pattern must be a literal
const modules = import.meta.glob("/content/**/*.svelte", { eager: true });
```

Server content (logic) can live anywhere at runtime.
Client content (UI overrides) must live under a known root at build time.

---

## Unknown Aspect Fallback

When content defines a new aspect the frontend has no registered component for,
the view renderer needs a fallback rather than silent omission.

Policy:

- **Dev mode:** render unknown aspects as a labeled JSON dump. Visible, ugly, useful.
- **Prod mode:** omit silently.

This makes the content development loop faster — add an aspect, see its raw data
immediately without writing a component first. Write the component when the shape
is stable.

Implemented as a catch-all in the view renderer after the `ViewTypes` lookup fails.

---

## Hosting

- GitHub Pages: static landing page only. "Enter server IP" form. No game UI.
- Bun server: serves SvelteKit static build + API + WS on single port.
- Player navigates to `http://<server-ip>:<port>` — gets real game client from Bun.

Dev: SvelteKit on 5173, proxies `/api` and `/ws` to Bun on 3000.
Prod: everything on one port, `adapter-static` build served by Bun.
