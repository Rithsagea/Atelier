const store = new WeakMap<object, Map<symbol, any>>();

export function getMetadata<T>(target: object, symbol: symbol, defaultValue: T): T {
  let symbolMap = store.get(target);
  if (!symbolMap) {
    symbolMap = new Map();
    store.set(target, symbolMap);
  }
  if (!symbolMap.has(symbol)) symbolMap.set(symbol, defaultValue);
  return symbolMap.get(symbol)!;
}
