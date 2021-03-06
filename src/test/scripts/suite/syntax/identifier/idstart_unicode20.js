/*
 * Copyright (c) André Bargull
 * Alle Rechte vorbehalten / All Rights Reserved.  Use is subject to license terms.
 *
 * <https://github.com/anba/es6draft>
 */
function test(start, end) {
  for (let cp = start; cp <= end;) {
    let source = "var obj = {};\n";
    for (let i = 0; cp <= end && i < 1000; ++cp, ++i) {
      source += `obj.${String.fromCodePoint(cp)};\n`;
    }
    eval(source);
  }
}

// Delta compared to Unicode 1.0
test(0x00aa, 0x00aa);
test(0x00b5, 0x00b5);
test(0x00ba, 0x00ba);
test(0x00f8, 0x01a9);
test(0x01c4, 0x01f5);
test(0x01fa, 0x0217);
test(0x02b0, 0x02b8);
test(0x02bb, 0x02c1);
test(0x02d0, 0x02d1);
test(0x02e0, 0x02e4);
test(0x037a, 0x037a);
test(0x03da, 0x03da);
test(0x03dc, 0x03dc);
test(0x03de, 0x03de);
test(0x03e0, 0x03e0);
test(0x03e2, 0x03f2);
test(0x03f3, 0x03f3);
test(0x04c1, 0x04c4);
test(0x04c7, 0x04c8);
test(0x04cb, 0x04cc);
test(0x04d0, 0x04eb);
test(0x04ee, 0x04f5);
test(0x04f8, 0x04f9);
test(0x0561, 0x0587);
test(0x0640, 0x0640);
test(0x0641, 0x064a);
test(0x06e5, 0x06e6);
test(0x0a72, 0x0a74);
test(0x0a8d, 0x0a8d);
test(0x0a8f, 0x0a91);
test(0x0abd, 0x0abd);
test(0x0b3d, 0x0b3d);
test(0x0f40, 0x0f47);
test(0x0f49, 0x0f69);
test(0x1100, 0x1159);
test(0x115f, 0x11a2);
test(0x11a8, 0x11f9);
test(0x1e00, 0x1e9b);
test(0x1ea0, 0x1ef9);
test(0x1f00, 0x1f15);
test(0x1f18, 0x1f1d);
test(0x1f20, 0x1f45);
test(0x1f48, 0x1f4d);
test(0x1f50, 0x1f57);
test(0x1f59, 0x1f59);
test(0x1f5b, 0x1f5b);
test(0x1f5d, 0x1f5d);
test(0x1f5f, 0x1f7d);
test(0x1f80, 0x1fb4);
test(0x1fb6, 0x1fbc);
test(0x1fbe, 0x1fbe);
test(0x1fc2, 0x1fc4);
test(0x1fc6, 0x1fcc);
test(0x1fd0, 0x1fd3);
test(0x1fd6, 0x1fdb);
test(0x1fe0, 0x1fec);
test(0x1ff2, 0x1ff4);
test(0x1ff6, 0x1ffc);
test(0x207f, 0x207f);
test(0x2102, 0x2102);
test(0x2107, 0x2107);
test(0x210a, 0x2113);
test(0x2115, 0x2115);
test(0x2118, 0x211d);
test(0x2124, 0x2124);
test(0x2126, 0x2126);
test(0x2128, 0x2128);
test(0x212a, 0x2131);
test(0x2133, 0x2134);
test(0x2135, 0x2138);
test(0x2160, 0x2182);
test(0x3005, 0x3005);
test(0x3007, 0x3007);
test(0x3021, 0x3029);
test(0x3031, 0x3035);
test(0x30a1, 0x30fa);
test(0x4e00, 0x9fa5);
test(0xac00, 0xd7a3);
test(0xf900, 0xfa2d);
test(0xfb00, 0xfb06);
test(0xfb13, 0xfb17);
test(0xfb1f, 0xfb28);
test(0xfb2a, 0xfb36);
test(0xfb38, 0xfb3c);
test(0xfb3e, 0xfb3e);
test(0xfb40, 0xfb41);
test(0xfb43, 0xfb44);
test(0xfb46, 0xfbb1);
test(0xfbd3, 0xfd3d);
test(0xfd50, 0xfd8f);
test(0xfd92, 0xfdc7);
test(0xfdf0, 0xfdfb);
test(0xff70, 0xff70);
test(0xff9e, 0xff9f);
