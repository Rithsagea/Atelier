import tseslint from 'typescript-eslint'

export default tseslint.config(
  // Ignored paths
  {
    ignores: ['node_modules', '**/node_modules', '.svelte-kit', 'build', 'dist', 'reference'],
  },

  // TypeScript files
  {
    files: ['**/*.ts'],
    extends: [tseslint.configs.recommended],
    rules: {
      // These fire too often on legitimate patterns in this codebase
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-unsafe-assignment': 'off',
      '@typescript-eslint/no-unsafe-member-access': 'off',
      '@typescript-eslint/no-unsafe-argument': 'off',
      '@typescript-eslint/no-unsafe-call': 'off',
      '@typescript-eslint/no-unsafe-return': 'off',

      // Decorators use parameter reassignment internally
      'no-param-reassign': 'off',

      // console.log is the logging strategy (see CLAUDE.md)
      'no-console': 'off',

      // TypeScript namespaces are used intentionally (e.g. Property namespace)
      '@typescript-eslint/no-namespace': 'off',

      // Allow unused vars prefixed with _ (intentional ignore)
      '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' },
      ],
    },
  },
)
