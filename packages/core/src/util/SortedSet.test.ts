import { expect, test } from 'bun:test'
import { TreapSet, type SortedSet } from './SortedSet'

const asc = (a: number, b: number) => a - b
const desc = (a: number, b: number) => b - a

function testSortedSet(name: string, make: <T>(cmp: (a: T, b: T) => number) => SortedSet<T>) {
  test(`${name}: iterates in sorted order`, () => {
    const set = make<number>(asc)
    set.add(3)
    set.add(1)
    set.add(2)
    expect([...set]).toEqual([1, 2, 3])
  })

  test(`${name}: respects custom comparator (descending)`, () => {
    const set = make<number>(desc)
    set.add(1)
    set.add(3)
    set.add(2)
    expect([...set]).toEqual([3, 2, 1])
  })

  test(`${name}: has returns true iff value is present`, () => {
    const set = make<number>(asc)
    set.add(5)
    expect(set.has(5)).toBeTrue()
    expect(set.has(99)).toBeFalse()
  })

  test(`${name}: add is idempotent`, () => {
    const set = make<number>(asc)
    set.add(1)
    set.add(1)
    expect(set.size).toBe(1)
    expect([...set]).toEqual([1])
  })

  test(`${name}: delete removes an existing value and returns true`, () => {
    const set = make<number>(asc)
    set.add(1)
    set.add(2)
    set.add(3)
    expect(set.delete(2)).toBeTrue()
    expect(set.has(2)).toBeFalse()
    expect([...set]).toEqual([1, 3])
  })

  test(`${name}: delete returns false for missing value`, () => {
    const set = make<number>(asc)
    expect(set.delete(99)).toBeFalse()
  })

  test(`${name}: size reflects number of elements`, () => {
    const set = make<number>(asc)
    expect(set.size).toBe(0)
    set.add(1)
    set.add(2)
    expect(set.size).toBe(2)
    set.delete(1)
    expect(set.size).toBe(1)
  })
}

testSortedSet('TreapSet', (cmp) => new TreapSet(cmp))
