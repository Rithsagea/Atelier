export function getMethodLabels(obj: object): string[] {
  const labels = new Set<string>()
  for (let proto = Object.getPrototypeOf(obj); proto; proto = Object.getPrototypeOf(proto)) {
    for (const label of Object.getOwnPropertyNames(proto)) {
      labels.add(label)
    }
  }
  return [...labels].filter((label) => typeof Reflect.get(obj, label) === 'function')
}
