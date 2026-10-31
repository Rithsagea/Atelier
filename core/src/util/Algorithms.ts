export class BiMap<K, V> {
  constructor(
    private readonly forward = new Map<K, V>(),
    private readonly backward = new Map<V, K>(),
  ) {}

  set(k: K, v: V): boolean {
    if (this.forward.has(k) || this.backward.has(v)) return false;
    this.forward.set(k, v);
    this.backward.set(v, k);
    return true;
  }

  get(k: K): V | undefined {
    return this.forward.get(k);
  }

  inverse(): BiMap<V, K> {
    return new BiMap(this.backward, this.forward);
  }

  static from<K extends string, V>(data: Record<K, V>): BiMap<K, V> {
    const res = new BiMap<K, V>();
    for (const [k, v] of Object.entries(data) as [K, V][]) {
      if (!res.set(k, v)) throw new Error("BiMap: non-unique mapping");
    }
    return res;
  }
}
