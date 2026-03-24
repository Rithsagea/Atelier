import { TreapMap } from './SortedMap'

export interface SortedSet<T> {
  add(value: T): void
  has(value: T): boolean
  delete(value: T): boolean
  readonly size: number
  [Symbol.iterator](): Iterator<T>
}

export class TreapSet<T> implements SortedSet<T> {
  private readonly map: TreapMap<T, null>

  constructor(compare: (a: T, b: T) => number) {
    this.map = new TreapMap(compare)
  }

  add(value: T): void {
    this.map.set(value, null)
  }

  has(value: T): boolean {
    return this.map.has(value)
  }

  delete(value: T): boolean {
    return this.map.delete(value)
  }

  get size(): number {
    return this.map.size
  }

  [Symbol.iterator](): Iterator<T> {
    return (function* (map) {
      for (const [k] of map) yield k
    })(this.map)
  }
}
