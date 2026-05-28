import { BiMap } from "../util/Algorithms";
import { type Constructor } from "../util/Types";
import { getMetadata } from "./Metadata";

export type Context = BiMap<string, Constructor>;
export type SerializedObject = Record<string, unknown>;

export interface SerializationStrategy<T = any> {
  serialize(source: T): any;
  deserialize(source: any): T;
}

// --- Property data ---

type PropertyData = Record<string, SerializationStrategy>;
const PROPERTY_SYMBOL = Symbol("properties");

function ownPropertyData(target: object): PropertyData {
  const proto = (target as any).constructor?.prototype ?? target;
  return getMetadata<PropertyData>(proto, PROPERTY_SYMBOL, {});
}

function getPropertyData(target: object): PropertyData {
  const chain: object[] = [];
  let proto: object | null = Object.getPrototypeOf(target);
  while (proto && proto !== Object.prototype) {
    chain.push(proto);
    proto = Object.getPrototypeOf(proto);
  }
  const merged: PropertyData = {};
  for (let i = chain.length - 1; i >= 0; i--) {
    Object.assign(merged, getMetadata<PropertyData>(chain[i]!, PROPERTY_SYMBOL, {}));
  }
  return merged;
}

// --- serialize / deserialize ---

export function serialize(target: object): SerializedObject {
  const res: SerializedObject = {};
  const properties = getPropertyData(target);
  for (const [label, strategy] of Object.entries(properties)) {
    const value = (target as SerializedObject)[label];
    if (value === undefined) continue;
    const out = strategy.serialize(value);
    if (out !== undefined) res[label] = out;
  }
  return res;
}

export function deserialize<T extends object>(
  source: SerializedObject,
  constructor: Constructor<T>,
): T {
  const res = new constructor();
  const properties = getPropertyData(res);
  for (const [label, strategy] of Object.entries(properties)) {
    const value = source[label];
    if (value === undefined) continue;
    (res as SerializedObject)[label] = strategy.deserialize(value);
  }
  return res;
}

// --- Strategies ---

function PrimitiveStrategy(): SerializationStrategy {
  return { serialize: (i) => i, deserialize: (i) => i };
}

function ClassStrategy<T extends object>(constructor: Constructor<T>): SerializationStrategy<T> {
  return {
    serialize: (source) => serialize(source),
    deserialize: (source) => deserialize(source, constructor),
  };
}

function MultiStrategy(context: Context): SerializationStrategy {
  return {
    serialize(source) {
      return { ...serialize(source), $type: context.inverse().get(source.constructor) };
    },
    deserialize(source) {
      return deserialize(source, context.get(source.$type)!);
    },
  };
}

function ListStrategy<T>(base: SerializationStrategy<T>): SerializationStrategy<T[]> {
  return {
    serialize: (source) => source.map((i) => base.serialize(i)),
    deserialize: (source) => (source as unknown[]).map((i) => base.deserialize(i)),
  };
}

function MapStrategy<T>(base: SerializationStrategy<T>): SerializationStrategy<Record<string, T>> {
  return {
    serialize(source) {
      const res: SerializedObject = {};
      for (const k in source) res[k] = base.serialize(source[k]!);
      return res;
    },
    deserialize(source) {
      const res: Record<string, T> = {};
      for (const k in source) res[k] = base.deserialize(source[k]);
      return res;
    },
  };
}

// --- Property decorator namespace ---

function getStrategy(
  context?: Constructor | Context | SerializationStrategy,
): SerializationStrategy {
  if (!context) return PrimitiveStrategy();
  if (typeof context === "function") return ClassStrategy(context);
  if (context instanceof BiMap) return MultiStrategy(context as Context);
  return context;
}

export namespace Property {
  export function Serialize(strategy: SerializationStrategy): PropertyDecorator {
    return (target: object, key: string | symbol) => {
      ownPropertyData(target)[key as string] = strategy;
    };
  }

  export const Primitive: PropertyDecorator = (target, key) => {
    ownPropertyData(target)[key as string] = PrimitiveStrategy();
  };

  export function Class<T extends object>(constructor: Constructor<T>): PropertyDecorator {
    return Serialize(ClassStrategy(constructor));
  }

  export function Multi(context: Context): PropertyDecorator {
    return Serialize(MultiStrategy(context));
  }

  export function List(context?: Constructor | Context | SerializationStrategy): PropertyDecorator {
    return Serialize(ListStrategy(getStrategy(context)));
  }

  export function Map(context?: Constructor | Context | SerializationStrategy): PropertyDecorator {
    return Serialize(MapStrategy(getStrategy(context)));
  }
}
