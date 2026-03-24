export interface SortedMap<K, V> {
  set(key: K, value: V): void
  get(key: K): V | undefined
  has(key: K): boolean
  delete(key: K): boolean
  readonly size: number
  values(): V[]
  [Symbol.iterator](): Iterator<[K, V]>
}

// --- Treap ---

type TreapNode<K, V> = {
  key: K
  value: V
  priority: number
  left: TreapNode<K, V> | null
  right: TreapNode<K, V> | null
}

function treapNode<K, V>(key: K, value: V): TreapNode<K, V> {
  return { key, value, priority: Math.random(), left: null, right: null }
}

function rotateRight<K, V>(n: TreapNode<K, V>): TreapNode<K, V> {
  const l = n.left!
  n.left = l.right
  l.right = n
  return l
}

function rotateLeft<K, V>(n: TreapNode<K, V>): TreapNode<K, V> {
  const r = n.right!
  n.right = r.left
  r.left = n
  return r
}

function treapInsert<K, V>(
  root: TreapNode<K, V> | null,
  key: K,
  value: V,
  cmp: (a: K, b: K) => number,
): [TreapNode<K, V>, boolean] {
  if (!root) return [treapNode(key, value), true]
  const c = cmp(key, root.key)
  if (c === 0) {
    root.value = value
    return [root, false]
  }
  let inserted: boolean
  if (c < 0) {
    ;[root.left, inserted] = treapInsert(root.left, key, value, cmp)
    if (root.left.priority > root.priority) root = rotateRight(root)
  } else {
    ;[root.right, inserted] = treapInsert(root.right, key, value, cmp)
    if (root.right.priority > root.priority) root = rotateLeft(root)
  }
  return [root, inserted]
}

function treapDelete<K, V>(
  root: TreapNode<K, V> | null,
  key: K,
  cmp: (a: K, b: K) => number,
): [TreapNode<K, V> | null, boolean] {
  if (!root) return [null, false]
  const c = cmp(key, root.key)
  if (c < 0) {
    let deleted: boolean
    ;[root.left, deleted] = treapDelete(root.left, key, cmp)
    return [root, deleted]
  }
  if (c > 0) {
    let deleted: boolean
    ;[root.right, deleted] = treapDelete(root.right, key, cmp)
    return [root, deleted]
  }
  // found — rotate down to a leaf, then drop
  if (!root.left && !root.right) return [null, true]
  if (!root.left || (root.right && root.right.priority > root.left.priority)) {
    root = rotateLeft(root)
    ;[root.left] = treapDelete(root.left, key, cmp)
  } else {
    root = rotateRight(root)
    ;[root.right] = treapDelete(root.right, key, cmp)
  }
  return [root, true]
}

function treapFind<K, V>(
  root: TreapNode<K, V> | null,
  key: K,
  cmp: (a: K, b: K) => number,
): V | undefined {
  if (!root) return undefined
  const c = cmp(key, root.key)
  if (c === 0) return root.value
  return treapFind(c < 0 ? root.left : root.right, key, cmp)
}

function treapInorder<K, V>(root: TreapNode<K, V> | null, out: [K, V][]): void {
  if (!root) return
  treapInorder(root.left, out)
  out.push([root.key, root.value])
  treapInorder(root.right, out)
}

export class TreapMap<K, V> implements SortedMap<K, V> {
  private root: TreapNode<K, V> | null = null
  private _size = 0

  constructor(private readonly compare: (a: K, b: K) => number) {}

  set(key: K, value: V): void {
    let inserted: boolean
    ;[this.root, inserted] = treapInsert(this.root, key, value, this.compare)
    if (inserted) this._size++
  }

  get(key: K): V | undefined {
    return treapFind(this.root, key, this.compare)
  }

  has(key: K): boolean {
    return treapFind(this.root, key, this.compare) !== undefined
  }

  delete(key: K): boolean {
    let deleted: boolean
    ;[this.root, deleted] = treapDelete(this.root, key, this.compare)
    if (deleted) this._size--
    return deleted
  }

  get size(): number {
    return this._size
  }

  values(): V[] {
    const out: [K, V][] = []
    treapInorder(this.root, out)
    return out.map(([, v]) => v)
  }

  [Symbol.iterator](): Iterator<[K, V]> {
    const out: [K, V][] = []
    treapInorder(this.root, out)
    return out[Symbol.iterator]()
  }
}
