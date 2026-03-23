export class Aspect<T> {
  declare readonly _type: T // phantom — constrains Actor.get/provide to T
  readonly id: symbol
  readonly name: string

  constructor(name: string) {
    this.name = name
    this.id = Symbol(name)
  }
}
