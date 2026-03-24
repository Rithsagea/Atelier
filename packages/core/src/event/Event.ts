import { getMetadata } from "../serial/Metadata";

export interface Event<_ = undefined> {
  id: symbol;
}

export function createEvent<D = undefined>(name?: string): Event<D> {
  return { id: Symbol(name) };
}

type SubscriptionData = Record<symbol, Function[]>;
const SUBSCRIPTION_SYMBOL = Symbol("subscriptions");

function getSubscriptionData(target: object): SubscriptionData {
  const proto = (target as any).constructor?.prototype ?? target;
  return getMetadata<SubscriptionData>(proto, SUBSCRIPTION_SYMBOL, {});
}

export function Subscribe<D>(event: Event<D>) {
  return (target: object, key: string, _descriptor: PropertyDescriptor) => {
    const data = getSubscriptionData(target);
    if (!data[event.id]) data[event.id] = [];
    data[event.id]!.push(Reflect.get(target, key));
  };
}

export function emit<D>(target: object, event: Event<D>, data?: D): void {
  const subscriptions = getSubscriptionData(target)[event.id] ?? [];
  for (const handler of subscriptions) handler.call(target, data);
}
