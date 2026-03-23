import { getMethodLabels } from '../util/Util'
import { getMetadata, type AnyFunction } from '../serial/Metadata'

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
  private handlers: Record<string, Handler<any>[]> = {}

  clearHandlers(): void {
    this.handlers = {}
  }

  addHandler<E extends Event>(event: E, handler: Handler<E>): void {
    const id = event.id
    if (!this.handlers[id]) this.handlers[id] = []
    this.handlers[id].push(handler)
    this.handlers[id].sort((a, b) => b.priority - a.priority)
  }

  removeHandler<E extends Event>(event: E, handler: Handler<E>): boolean {
    const handlers = this.handlers[event.id]
    if (!handlers) return false
    this.handlers[event.id] = handlers.filter((h) => h !== handler)
    return handlers.length !== this.handlers[event.id].length
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
      const handlers = this.handlers[metadata.event.id]
      if (handlers) {
        this.handlers[metadata.event.id] = handlers.filter((h) => h.method !== method)
      }
    }
  }

  call<E extends Event<D>, D>(event: E, data?: D): void {
    this.handlers[event.id]?.forEach((handler) => handler.method.call(handler.listener, data))
  }
}
