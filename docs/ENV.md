# ENV — Project Organization & Environment

_Load when: setting up the project, touching build config, adding a package, or debugging
imports/aliases._

---

## Monorepo Structure

```
packages/
├── core/        # shared types, primitives, base classes — no Bun APIs, no Svelte
├── server/      # Hono server, game engine, SRD content, CLI
├── client/      # SvelteKit frontend (not yet created)
└── playground/  # scratch pad for manual testing of core primitives

content/         # user-defined homebrew (not yet created; explicit index.ts entry point)
CLAUDE.md        # router + invariants
```

---

## Package aliases

All imports use package aliases. **Never use relative paths that cross package boundaries.**

```ts
import { Composite } from "@atelier/core/composite/Composite";
import { Subscribe } from "@atelier/core/event/Event";
import { SomeServerThing } from "@atelier/server/path/to/module";
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
  "include": ["src"]
}
```

When the `content/` directory is added, extend `include` to `["src", "../../content"]` so
content files get type-checking and alias resolution without extra config.

### `packages/client/tsconfig.json` (planned)

`packages/client` will be the SvelteKit frontend. It needs `$lib/*` (SvelteKit's built-in
alias for `src/lib`) and `@atelier/core/*` for shared types. No `@atelier/server` alias —
the client never imports server internals directly.

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

### `content/tsconfig.json` (planned)

`content/` will hold user-defined homebrew — classes, races, spells, items. It extends the
server tsconfig so content files automatically get both `@atelier/core` and `@atelier/server`
aliases. Content authors never touch tsconfig.

```json
{
  "extends": "../packages/server/tsconfig.json"
}
```

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
  "module": "src/index.ts",
  "type": "module",
  "exports": { "./*": "./src/*" }
}
```

The `exports` map is what resolves subpath imports like `@atelier/core/composite/Composite`
to `./src/composite/Composite.ts`. The `module` field is a bundler convention (Bun, Rollup,
Vite) for the ESM entry point used when the package is imported without a subpath
(`import "@atelier/core"`); it does not affect subpath imports.

### `packages/server/package.json`

```json
{
  "name": "@atelier/server",
  "module": "src/index.ts",
  "type": "module",
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
