# ENV — Project Organization & Environment

*Load when: setting up the project, touching build config, adding a package, or debugging
imports/aliases.*

---

## Monorepo Structure

```
packages/
├── core/        # shared types, primitives, base classes — no Bun APIs, no Svelte
├── server/      # Hono server, game engine, SRD content, CLI
└── client/      # SvelteKit frontend

content/         # user-defined homebrew (explicit index.ts entry point)
CLAUDE.md        # router + invariants
```

---

## Package aliases

All imports use package aliases. **Never use relative paths that cross package boundaries.**

```ts
import { Aspect } from "@atelier/core/composite/Aspect";
import { Subscribe } from "@atelier/core/event/Event";
import { AbilityScoresAspect } from "@atelier/server/aspects/AbilityScores";
```

`@atelier/core/*` for core primitives. `@atelier/server/*` for server internals.
Content files use both — never `../../packages/`.

---

## tsconfig

### Root `tsconfig.json`

```json
{
  "compilerOptions": {
    "strict": true,
    "experimentalDecorators": true,
    "emitDecoratorMetadata": true,
    "moduleResolution": "bundler",
    "target": "ESNext",
    "module": "ESNext"
  }
}
```

### `packages/core/tsconfig.json`

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "rootDir": "src",
    "paths": {
      "@atelier/core/*": ["./src/*"]
    }
  },
  "include": ["src"]
}
```

### `packages/server/tsconfig.json`

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "paths": {
      "@atelier/core/*": ["../core/src/*"],
      "@atelier/server/*": ["./src/*"]
    }
  },
  "include": ["src", "../../content"]
}
```

Note `content` is included here — this gives content files type-checking and alias
resolution without any extra config on the content author's side.

### `packages/client/tsconfig.json`

```json
{
  "extends": "../../tsconfig.json",
  "compilerOptions": {
    "paths": {
      "$lib/*": ["./src/lib/*"],
      "@atelier/core/*": ["../core/src/*"]
    }
  }
}
```

### `content/tsconfig.json`

```json
{
  "extends": "../packages/server/tsconfig.json"
}
```

Extends server tsconfig so content files get `@atelier/core` and `@atelier/server`
aliases automatically. Content authors never touch tsconfig.

---

## package.json

### Root `package.json`

```json
{
  "name": "atelier",
  "workspaces": ["packages/*"]
}
```

### `packages/core/package.json`

```json
{
  "name": "@atelier/core",
  "module": "src/index.ts"
}
```

### `packages/server/package.json`

```json
{
  "name": "@atelier/server",
  "dependencies": {
    "@atelier/core": "workspace:*"
  }
}
```

`workspace:*` resolves to the local package directly — no symlinks, no `bun link`.

---

## LSP

The setup works identically in AstroNvim, VSCode, and any editor using `tsserver`
and `svelte-language-server`. Both servers read `tsconfig.json` paths natively.

VSCode note: if you open the monorepo root folder, VSCode occasionally picks up the
wrong `tsconfig.json` for a file. Fix with a `.code-workspace` file if this occurs —
it's a VSCode UI issue, not a TypeScript config issue.
