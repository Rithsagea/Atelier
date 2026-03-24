import { expect, test } from 'bun:test'
import { TreapMap } from './SortedMap'

const asc = (a: number, b: number) => a - b
const desc = (a: number, b: number) => b - a

test('TreapMap: inserts entries in sorted order', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(3, 'c')
  map.set(1, 'a')
  map.set(2, 'b')
  expect(map.values()).toEqual(['a', 'b', 'c'])
})

test('TreapMap: respects custom comparator (descending)', () => {
  const map = new TreapMap<number, string>(desc)
  map.set(1, 'a')
  map.set(3, 'c')
  map.set(2, 'b')
  expect(map.values()).toEqual(['c', 'b', 'a'])
})

test('TreapMap: get returns value for existing key', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(5, 'five')
  expect(map.get(5)).toBe('five')
  expect(map.get(99)).toBeUndefined()
})

test('TreapMap: set overwrites existing key in place', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(1, 'a')
  map.set(2, 'b')
  map.set(3, 'c')
  map.set(2, 'B')
  expect(map.get(2)).toBe('B')
  expect(map.size).toBe(3)
  expect(map.values()).toEqual(['a', 'B', 'c'])
})

test('TreapMap: has returns true iff key is present', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(1, 'a')
  expect(map.has(1)).toBeTrue()
  expect(map.has(2)).toBeFalse()
})

test('TreapMap: delete removes an existing key and returns true', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(1, 'a')
  map.set(2, 'b')
  map.set(3, 'c')
  expect(map.delete(2)).toBeTrue()
  expect(map.has(2)).toBeFalse()
  expect(map.values()).toEqual(['a', 'c'])
})

test('TreapMap: delete returns false for missing key', () => {
  const map = new TreapMap<number, string>(asc)
  expect(map.delete(99)).toBeFalse()
})

test('TreapMap: size reflects number of entries', () => {
  const map = new TreapMap<number, string>(asc)
  expect(map.size).toBe(0)
  map.set(1, 'a')
  map.set(2, 'b')
  expect(map.size).toBe(2)
  map.delete(1)
  expect(map.size).toBe(1)
})

test('TreapMap: iteration yields entries in sorted key order', () => {
  const map = new TreapMap<number, string>(asc)
  map.set(10, 'ten')
  map.set(5, 'five')
  map.set(20, 'twenty')
  expect([...map]).toEqual([
    [5, 'five'],
    [10, 'ten'],
    [20, 'twenty'],
  ])
})
