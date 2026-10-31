import assert from "node:assert/strict";
import { mock, test } from "node:test";
import { createEvent, emit, Subscribe } from "./Event";

test("event subscription", () => {
  const TestEvent = createEvent();
  const fn = mock.fn();

  class Listener {
    @Subscribe(TestEvent)
    listen() {
      fn();
    }
  }

  emit(new Listener(), TestEvent);
  assert.equal(fn.mock.callCount(), 1);
});

test("emit event data", () => {
  const MessageEvent = createEvent<string>();
  const testMessage = "This is a test message!";
  const fn = mock.fn();

  class Listener {
    @Subscribe(MessageEvent)
    listen(message: string) {
      fn(message);
    }
  }

  emit(new Listener(), MessageEvent, testMessage);
  assert.deepEqual(fn.mock.calls[0]?.arguments, [testMessage]);
});
