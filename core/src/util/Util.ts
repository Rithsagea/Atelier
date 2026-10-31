// A value carries no content when it is undefined or an empty plain object.
export function isEmpty(value: unknown): boolean {
  return (
    value === undefined ||
    (typeof value === "object" &&
      value !== null &&
      value.constructor === Object &&
      Object.keys(value).length === 0)
  );
}
