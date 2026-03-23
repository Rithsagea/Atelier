export class Aspect<T> {
  readonly id: symbol
  readonly name: string

  constructor(name: string) {
    this.name = name
    this.id = Symbol(name)
  }
}
