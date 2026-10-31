export type Constructor<T extends object = object> = new () => T;

export function enumMap<K extends string, V>(keys: readonly K[], fn: (key: K) => V): Record<K, V> {
  return keys.reduce((res, key) => ({ ...res, [key]: fn(key) }), {} as Record<K, V>);
}
