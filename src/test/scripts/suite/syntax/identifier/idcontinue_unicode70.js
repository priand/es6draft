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
      source += `obj.A${String.fromCodePoint(cp)};\n`;
    }
    eval(source);
  }
}

// Delta compared to Unicode 6.3
test(0x037f, 0x037f);
test(0x048a, 0x052f);
test(0x08a0, 0x08b2);
test(0x08e4, 0x0902);
test(0x0972, 0x0980);
test(0x0c00, 0x0c00);
test(0x0c2a, 0x0c39);
test(0x0c81, 0x0c81);
test(0x0d01, 0x0d01);
test(0x0de6, 0x0def);
test(0x16f1, 0x16f8);
test(0x1900, 0x191e);
test(0x1ab0, 0x1abd);
test(0x1bab, 0x1bad);
test(0x1cf8, 0x1cf9);
test(0x1dc0, 0x1df5);
test(0xa680, 0xa69b);
test(0xa69c, 0xa69d);
test(0xa790, 0xa7ad);
test(0xa7b0, 0xa7b1);
test(0xa7f7, 0xa7f7);
test(0xa9e0, 0xa9e4);
test(0xa9e5, 0xa9e5);
test(0xa9e6, 0xa9e6);
test(0xa9e7, 0xa9ef);
test(0xa9f0, 0xa9f9);
test(0xa9fa, 0xa9fe);
test(0xaa7c, 0xaa7c);
test(0xaa7d, 0xaa7d);
test(0xaa7e, 0xaaaf);
test(0xab30, 0xab5a);
test(0xab5c, 0xab5f);
test(0xab64, 0xab65);
test(0xfe20, 0xfe2d);
test(0x102e0, 0x102e0);
test(0x10300, 0x1031f);
test(0x10350, 0x10375);
test(0x10376, 0x1037a);
test(0x10500, 0x10527);
test(0x10530, 0x10563);
test(0x10600, 0x10736);
test(0x10740, 0x10755);
test(0x10760, 0x10767);
test(0x10860, 0x10876);
test(0x10880, 0x1089e);
test(0x10a80, 0x10a9c);
test(0x10ac0, 0x10ac7);
test(0x10ac9, 0x10ae4);
test(0x10ae5, 0x10ae6);
test(0x10b80, 0x10b91);
test(0x1107f, 0x11081);
test(0x11150, 0x11172);
test(0x11173, 0x11173);
test(0x11176, 0x11176);
test(0x111da, 0x111da);
test(0x11200, 0x11211);
test(0x11213, 0x1122b);
test(0x1122c, 0x1122e);
test(0x1122f, 0x11231);
test(0x11232, 0x11233);
test(0x11234, 0x11234);
test(0x11235, 0x11235);
test(0x11236, 0x11237);
test(0x112b0, 0x112de);
test(0x112df, 0x112df);
test(0x112e0, 0x112e2);
test(0x112e3, 0x112ea);
test(0x112f0, 0x112f9);
test(0x11301, 0x11301);
test(0x11302, 0x11303);
test(0x11305, 0x1130c);
test(0x1130f, 0x11310);
test(0x11313, 0x11328);
test(0x1132a, 0x11330);
test(0x11332, 0x11333);
test(0x11335, 0x11339);
test(0x1133c, 0x1133c);
test(0x1133d, 0x1133d);
test(0x1133e, 0x1133f);
test(0x11340, 0x11340);
test(0x11341, 0x11344);
test(0x11347, 0x11348);
test(0x1134b, 0x1134d);
test(0x11357, 0x11357);
test(0x1135d, 0x11361);
test(0x11362, 0x11363);
test(0x11366, 0x1136c);
test(0x11370, 0x11374);
test(0x11480, 0x114af);
test(0x114b0, 0x114b2);
test(0x114b3, 0x114b8);
test(0x114b9, 0x114b9);
test(0x114ba, 0x114ba);
test(0x114bb, 0x114be);
test(0x114bf, 0x114c0);
test(0x114c1, 0x114c1);
test(0x114c2, 0x114c3);
test(0x114c4, 0x114c5);
test(0x114c7, 0x114c7);
test(0x114d0, 0x114d9);
test(0x11580, 0x115ae);
test(0x115af, 0x115b1);
test(0x115b2, 0x115b5);
test(0x115b8, 0x115bb);
test(0x115bc, 0x115bd);
test(0x115be, 0x115be);
test(0x115bf, 0x115c0);
test(0x11600, 0x1162f);
test(0x11630, 0x11632);
test(0x11633, 0x1163a);
test(0x1163b, 0x1163c);
test(0x1163d, 0x1163d);
test(0x1163e, 0x1163e);
test(0x1163f, 0x11640);
test(0x11644, 0x11644);
test(0x11650, 0x11659);
test(0x118a0, 0x118df);
test(0x118e0, 0x118e9);
test(0x118ff, 0x118ff);
test(0x11ac0, 0x11af8);
test(0x12000, 0x12398);
test(0x12400, 0x1246e);
test(0x16a40, 0x16a5e);
test(0x16a60, 0x16a69);
test(0x16ad0, 0x16aed);
test(0x16af0, 0x16af4);
test(0x16b00, 0x16b2f);
test(0x16b30, 0x16b36);
test(0x16b40, 0x16b43);
test(0x16b50, 0x16b59);
test(0x16b63, 0x16b77);
test(0x16b7d, 0x16b8f);
test(0x1bc00, 0x1bc6a);
test(0x1bc70, 0x1bc7c);
test(0x1bc80, 0x1bc88);
test(0x1bc90, 0x1bc99);
test(0x1bc9d, 0x1bc9e);
test(0x1e800, 0x1e8c4);
test(0x1e8d0, 0x1e8d6);
