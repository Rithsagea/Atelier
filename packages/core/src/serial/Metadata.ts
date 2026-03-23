import type { Serializer } from './Data'
import type { Event } from '../event/Event'

export type AnyFunction = (...args: any[]) => any

export interface ObjectMetadata {
  propertyData: Record<string | symbol, Serializer<any>>
}

export interface MethodMetadata {
  event?: Event
  priority?: number
}

export function getMetadata(target: AnyFunction, generate: false): MethodMetadata | undefined
export function getMetadata(target: object, generate: false): ObjectMetadata | undefined
export function getMetadata(target: AnyFunction, generate?: boolean): MethodMetadata
export function getMetadata(target: object, generate?: boolean): ObjectMetadata
export function getMetadata(
  target: any,
  generate: boolean = true,
): ObjectMetadata | MethodMetadata | undefined {
  if (typeof target === 'function') {
    if (!target.$metadata && generate) target.$metadata = {} as MethodMetadata
    return target.$metadata
  } else {
    if (!target.constructor.$metadata && generate) {
      target.constructor.$metadata = { propertyData: {} } as ObjectMetadata
    }
    return target.constructor.$metadata
  }
}
