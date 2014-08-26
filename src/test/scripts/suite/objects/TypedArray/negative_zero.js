/*
 * Copyright (c) 2012-2014 André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */

const {
  assertSame, assertThrows, assertTrue
} = Assert;

// -0 as property key

assertSame(-0, +"-0");
assertThrows(() => Object.defineProperty(new Int8Array(0), "-0", {value: 123}), TypeError);
assertThrows(() => Object.defineProperty(new Int8Array(1), "-0", {value: 123}), TypeError);

assertSame(-0, +"-0.");
assertTrue(Reflect.defineProperty(new Int8Array(0), "-0.", {value: 123}));
assertTrue(Reflect.defineProperty(new Int8Array(0), "-0.", {value: 123}));

assertSame(-0, +"-0.0");
assertTrue(Reflect.defineProperty(new Int8Array(0), "-0.0", {value: 123}));
assertTrue(Reflect.defineProperty(new Int8Array(0), "-0.0", {value: 123}));
