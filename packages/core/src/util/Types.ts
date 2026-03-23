export type Primitive = bigint | boolean | null | number | string | symbol | undefined

export function isPrimitive(val: unknown): val is Primitive {
  return val !== Object(val)
}

export type Constructor<T extends object = object> = new () => T

export function isConstructor<T extends object>(obj: unknown): obj is Constructor<T> {
  return typeof obj === 'function' && 'prototype' in (obj as object)
}

export function enumMap<K extends string, V>(keys: readonly K[], fn: (key: K) => V): Record<K, V> {
  return keys.reduce((res, key) => ({ ...res, [key]: fn(key) }), {} as Record<K, V>)
}
