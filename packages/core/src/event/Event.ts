import { getMethodLabels } from '../util/Util'
import { getMetadata, type AnyFunction } from '../serial/Metadata'
import { SortedSet, TreapSet } from '../util/SortedSet'

export interface Event<_ = any> {
  id: string
  name?: string
}

export function createEvent<D = void>(name?: string): Event<D> {
  return { id: crypto.randomUUID(), name }
}

type Method<E extends Event> = E extends Event<infer D> ? (data: D) => void : never

export interface Handler<E extends Event> {
  readonly listener?: object
  readonly method: Method<E>
  readonly priority: number
}

export function Subscribe<D>(event: Event<D>, priority: number = 0) {
  return (target: any, key: string, _descriptor: PropertyDescriptor) => {
    const fn = Reflect.get(target, key) as AnyFunction
    const metadata = getMetadata(fn)
    metadata.event = event
    metadata.priority = priority
  }
}

export class Emitter {
  private seq = 0
  private readonly seqMap = new WeakMap<Handler<any>, number>()
  private readonly compare = (a: Handler<any>, b: Handler<any>) => {
    const byPriority = b.priority - a.priority
    return byPriority !== 0 ? byPriority : this.seqMap.get(a)! - this.seqMap.get(b)!
  }
  private handlers: Record<string, SortedSet<Handler<any>>> = {}

  clearHandlers(): void {
    this.handlers = {}
  }

  addHandler<E extends Event>(event: E, handler: Handler<E>): void {
    if (!this.seqMap.has(handler)) this.seqMap.set(handler, this.seq++)
    ;(this.handlers[event.id] ??= new TreapSet(this.compare)).add(handler)
  }

  removeHandler<E extends Event>(event: E, handler: Handler<E>): boolean {
    return this.handlers[event.id]?.delete(handler) ?? false
  }

  addListener(listener: object): void {
    for (const label of getMethodLabels(listener)) {
      if (label === 'constructor') continue
      const method: Method<any> = Reflect.get(listener, label)
      const metadata = getMetadata(method, false)
      if (!metadata?.event) continue
      this.addHandler(metadata.event, {
        listener,
        method,
        priority: metadata.priority ?? 0,
      })
    }
  }

  addListeners(...listeners: (object | undefined)[]): void {
    for (const l of listeners) {
      if (l !== undefined) this.addListener(l)
    }
  }

  removeListener(listener: object): void {
    for (const label of getMethodLabels(listener)) {
      if (label === 'constructor') continue
      const method: Method<any> = Reflect.get(listener, label)
      const metadata = getMetadata(method, false)
      if (!metadata?.event) continue
      const set = this.handlers[metadata.event.id]
      if (!set) continue
      for (const handler of set) {
        if (handler.method === method) {
          set.delete(handler)
          break
        }
      }
    }
  }

  call<E extends Event<D>, D>(event: E, data?: D): void {
    const set = this.handlers[event.id]
    if (!set) return
    for (const handler of set) handler.method.call(handler.listener, data)
  }
}
