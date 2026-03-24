import { getMetadata } from "./Metadata";
import { isPrimitive, type Constructor } from "../util/Types";
import { createEvent, Emitter } from "../event/Event";

// --- Serializer interface ---

export interface Serializer<V> {
  serialize(value: V): any;
  deserialize(raw: any): V;
}

export function isSerializer<V>(obj: unknown): obj is Serializer<V> {
  return (
    typeof (obj as any)?.serialize === "function" && typeof (obj as any)?.deserialize === "function"
  );
}

// --- TypeMap ---

export class TypeMap<T extends object> {
  private readonly map = new Map<string, Constructor<T>[]>();

  add(ctor: Constructor<T>): void {
    const list = this.map.get(ctor.name) ?? [];
    if (!list.includes(ctor)) list.push(ctor);
    this.map.set(ctor.name, list);
  }

  hash(ctor: Constructor): string | undefined {
    const list = this.map.get(ctor.name);
    if (!list) return undefined;
    const idx = list.indexOf(ctor as Constructor<T>);
    if (idx === -1) return undefined;
    return list.length > 1 ? `${ctor.name}@${idx + 1}` : ctor.name;
  }

  get(hash: string): Constructor<T> {
    const [name, idxStr] = hash.split("@");
    const list = this.map.get(name);
    if (!list?.length) throw new Error(`TypeMap: unknown type "${hash}"`);
    return list[idxStr ? parseInt(idxStr) - 1 : 0];
  }

  values(): Constructor<T>[] {
    return [...this.map.values()].flat();
  }
}

export function isTypeMap<T extends object>(obj: unknown): obj is TypeMap<T> {
  return obj instanceof TypeMap;
}

export function Register<T extends object>(typeMap: TypeMap<T>) {
  return (ctor: Constructor<T>) => typeMap.add(ctor);
}

// --- InitializeObjectEvent ---

export const InitializeObjectEvent = createEvent("InitializeObjectEvent");

// --- Core serialize / deserialize ---

export type SerializableValue = Primitive | any[] | object;
type Primitive = string | number | boolean | bigint | symbol | null | undefined;

export function serialize(value: unknown): any {
  if (typeof value === "function") return undefined;
  if (isPrimitive(value)) return value;
  if (Array.isArray(value)) return value.map(serialize);

  const properties = getMetadata(value as object, false)?.propertyData;
  if (!properties) return { ...(value as object) };

  const res: Record<string, any> = {};
  for (const [k, v] of Object.entries(value as object)) {
    if (v === undefined) continue;
    const serializer = properties[k];
    if (serializer === undefined) continue;
    res[k] = Array.isArray(v)
      ? (v as unknown[]).map((item) => serializer.serialize(item))
      : serializer.serialize(v);
  }
  return res;
}

export function deserialize<T extends object>(data: any, ctor: Constructor<T>): T;
export function deserialize<D>(data: D): D;
export function deserialize(data: any, ctor?: Constructor): any {
  if (!ctor) return data;

  const res = new ctor();
  const properties = getMetadata(res).propertyData;

  for (const [k, v] of Object.entries(data)) {
    const serializer = properties[k];
    Reflect.set(
      res,
      k,
      serializer === undefined
        ? v
        : Array.isArray(v)
          ? (v as any[]).map((item) => serializer.deserialize(item))
          : serializer.deserialize(v),
    );
  }

  const emitter = new Emitter();
  emitter.addListener(res);
  emitter.call(InitializeObjectEvent);

  return res;
}

// --- Built-in serializers ---

const DefaultSerializer: Serializer<any> = { serialize, deserialize };

export class ClassSerializer<T extends object> implements Serializer<T> {
  constructor(private readonly ctor: Constructor<T>) {}
  serialize(obj: T): any {
    return serialize(obj);
  }
  deserialize(raw: any): T {
    return deserialize(raw, this.ctor);
  }
}

export class MultiSerializer<T extends object> implements Serializer<T> {
  constructor(private readonly typeMap: TypeMap<T>) {}

  serialize(obj: T): any {
    const res = serialize(obj);
    res.$type = this.typeMap.hash(obj.constructor as Constructor);
    return res;
  }

  deserialize(raw: any): T {
    const { $type, ...data } = raw as { $type: string; [k: string]: unknown };
    if (!$type) throw new Error("MultiSerializer: missing $type tag");
    return deserialize(data, this.typeMap.get($type));
  }
}

export class ListSerializer<T> implements Serializer<T[]> {
  constructor(private readonly item?: Serializer<T>) {}

  serialize(arr: T[]): any[] {
    return this.item ? arr.map((v) => this.item!.serialize(v)) : arr.map(serialize);
  }

  deserialize(raw: any[]): T[] {
    return this.item ? raw.map((v) => this.item!.deserialize(v)) : raw;
  }
}

export class MapSerializer<V> implements Serializer<Record<string, V>> {
  constructor(private readonly value?: Serializer<V>) {}

  serialize(rec: Record<string, V>): any {
    if (!this.value) return rec;
    return Object.fromEntries(Object.entries(rec).map(([k, v]) => [k, this.value!.serialize(v)]));
  }

  deserialize(raw: Record<string, any>): Record<string, V> {
    if (!this.value) return raw as Record<string, V>;
    return Object.fromEntries(Object.entries(raw).map(([k, v]) => [k, this.value!.deserialize(v)]));
  }
}

// --- Property decorator namespace ---

function makeDecorator(serializer: Serializer<any>): PropertyDecorator {
  return (target: object, key: string | symbol) => {
    getMetadata(target).propertyData[key] = serializer;
  };
}

export namespace Property {
  export const Primitive: PropertyDecorator = makeDecorator(DefaultSerializer);

  export function Class<T extends object>(ctor: Constructor<T>): PropertyDecorator {
    return makeDecorator(new ClassSerializer(ctor));
  }

  export function Multi<T extends object>(typeMap: TypeMap<T>): PropertyDecorator {
    return makeDecorator(new MultiSerializer(typeMap));
  }

  export function List<T>(serializer?: Serializer<T>): PropertyDecorator {
    return makeDecorator(new ListSerializer(serializer));
  }

  export function Map<T>(serializer?: Serializer<T>): PropertyDecorator {
    return makeDecorator(new MapSerializer(serializer));
  }
}
