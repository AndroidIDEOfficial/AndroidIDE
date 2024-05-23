package com.android.aaptcompiler.android

import com.itsaky.androidide.utils.intByteMapOf

internal val SCRIPT_CODES = arrayOf(
  /* 0  */ byteArrayOf('A'.code.toByte(), 'h'.code.toByte(), 'o'.code.toByte(), 'm'.code.toByte()),
  /* 1  */ byteArrayOf('A'.code.toByte(), 'r'.code.toByte(), 'a'.code.toByte(), 'b'.code.toByte()),
  /* 2  */ byteArrayOf('A'.code.toByte(), 'r'.code.toByte(), 'm'.code.toByte(), 'i'.code.toByte()),
  /* 3  */ byteArrayOf('A'.code.toByte(), 'r'.code.toByte(), 'm'.code.toByte(), 'n'.code.toByte()),
  /* 4  */ byteArrayOf('A'.code.toByte(), 'v'.code.toByte(), 's'.code.toByte(), 't'.code.toByte()),
  /* 5  */ byteArrayOf('B'.code.toByte(), 'a'.code.toByte(), 'm'.code.toByte(), 'u'.code.toByte()),
  /* 6  */ byteArrayOf('B'.code.toByte(), 'a'.code.toByte(), 's'.code.toByte(), 's'.code.toByte()),
  /* 7  */ byteArrayOf('B'.code.toByte(), 'e'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte()),
  /* 8  */ byteArrayOf('B'.code.toByte(), 'r'.code.toByte(), 'a'.code.toByte(), 'h'.code.toByte()),
  /* 9  */ byteArrayOf('C'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 's'.code.toByte()),
  /* 10 */ byteArrayOf('C'.code.toByte(), 'a'.code.toByte(), 'r'.code.toByte(), 'i'.code.toByte()),
  /* 11 */ byteArrayOf('C'.code.toByte(), 'h'.code.toByte(), 'a'.code.toByte(), 'm'.code.toByte()),
  /* 12 */ byteArrayOf('C'.code.toByte(), 'h'.code.toByte(), 'e'.code.toByte(), 'r'.code.toByte()),
  /* 13 */ byteArrayOf('C'.code.toByte(), 'o'.code.toByte(), 'p'.code.toByte(), 't'.code.toByte()),
  /* 14 */ byteArrayOf('C'.code.toByte(), 'p'.code.toByte(), 'r'.code.toByte(), 't'.code.toByte()),
  /* 15 */ byteArrayOf('C'.code.toByte(), 'y'.code.toByte(), 'r'.code.toByte(), 'l'.code.toByte()),
  /* 16 */ byteArrayOf('D'.code.toByte(), 'e'.code.toByte(), 'v'.code.toByte(), 'a'.code.toByte()),
  /* 17 */ byteArrayOf('E'.code.toByte(), 'g'.code.toByte(), 'y'.code.toByte(), 'p'.code.toByte()),
  /* 18 */ byteArrayOf('E'.code.toByte(), 't'.code.toByte(), 'h'.code.toByte(), 'i'.code.toByte()),
  /* 19 */ byteArrayOf('G'.code.toByte(), 'e'.code.toByte(), 'o'.code.toByte(), 'r'.code.toByte()),
  /* 20 */ byteArrayOf('G'.code.toByte(), 'o'.code.toByte(), 't'.code.toByte(), 'h'.code.toByte()),
  /* 21 */ byteArrayOf('G'.code.toByte(), 'r'.code.toByte(), 'e'.code.toByte(), 'k'.code.toByte()),
  /* 22 */ byteArrayOf('G'.code.toByte(), 'u'.code.toByte(), 'j'.code.toByte(), 'r'.code.toByte()),
  /* 23 */ byteArrayOf('G'.code.toByte(), 'u'.code.toByte(), 'r'.code.toByte(), 'u'.code.toByte()),
  /* 24 */ byteArrayOf('H'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 's'.code.toByte()),
  /* 25 */ byteArrayOf('H'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 't'.code.toByte()),
  /* 26 */ byteArrayOf('H'.code.toByte(), 'a'.code.toByte(), 't'.code.toByte(), 'r'.code.toByte()),
  /* 27 */ byteArrayOf('H'.code.toByte(), 'e'.code.toByte(), 'b'.code.toByte(), 'r'.code.toByte()),
  /* 28 */ byteArrayOf('H'.code.toByte(), 'l'.code.toByte(), 'u'.code.toByte(), 'w'.code.toByte()),
  /* 29 */ byteArrayOf('H'.code.toByte(), 'm'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte()),
  /* 30 */ byteArrayOf('I'.code.toByte(), 't'.code.toByte(), 'a'.code.toByte(), 'l'.code.toByte()),
  /* 31 */ byteArrayOf('J'.code.toByte(), 'p'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte()),
  /* 32 */ byteArrayOf('K'.code.toByte(), 'a'.code.toByte(), 'l'.code.toByte(), 'i'.code.toByte()),
  /* 33 */ byteArrayOf('K'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 'a'.code.toByte()),
  /* 34 */ byteArrayOf('K'.code.toByte(), 'h'.code.toByte(), 'a'.code.toByte(), 'r'.code.toByte()),
  /* 35 */ byteArrayOf('K'.code.toByte(), 'h'.code.toByte(), 'm'.code.toByte(), 'r'.code.toByte()),
  /* 36 */ byteArrayOf('K'.code.toByte(), 'n'.code.toByte(), 'd'.code.toByte(), 'a'.code.toByte()),
  /* 37 */ byteArrayOf('K'.code.toByte(), 'o'.code.toByte(), 'r'.code.toByte(), 'e'.code.toByte()),
  /* 38 */ byteArrayOf('L'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 'a'.code.toByte()),
  /* 39 */ byteArrayOf('L'.code.toByte(), 'a'.code.toByte(), 'o'.code.toByte(), 'o'.code.toByte()),
  /* 40 */ byteArrayOf('L'.code.toByte(), 'a'.code.toByte(), 't'.code.toByte(), 'n'.code.toByte()),
  /* 41 */ byteArrayOf('L'.code.toByte(), 'e'.code.toByte(), 'p'.code.toByte(), 'c'.code.toByte()),
  /* 42 */ byteArrayOf('L'.code.toByte(), 'i'.code.toByte(), 'n'.code.toByte(), 'a'.code.toByte()),
  /* 43 */ byteArrayOf('L'.code.toByte(), 'i'.code.toByte(), 's'.code.toByte(), 'u'.code.toByte()),
  /* 44 */ byteArrayOf('L'.code.toByte(), 'y'.code.toByte(), 'c'.code.toByte(), 'i'.code.toByte()),
  /* 45 */ byteArrayOf('L'.code.toByte(), 'y'.code.toByte(), 'd'.code.toByte(), 'i'.code.toByte()),
  /* 46 */ byteArrayOf('M'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 'd'.code.toByte()),
  /* 47 */ byteArrayOf('M'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 'i'.code.toByte()),
  /* 48 */ byteArrayOf('M'.code.toByte(), 'e'.code.toByte(), 'r'.code.toByte(), 'c'.code.toByte()),
  /* 49 */ byteArrayOf('M'.code.toByte(), 'l'.code.toByte(), 'y'.code.toByte(), 'm'.code.toByte()),
  /* 50 */ byteArrayOf('M'.code.toByte(), 'o'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte()),
  /* 51 */ byteArrayOf('M'.code.toByte(), 'r'.code.toByte(), 'o'.code.toByte(), 'o'.code.toByte()),
  /* 52 */ byteArrayOf('M'.code.toByte(), 'y'.code.toByte(), 'm'.code.toByte(), 'r'.code.toByte()),
  /* 53 */ byteArrayOf('N'.code.toByte(), 'a'.code.toByte(), 'r'.code.toByte(), 'b'.code.toByte()),
  /* 54 */ byteArrayOf('N'.code.toByte(), 'k'.code.toByte(), 'o'.code.toByte(), 'o'.code.toByte()),
  /* 55 */ byteArrayOf('O'.code.toByte(), 'g'.code.toByte(), 'a'.code.toByte(), 'm'.code.toByte()),
  /* 56 */ byteArrayOf('O'.code.toByte(), 'r'.code.toByte(), 'k'.code.toByte(), 'h'.code.toByte()),
  /* 57 */ byteArrayOf('O'.code.toByte(), 'r'.code.toByte(), 'y'.code.toByte(), 'a'.code.toByte()),
  /* 58 */ byteArrayOf('O'.code.toByte(), 's'.code.toByte(), 'g'.code.toByte(), 'e'.code.toByte()),
  /* 59 */ byteArrayOf('P'.code.toByte(), 'a'.code.toByte(), 'u'.code.toByte(), 'c'.code.toByte()),
  /* 60 */ byteArrayOf('P'.code.toByte(), 'h'.code.toByte(), 'l'.code.toByte(), 'i'.code.toByte()),
  /* 61 */ byteArrayOf('P'.code.toByte(), 'h'.code.toByte(), 'n'.code.toByte(), 'x'.code.toByte()),
  /* 62 */ byteArrayOf('P'.code.toByte(), 'l'.code.toByte(), 'r'.code.toByte(), 'd'.code.toByte()),
  /* 63 */ byteArrayOf('P'.code.toByte(), 'r'.code.toByte(), 't'.code.toByte(), 'i'.code.toByte()),
  /* 64 */ byteArrayOf('R'.code.toByte(), 'u'.code.toByte(), 'n'.code.toByte(), 'r'.code.toByte()),
  /* 65 */ byteArrayOf('S'.code.toByte(), 'a'.code.toByte(), 'm'.code.toByte(), 'r'.code.toByte()),
  /* 66 */ byteArrayOf('S'.code.toByte(), 'a'.code.toByte(), 'r'.code.toByte(), 'b'.code.toByte()),
  /* 67 */ byteArrayOf('S'.code.toByte(), 'a'.code.toByte(), 'u'.code.toByte(), 'r'.code.toByte()),
  /* 68 */ byteArrayOf('S'.code.toByte(), 'g'.code.toByte(), 'n'.code.toByte(), 'w'.code.toByte()),
  /* 69 */ byteArrayOf('S'.code.toByte(), 'i'.code.toByte(), 'n'.code.toByte(), 'h'.code.toByte()),
  /* 70 */ byteArrayOf('S'.code.toByte(), 'o'.code.toByte(), 'r'.code.toByte(), 'a'.code.toByte()),
  /* 71 */ byteArrayOf('S'.code.toByte(), 'y'.code.toByte(), 'r'.code.toByte(), 'c'.code.toByte()),
  /* 72 */ byteArrayOf('T'.code.toByte(), 'a'.code.toByte(), 'l'.code.toByte(), 'e'.code.toByte()),
  /* 73 */ byteArrayOf('T'.code.toByte(), 'a'.code.toByte(), 'l'.code.toByte(), 'u'.code.toByte()),
  /* 74 */ byteArrayOf('T'.code.toByte(), 'a'.code.toByte(), 'm'.code.toByte(), 'l'.code.toByte()),
  /* 75 */ byteArrayOf('T'.code.toByte(), 'a'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte()),
  /* 76 */ byteArrayOf('T'.code.toByte(), 'a'.code.toByte(), 'v'.code.toByte(), 't'.code.toByte()),
  /* 77 */ byteArrayOf('T'.code.toByte(), 'e'.code.toByte(), 'l'.code.toByte(), 'u'.code.toByte()),
  /* 78 */ byteArrayOf('T'.code.toByte(), 'f'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte()),
  /* 79 */ byteArrayOf('T'.code.toByte(), 'h'.code.toByte(), 'a'.code.toByte(), 'a'.code.toByte()),
  /* 80 */ byteArrayOf('T'.code.toByte(), 'h'.code.toByte(), 'a'.code.toByte(), 'i'.code.toByte()),
  /* 81 */ byteArrayOf('T'.code.toByte(), 'i'.code.toByte(), 'b'.code.toByte(), 't'.code.toByte()),
  /* 82 */ byteArrayOf('U'.code.toByte(), 'g'.code.toByte(), 'a'.code.toByte(), 'r'.code.toByte()),
  /* 83 */ byteArrayOf('V'.code.toByte(), 'a'.code.toByte(), 'i'.code.toByte(), 'i'.code.toByte()),
  /* 84 */ byteArrayOf('X'.code.toByte(), 'p'.code.toByte(), 'e'.code.toByte(), 'o'.code.toByte()),
  /* 85 */ byteArrayOf('X'.code.toByte(), 's'.code.toByte(), 'u'.code.toByte(), 'x'.code.toByte()),
  /* 86 */ byteArrayOf('Y'.code.toByte(), 'i'.code.toByte(), 'i'.code.toByte(), 'i'.code.toByte()),
  /* 87 */ byteArrayOf('~'.code.toByte(), '~'.code.toByte(), '~'.code.toByte(), 'A'.code.toByte()),
  /* 88 */ byteArrayOf('~'.code.toByte(), '~'.code.toByte(), '~'.code.toByte(), 'B'.code.toByte()))

internal val LIKELY_SCRIPTS = intByteMapOf(
  0x61610000         to 40, // aa -> Latn
  0xa0000000.toInt() to 40, // aai -> Latn
  0xa8000000.toInt() to 40, // aak -> Latn
  0xd0000000.toInt() to 40, // aau -> Latn
  0x61620000         to 15, // ab -> Cyrl
  0xa0200000.toInt() to 40, // abi -> Latn
  0xc4200000.toInt() to 40, // abr -> Latn
  0xcc200000.toInt() to 40, // abt -> Latn
  0xe0200000.toInt() to 40, // aby -> Latn
  0x8c400000.toInt() to 40, // acd -> Latn
  0x90400000.toInt() to 40, // ace -> Latn
  0x9c400000.toInt() to 40, // ach -> Latn
  0x80600000.toInt() to 40, // ada -> Latn
  0x90600000.toInt() to 40, // ade -> Latn
  0xa4600000.toInt() to 40, // adj -> Latn
  0xe0600000.toInt() to 15, // ady -> Cyrl
  0xe4600000.toInt() to 40, // adz -> Latn
  0x61650000         to 4, // ae -> Avst
  0x84800000.toInt() to 1, // aeb -> Arab
  0xe0800000.toInt() to 40, // aey -> Latn
  0x61660000         to 40, // af -> Latn
  0x88c00000.toInt() to 40, // agc -> Latn
  0x8cc00000.toInt() to 40, // agd -> Latn
  0x98c00000.toInt() to 40, // agg -> Latn
  0xb0c00000.toInt() to 40, // agm -> Latn
  0xb8c00000.toInt() to 40, // ago -> Latn
  0xc0c00000.toInt() to 40, // agq -> Latn
  0x80e00000.toInt() to 40, // aha -> Latn
  0xace00000.toInt() to 40, // ahl -> Latn
  0xb8e00000.toInt() to 0, // aho -> Ahom
  0x99200000.toInt() to 40, // ajg -> Latn
  0x616b0000         to 40, // ak -> Latn
  0xa9400000.toInt() to 85, // akk -> Xsux
  0x81600000.toInt() to 40, // ala -> Latn
  0xa1600000.toInt() to 40, // ali -> Latn
  0xb5600000.toInt() to 40, // aln -> Latn
  0xcd600000.toInt() to 15, // alt -> Cyrl
  0x616d0000         to 18, // am -> Ethi
  0xb1800000.toInt() to 40, // amm -> Latn
  0xb5800000.toInt() to 40, // amn -> Latn
  0xb9800000.toInt() to 40, // amo -> Latn
  0xbd800000.toInt() to 40, // amp -> Latn
  0x89a00000.toInt() to 40, // anc -> Latn
  0xa9a00000.toInt() to 40, // ank -> Latn
  0xb5a00000.toInt() to 40, // ann -> Latn
  0xe1a00000.toInt() to 40, // any -> Latn
  0xa5c00000.toInt() to 40, // aoj -> Latn
  0xb1c00000.toInt() to 40, // aom -> Latn
  0xe5c00000.toInt() to 40, // aoz -> Latn
  0x89e00000.toInt() to 1, // apc -> Arab
  0x8de00000.toInt() to 1, // apd -> Arab
  0x91e00000.toInt() to 40, // ape -> Latn
  0xc5e00000.toInt() to 40, // apr -> Latn
  0xc9e00000.toInt() to 40, // aps -> Latn
  0xe5e00000.toInt() to 40, // apz -> Latn
  0x61720000         to 1, // ar -> Arab
  0x61725842         to 88, // ar-XB -> ~~~B
  0x8a200000.toInt() to 2, // arc -> Armi
  0x9e200000.toInt() to 40, // arh -> Latn
  0xb6200000.toInt() to 40, // arn -> Latn
  0xba200000.toInt() to 40, // aro -> Latn
  0xc2200000.toInt() to 1, // arq -> Arab
  0xe2200000.toInt() to 1, // ary -> Arab
  0xe6200000.toInt() to 1, // arz -> Arab
  0x61730000         to 7, // as -> Beng
  0x82400000.toInt() to 40, // asa -> Latn
  0x92400000.toInt() to 68, // ase -> Sgnw
  0x9a400000.toInt() to 40, // asg -> Latn
  0xba400000.toInt() to 40, // aso -> Latn
  0xce400000.toInt() to 40, // ast -> Latn
  0x82600000.toInt() to 40, // ata -> Latn
  0x9a600000.toInt() to 40, // atg -> Latn
  0xa6600000.toInt() to 40, // atj -> Latn
  0xe2800000.toInt() to 40, // auy -> Latn
  0x61760000         to 15, // av -> Cyrl
  0xaea00000.toInt() to 1, // avl -> Arab
  0xb6a00000.toInt() to 40, // avn -> Latn
  0xcea00000.toInt() to 40, // avt -> Latn
  0xd2a00000.toInt() to 40, // avu -> Latn
  0x82c00000.toInt() to 16, // awa -> Deva
  0x86c00000.toInt() to 40, // awb -> Latn
  0xbac00000.toInt() to 40, // awo -> Latn
  0xdec00000.toInt() to 40, // awx -> Latn
  0x61790000         to 40, // ay -> Latn
  0x87000000.toInt() to 40, // ayb -> Latn
  0x617a0000         to 40, // az -> Latn
  0x617a4951         to 1, // az-IQ -> Arab
  0x617a4952         to 1, // az-IR -> Arab
  0x617a5255         to 15, // az-RU -> Cyrl
  0x62610000         to 15, // ba -> Cyrl
  0xac010000.toInt() to 1, // bal -> Arab
  0xb4010000.toInt() to 40, // ban -> Latn
  0xbc010000.toInt() to 16, // bap -> Deva
  0xc4010000.toInt() to 40, // bar -> Latn
  0xc8010000.toInt() to 40, // bas -> Latn
  0xd4010000.toInt() to 40, // bav -> Latn
  0xdc010000.toInt() to 5, // bax -> Bamu
  0x80210000.toInt() to 40, // bba -> Latn
  0x84210000.toInt() to 40, // bbb -> Latn
  0x88210000.toInt() to 40, // bbc -> Latn
  0x8c210000.toInt() to 40, // bbd -> Latn
  0xa4210000.toInt() to 40, // bbj -> Latn
  0xbc210000.toInt() to 40, // bbp -> Latn
  0xc4210000.toInt() to 40, // bbr -> Latn
  0x94410000.toInt() to 40, // bcf -> Latn
  0x9c410000.toInt() to 40, // bch -> Latn
  0xa0410000.toInt() to 40, // bci -> Latn
  0xb0410000.toInt() to 40, // bcm -> Latn
  0xb4410000.toInt() to 40, // bcn -> Latn
  0xb8410000.toInt() to 40, // bco -> Latn
  0xc0410000.toInt() to 18, // bcq -> Ethi
  0xd0410000.toInt() to 40, // bcu -> Latn
  0x8c610000.toInt() to 40, // bdd -> Latn
  0x62650000         to 15, // be -> Cyrl
  0x94810000.toInt() to 40, // bef -> Latn
  0x9c810000.toInt() to 40, // beh -> Latn
  0xa4810000.toInt() to 1, // bej -> Arab
  0xb0810000.toInt() to 40, // bem -> Latn
  0xcc810000.toInt() to 40, // bet -> Latn
  0xd8810000.toInt() to 40, // bew -> Latn
  0xdc810000.toInt() to 40, // bex -> Latn
  0xe4810000.toInt() to 40, // bez -> Latn
  0x8ca10000.toInt() to 40, // bfd -> Latn
  0xc0a10000.toInt() to 74, // bfq -> Taml
  0xcca10000.toInt() to 1, // bft -> Arab
  0xe0a10000.toInt() to 16, // bfy -> Deva
  0x62670000         to 15, // bg -> Cyrl
  0x88c10000.toInt() to 16, // bgc -> Deva
  0xb4c10000.toInt() to 1, // bgn -> Arab
  0xdcc10000.toInt() to 21, // bgx -> Grek
  0x84e10000.toInt() to 16, // bhb -> Deva
  0x98e10000.toInt() to 40, // bhg -> Latn
  0xa0e10000.toInt() to 16, // bhi -> Deva
  0xa8e10000.toInt() to 40, // bhk -> Latn
  0xace10000.toInt() to 40, // bhl -> Latn
  0xb8e10000.toInt() to 16, // bho -> Deva
  0xe0e10000.toInt() to 40, // bhy -> Latn
  0x62690000         to 40, // bi -> Latn
  0x85010000.toInt() to 40, // bib -> Latn
  0x99010000.toInt() to 40, // big -> Latn
  0xa9010000.toInt() to 40, // bik -> Latn
  0xb1010000.toInt() to 40, // bim -> Latn
  0xb5010000.toInt() to 40, // bin -> Latn
  0xb9010000.toInt() to 40, // bio -> Latn
  0xc1010000.toInt() to 40, // biq -> Latn
  0x9d210000.toInt() to 40, // bjh -> Latn
  0xa1210000.toInt() to 18, // bji -> Ethi
  0xa5210000.toInt() to 16, // bjj -> Deva
  0xb5210000.toInt() to 40, // bjn -> Latn
  0xb9210000.toInt() to 40, // bjo -> Latn
  0xc5210000.toInt() to 40, // bjr -> Latn
  0xe5210000.toInt() to 40, // bjz -> Latn
  0x89410000.toInt() to 40, // bkc -> Latn
  0xb1410000.toInt() to 40, // bkm -> Latn
  0xc1410000.toInt() to 40, // bkq -> Latn
  0xd1410000.toInt() to 40, // bku -> Latn
  0xd5410000.toInt() to 40, // bkv -> Latn
  0xcd610000.toInt() to 76, // blt -> Tavt
  0x626d0000         to 40, // bm -> Latn
  0x9d810000.toInt() to 40, // bmh -> Latn
  0xa9810000.toInt() to 40, // bmk -> Latn
  0xc1810000.toInt() to 40, // bmq -> Latn
  0xd1810000.toInt() to 40, // bmu -> Latn
  0x626e0000         to 7, // bn -> Beng
  0x99a10000.toInt() to 40, // bng -> Latn
  0xb1a10000.toInt() to 40, // bnm -> Latn
  0xbda10000.toInt() to 40, // bnp -> Latn
  0x626f0000         to 81, // bo -> Tibt
  0xa5c10000.toInt() to 40, // boj -> Latn
  0xb1c10000.toInt() to 40, // bom -> Latn
  0xb5c10000.toInt() to 40, // bon -> Latn
  0xe1e10000.toInt() to 7, // bpy -> Beng
  0x8a010000.toInt() to 40, // bqc -> Latn
  0xa2010000.toInt() to 1, // bqi -> Arab
  0xbe010000.toInt() to 40, // bqp -> Latn
  0xd6010000.toInt() to 40, // bqv -> Latn
  0x62720000         to 40, // br -> Latn
  0x82210000.toInt() to 16, // bra -> Deva
  0x9e210000.toInt() to 1, // brh -> Arab
  0xde210000.toInt() to 16, // brx -> Deva
  0xe6210000.toInt() to 40, // brz -> Latn
  0x62730000         to 40, // bs -> Latn
  0xa6410000.toInt() to 40, // bsj -> Latn
  0xc2410000.toInt() to 6, // bsq -> Bass
  0xca410000.toInt() to 40, // bss -> Latn
  0xce410000.toInt() to 18, // bst -> Ethi
  0xba610000.toInt() to 40, // bto -> Latn
  0xce610000.toInt() to 40, // btt -> Latn
  0xd6610000.toInt() to 16, // btv -> Deva
  0x82810000.toInt() to 15, // bua -> Cyrl
  0x8a810000.toInt() to 40, // buc -> Latn
  0x8e810000.toInt() to 40, // bud -> Latn
  0x9a810000.toInt() to 40, // bug -> Latn
  0xaa810000.toInt() to 40, // buk -> Latn
  0xb2810000.toInt() to 40, // bum -> Latn
  0xba810000.toInt() to 40, // buo -> Latn
  0xca810000.toInt() to 40, // bus -> Latn
  0xd2810000.toInt() to 40, // buu -> Latn
  0x86a10000.toInt() to 40, // bvb -> Latn
  0x8ec10000.toInt() to 40, // bwd -> Latn
  0xc6c10000.toInt() to 40, // bwr -> Latn
  0x9ee10000.toInt() to 40, // bxh -> Latn
  0x93010000.toInt() to 40, // bye -> Latn
  0xb7010000.toInt() to 18, // byn -> Ethi
  0xc7010000.toInt() to 40, // byr -> Latn
  0xcb010000.toInt() to 40, // bys -> Latn
  0xd7010000.toInt() to 40, // byv -> Latn
  0xdf010000.toInt() to 40, // byx -> Latn
  0x83210000.toInt() to 40, // bza -> Latn
  0x93210000.toInt() to 40, // bze -> Latn
  0x97210000.toInt() to 40, // bzf -> Latn
  0x9f210000.toInt() to 40, // bzh -> Latn
  0xdb210000.toInt() to 40, // bzw -> Latn
  0x63610000         to 40, // ca -> Latn
  0xb4020000.toInt() to 40, // can -> Latn
  0xa4220000.toInt() to 40, // cbj -> Latn
  0x9c420000.toInt() to 40, // cch -> Latn
  0xbc420000.toInt() to 7, // ccp -> Beng
  0x63650000         to 15, // ce -> Cyrl
  0x84820000.toInt() to 40, // ceb -> Latn
  0x80a20000.toInt() to 40, // cfa -> Latn
  0x98c20000.toInt() to 40, // cgg -> Latn
  0x63680000         to 40, // ch -> Latn
  0xa8e20000.toInt() to 40, // chk -> Latn
  0xb0e20000.toInt() to 15, // chm -> Cyrl
  0xb8e20000.toInt() to 40, // cho -> Latn
  0xbce20000.toInt() to 40, // chp -> Latn
  0xc4e20000.toInt() to 12, // chr -> Cher
  0x81220000.toInt() to 1, // cja -> Arab
  0xb1220000.toInt() to 11, // cjm -> Cham
  0xd5220000.toInt() to 40, // cjv -> Latn
  0x85420000.toInt() to 1, // ckb -> Arab
  0xad420000.toInt() to 40, // ckl -> Latn
  0xb9420000.toInt() to 40, // cko -> Latn
  0xe1420000.toInt() to 40, // cky -> Latn
  0x81620000.toInt() to 40, // cla -> Latn
  0x91820000.toInt() to 40, // cme -> Latn
  0x636f0000         to 40, // co -> Latn
  0xbdc20000.toInt() to 13, // cop -> Copt
  0xc9e20000.toInt() to 40, // cps -> Latn
  0x63720000         to 9, // cr -> Cans
  0xa6220000.toInt() to 9, // crj -> Cans
  0xaa220000.toInt() to 9, // crk -> Cans
  0xae220000.toInt() to 9, // crl -> Cans
  0xb2220000.toInt() to 9, // crm -> Cans
  0xca220000.toInt() to 40, // crs -> Latn
  0x63730000         to 40, // cs -> Latn
  0x86420000.toInt() to 40, // csb -> Latn
  0xda420000.toInt() to 9, // csw -> Cans
  0x8e620000.toInt() to 59, // ctd -> Pauc
  0x63750000         to 15, // cu -> Cyrl
  0x63760000         to 15, // cv -> Cyrl
  0x63790000         to 40, // cy -> Latn
  0x64610000         to 40, // da -> Latn
  0x8c030000.toInt() to 40, // dad -> Latn
  0x94030000.toInt() to 40, // daf -> Latn
  0x98030000.toInt() to 40, // dag -> Latn
  0x9c030000.toInt() to 40, // dah -> Latn
  0xa8030000.toInt() to 40, // dak -> Latn
  0xc4030000.toInt() to 15, // dar -> Cyrl
  0xd4030000.toInt() to 40, // dav -> Latn
  0x8c230000.toInt() to 40, // dbd -> Latn
  0xc0230000.toInt() to 40, // dbq -> Latn
  0x88430000.toInt() to 1, // dcc -> Arab
  0xb4630000.toInt() to 40, // ddn -> Latn
  0x64650000         to 40, // de -> Latn
  0x8c830000.toInt() to 40, // ded -> Latn
  0xb4830000.toInt() to 40, // den -> Latn
  0x80c30000.toInt() to 40, // dga -> Latn
  0x9cc30000.toInt() to 40, // dgh -> Latn
  0xa0c30000.toInt() to 40, // dgi -> Latn
  0xacc30000.toInt() to 1, // dgl -> Arab
  0xc4c30000.toInt() to 40, // dgr -> Latn
  0xe4c30000.toInt() to 40, // dgz -> Latn
  0x81030000.toInt() to 40, // dia -> Latn
  0x91230000.toInt() to 40, // dje -> Latn
  0xa5a30000.toInt() to 40, // dnj -> Latn
  0x85c30000.toInt() to 40, // dob -> Latn
  0xa1c30000.toInt() to 1, // doi -> Arab
  0xbdc30000.toInt() to 40, // dop -> Latn
  0xd9c30000.toInt() to 40, // dow -> Latn
  0xa2230000.toInt() to 40, // dri -> Latn
  0xca230000.toInt() to 18, // drs -> Ethi
  0x86430000.toInt() to 40, // dsb -> Latn
  0xb2630000.toInt() to 40, // dtm -> Latn
  0xbe630000.toInt() to 40, // dtp -> Latn
  0xca630000.toInt() to 40, // dts -> Latn
  0xe2630000.toInt() to 16, // dty -> Deva
  0x82830000.toInt() to 40, // dua -> Latn
  0x8a830000.toInt() to 40, // duc -> Latn
  0x8e830000.toInt() to 40, // dud -> Latn
  0x9a830000.toInt() to 40, // dug -> Latn
  0x64760000         to 79, // dv -> Thaa
  0x82a30000.toInt() to 40, // dva -> Latn
  0xdac30000.toInt() to 40, // dww -> Latn
  0xbb030000.toInt() to 40, // dyo -> Latn
  0xd3030000.toInt() to 40, // dyu -> Latn
  0x647a0000         to 81, // dz -> Tibt
  0x9b230000.toInt() to 40, // dzg -> Latn
  0xd0240000.toInt() to 40, // ebu -> Latn
  0x65650000         to 40, // ee -> Latn
  0xa0a40000.toInt() to 40, // efi -> Latn
  0xacc40000.toInt() to 40, // egl -> Latn
  0xe0c40000.toInt() to 17, // egy -> Egyp
  0xe1440000.toInt() to 32, // eky -> Kali
  0x656c0000         to 21, // el -> Grek
  0x81840000.toInt() to 40, // ema -> Latn
  0xa1840000.toInt() to 40, // emi -> Latn
  0x656e0000         to 40, // en -> Latn
  0x656e5841         to 87, // en-XA -> ~~~A
  0xb5a40000.toInt() to 40, // enn -> Latn
  0xc1a40000.toInt() to 40, // enq -> Latn
  0x656f0000         to 40, // eo -> Latn
  0xa2240000.toInt() to 40, // eri -> Latn
  0x65730000         to 40, // es -> Latn
  0xd2440000.toInt() to 40, // esu -> Latn
  0x65740000         to 40, // et -> Latn
  0xc6640000.toInt() to 40, // etr -> Latn
  0xce640000.toInt() to 30, // ett -> Ital
  0xd2640000.toInt() to 40, // etu -> Latn
  0xde640000.toInt() to 40, // etx -> Latn
  0x65750000         to 40, // eu -> Latn
  0xbac40000.toInt() to 40, // ewo -> Latn
  0xcee40000.toInt() to 40, // ext -> Latn
  0x66610000         to 1, // fa -> Arab
  0x80050000.toInt() to 40, // faa -> Latn
  0x84050000.toInt() to 40, // fab -> Latn
  0x98050000.toInt() to 40, // fag -> Latn
  0xa0050000.toInt() to 40, // fai -> Latn
  0xb4050000.toInt() to 40, // fan -> Latn
  0x66660000         to 40, // ff -> Latn
  0xa0a50000.toInt() to 40, // ffi -> Latn
  0xb0a50000.toInt() to 40, // ffm -> Latn
  0x66690000         to 40, // fi -> Latn
  0x81050000.toInt() to 1, // fia -> Arab
  0xad050000.toInt() to 40, // fil -> Latn
  0xcd050000.toInt() to 40, // fit -> Latn
  0x666a0000         to 40, // fj -> Latn
  0xc5650000.toInt() to 40, // flr -> Latn
  0xbd850000.toInt() to 40, // fmp -> Latn
  0x666f0000         to 40, // fo -> Latn
  0x8dc50000.toInt() to 40, // fod -> Latn
  0xb5c50000.toInt() to 40, // fon -> Latn
  0xc5c50000.toInt() to 40, // for -> Latn
  0x91e50000.toInt() to 40, // fpe -> Latn
  0xca050000.toInt() to 40, // fqs -> Latn
  0x66720000         to 40, // fr -> Latn
  0x8a250000.toInt() to 40, // frc -> Latn
  0xbe250000.toInt() to 40, // frp -> Latn
  0xc6250000.toInt() to 40, // frr -> Latn
  0xca250000.toInt() to 40, // frs -> Latn
  0x86850000.toInt() to 1, // fub -> Arab
  0x8e850000.toInt() to 40, // fud -> Latn
  0x92850000.toInt() to 40, // fue -> Latn
  0x96850000.toInt() to 40, // fuf -> Latn
  0x9e850000.toInt() to 40, // fuh -> Latn
  0xc2850000.toInt() to 40, // fuq -> Latn
  0xc6850000.toInt() to 40, // fur -> Latn
  0xd6850000.toInt() to 40, // fuv -> Latn
  0xe2850000.toInt() to 40, // fuy -> Latn
  0xc6a50000.toInt() to 40, // fvr -> Latn
  0x66790000         to 40, // fy -> Latn
  0x67610000         to 40, // ga -> Latn
  0x80060000.toInt() to 40, // gaa -> Latn
  0x94060000.toInt() to 40, // gaf -> Latn
  0x98060000.toInt() to 40, // gag -> Latn
  0x9c060000.toInt() to 40, // gah -> Latn
  0xa4060000.toInt() to 40, // gaj -> Latn
  0xb0060000.toInt() to 40, // gam -> Latn
  0xb4060000.toInt() to 24, // gan -> Hans
  0xd8060000.toInt() to 40, // gaw -> Latn
  0xe0060000.toInt() to 40, // gay -> Latn
  0x94260000.toInt() to 40, // gbf -> Latn
  0xb0260000.toInt() to 16, // gbm -> Deva
  0xe0260000.toInt() to 40, // gby -> Latn
  0xe4260000.toInt() to 1, // gbz -> Arab
  0xc4460000.toInt() to 40, // gcr -> Latn
  0x67640000         to 40, // gd -> Latn
  0x90660000.toInt() to 40, // gde -> Latn
  0xb4660000.toInt() to 40, // gdn -> Latn
  0xc4660000.toInt() to 40, // gdr -> Latn
  0x84860000.toInt() to 40, // geb -> Latn
  0xa4860000.toInt() to 40, // gej -> Latn
  0xac860000.toInt() to 40, // gel -> Latn
  0xe4860000.toInt() to 18, // gez -> Ethi
  0xa8a60000.toInt() to 40, // gfk -> Latn
  0xb4c60000.toInt() to 16, // ggn -> Deva
  0xc8e60000.toInt() to 40, // ghs -> Latn
  0xad060000.toInt() to 40, // gil -> Latn
  0xb1060000.toInt() to 40, // gim -> Latn
  0xa9260000.toInt() to 1, // gjk -> Arab
  0xb5260000.toInt() to 40, // gjn -> Latn
  0xd1260000.toInt() to 1, // gju -> Arab
  0xb5460000.toInt() to 40, // gkn -> Latn
  0xbd460000.toInt() to 40, // gkp -> Latn
  0x676c0000         to 40, // gl -> Latn
  0xa9660000.toInt() to 1, // glk -> Arab
  0xb1860000.toInt() to 40, // gmm -> Latn
  0xd5860000.toInt() to 18, // gmv -> Ethi
  0x676e0000         to 40, // gn -> Latn
  0x8da60000.toInt() to 40, // gnd -> Latn
  0x99a60000.toInt() to 40, // gng -> Latn
  0x8dc60000.toInt() to 40, // god -> Latn
  0x95c60000.toInt() to 18, // gof -> Ethi
  0xa1c60000.toInt() to 40, // goi -> Latn
  0xb1c60000.toInt() to 16, // gom -> Deva
  0xb5c60000.toInt() to 77, // gon -> Telu
  0xc5c60000.toInt() to 40, // gor -> Latn
  0xc9c60000.toInt() to 40, // gos -> Latn
  0xcdc60000.toInt() to 20, // got -> Goth
  0x8a260000.toInt() to 14, // grc -> Cprt
  0xce260000.toInt() to 7, // grt -> Beng
  0xda260000.toInt() to 40, // grw -> Latn
  0xda460000.toInt() to 40, // gsw -> Latn
  0x67750000         to 22, // gu -> Gujr
  0x86860000.toInt() to 40, // gub -> Latn
  0x8a860000.toInt() to 40, // guc -> Latn
  0x8e860000.toInt() to 40, // gud -> Latn
  0xc6860000.toInt() to 40, // gur -> Latn
  0xda860000.toInt() to 40, // guw -> Latn
  0xde860000.toInt() to 40, // gux -> Latn
  0xe6860000.toInt() to 40, // guz -> Latn
  0x67760000         to 40, // gv -> Latn
  0x96a60000.toInt() to 40, // gvf -> Latn
  0xc6a60000.toInt() to 16, // gvr -> Deva
  0xcaa60000.toInt() to 40, // gvs -> Latn
  0x8ac60000.toInt() to 1, // gwc -> Arab
  0xa2c60000.toInt() to 40, // gwi -> Latn
  0xcec60000.toInt() to 1, // gwt -> Arab
  0xa3060000.toInt() to 40, // gyi -> Latn
  0x68610000         to 40, // ha -> Latn
  0x6861434d         to 1, // ha-CM -> Arab
  0x68615344         to 1, // ha-SD -> Arab
  0x98070000.toInt() to 40, // hag -> Latn
  0xa8070000.toInt() to 24, // hak -> Hans
  0xb0070000.toInt() to 40, // ham -> Latn
  0xd8070000.toInt() to 40, // haw -> Latn
  0xe4070000.toInt() to 1, // haz -> Arab
  0x84270000.toInt() to 40, // hbb -> Latn
  0xe0670000.toInt() to 18, // hdy -> Ethi
  0x68650000         to 27, // he -> Hebr
  0xe0e70000.toInt() to 40, // hhy -> Latn
  0x68690000         to 16, // hi -> Deva
  0x81070000.toInt() to 40, // hia -> Latn
  0x95070000.toInt() to 40, // hif -> Latn
  0x99070000.toInt() to 40, // hig -> Latn
  0x9d070000.toInt() to 40, // hih -> Latn
  0xad070000.toInt() to 40, // hil -> Latn
  0x81670000.toInt() to 40, // hla -> Latn
  0xd1670000.toInt() to 28, // hlu -> Hluw
  0x8d870000.toInt() to 62, // hmd -> Plrd
  0xcd870000.toInt() to 40, // hmt -> Latn
  0x8da70000.toInt() to 1, // hnd -> Arab
  0x91a70000.toInt() to 16, // hne -> Deva
  0xa5a70000.toInt() to 29, // hnj -> Hmng
  0xb5a70000.toInt() to 40, // hnn -> Latn
  0xb9a70000.toInt() to 1, // hno -> Arab
  0x686f0000         to 40, // ho -> Latn
  0x89c70000.toInt() to 16, // hoc -> Deva
  0xa5c70000.toInt() to 16, // hoj -> Deva
  0xcdc70000.toInt() to 40, // hot -> Latn
  0x68720000         to 40, // hr -> Latn
  0x86470000.toInt() to 40, // hsb -> Latn
  0xb6470000.toInt() to 24, // hsn -> Hans
  0x68740000         to 40, // ht -> Latn
  0x68750000         to 40, // hu -> Latn
  0xa2870000.toInt() to 40, // hui -> Latn
  0x68790000         to 3, // hy -> Armn
  0x687a0000         to 40, // hz -> Latn
  0x69610000         to 40, // ia -> Latn
  0xb4080000.toInt() to 40, // ian -> Latn
  0xc4080000.toInt() to 40, // iar -> Latn
  0x80280000.toInt() to 40, // iba -> Latn
  0x84280000.toInt() to 40, // ibb -> Latn
  0xe0280000.toInt() to 40, // iby -> Latn
  0x80480000.toInt() to 40, // ica -> Latn
  0x9c480000.toInt() to 40, // ich -> Latn
  0x69640000         to 40, // id -> Latn
  0x8c680000.toInt() to 40, // idd -> Latn
  0xa0680000.toInt() to 40, // idi -> Latn
  0xd0680000.toInt() to 40, // idu -> Latn
  0x69670000         to 40, // ig -> Latn
  0x84c80000.toInt() to 40, // igb -> Latn
  0x90c80000.toInt() to 40, // ige -> Latn
  0x69690000         to 86, // ii -> Yiii
  0xa5280000.toInt() to 40, // ijj -> Latn
  0x696b0000         to 40, // ik -> Latn
  0xa9480000.toInt() to 40, // ikk -> Latn
  0xcd480000.toInt() to 40, // ikt -> Latn
  0xd9480000.toInt() to 40, // ikw -> Latn
  0xdd480000.toInt() to 40, // ikx -> Latn
  0xb9680000.toInt() to 40, // ilo -> Latn
  0xb9880000.toInt() to 40, // imo -> Latn
  0x696e0000         to 40, // in -> Latn
  0x9da80000.toInt() to 15, // inh -> Cyrl
  0xd1c80000.toInt() to 40, // iou -> Latn
  0xa2280000.toInt() to 40, // iri -> Latn
  0x69730000         to 40, // is -> Latn
  0x69740000         to 40, // it -> Latn
  0x69750000         to 9, // iu -> Cans
  0x69770000         to 27, // iw -> Hebr
  0xb2c80000.toInt() to 40, // iwm -> Latn
  0xcac80000.toInt() to 40, // iws -> Latn
  0x9f280000.toInt() to 40, // izh -> Latn
  0xa3280000.toInt() to 40, // izi -> Latn
  0x6a610000         to 31, // ja -> Jpan
  0x84090000.toInt() to 40, // jab -> Latn
  0xb0090000.toInt() to 40, // jam -> Latn
  0xd0290000.toInt() to 40, // jbu -> Latn
  0xb4890000.toInt() to 40, // jen -> Latn
  0xa8c90000.toInt() to 40, // jgk -> Latn
  0xb8c90000.toInt() to 40, // jgo -> Latn
  0x6a690000         to 27, // ji -> Hebr
  0x85090000.toInt() to 40, // jib -> Latn
  0x89890000.toInt() to 40, // jmc -> Latn
  0xad890000.toInt() to 16, // jml -> Deva
  0x82290000.toInt() to 40, // jra -> Latn
  0xce890000.toInt() to 40, // jut -> Latn
  0x6a760000         to 40, // jv -> Latn
  0x6a770000         to 40, // jw -> Latn
  0x6b610000         to 19, // ka -> Geor
  0x800a0000.toInt() to 15, // kaa -> Cyrl
  0x840a0000.toInt() to 40, // kab -> Latn
  0x880a0000.toInt() to 40, // kac -> Latn
  0x8c0a0000.toInt() to 40, // kad -> Latn
  0xa00a0000.toInt() to 40, // kai -> Latn
  0xa40a0000.toInt() to 40, // kaj -> Latn
  0xb00a0000.toInt() to 40, // kam -> Latn
  0xb80a0000.toInt() to 40, // kao -> Latn
  0x8c2a0000.toInt() to 15, // kbd -> Cyrl
  0xb02a0000.toInt() to 40, // kbm -> Latn
  0xbc2a0000.toInt() to 40, // kbp -> Latn
  0xc02a0000.toInt() to 40, // kbq -> Latn
  0xdc2a0000.toInt() to 40, // kbx -> Latn
  0xe02a0000.toInt() to 1, // kby -> Arab
  0x984a0000.toInt() to 40, // kcg -> Latn
  0xa84a0000.toInt() to 40, // kck -> Latn
  0xac4a0000.toInt() to 40, // kcl -> Latn
  0xcc4a0000.toInt() to 40, // kct -> Latn
  0x906a0000.toInt() to 40, // kde -> Latn
  0x9c6a0000.toInt() to 1, // kdh -> Arab
  0xac6a0000.toInt() to 40, // kdl -> Latn
  0xcc6a0000.toInt() to 80, // kdt -> Thai
  0x808a0000.toInt() to 40, // kea -> Latn
  0xb48a0000.toInt() to 40, // ken -> Latn
  0xe48a0000.toInt() to 40, // kez -> Latn
  0xb8aa0000.toInt() to 40, // kfo -> Latn
  0xc4aa0000.toInt() to 16, // kfr -> Deva
  0xe0aa0000.toInt() to 16, // kfy -> Deva
  0x6b670000         to 40, // kg -> Latn
  0x90ca0000.toInt() to 40, // kge -> Latn
  0x94ca0000.toInt() to 40, // kgf -> Latn
  0xbcca0000.toInt() to 40, // kgp -> Latn
  0x80ea0000.toInt() to 40, // kha -> Latn
  0x84ea0000.toInt() to 73, // khb -> Talu
  0xb4ea0000.toInt() to 16, // khn -> Deva
  0xc0ea0000.toInt() to 40, // khq -> Latn
  0xc8ea0000.toInt() to 40, // khs -> Latn
  0xccea0000.toInt() to 52, // kht -> Mymr
  0xd8ea0000.toInt() to 1, // khw -> Arab
  0xe4ea0000.toInt() to 40, // khz -> Latn
  0x6b690000         to 40, // ki -> Latn
  0xa50a0000.toInt() to 40, // kij -> Latn
  0xd10a0000.toInt() to 40, // kiu -> Latn
  0xd90a0000.toInt() to 40, // kiw -> Latn
  0x6b6a0000         to 40, // kj -> Latn
  0x8d2a0000.toInt() to 40, // kjd -> Latn
  0x992a0000.toInt() to 39, // kjg -> Laoo
  0xc92a0000.toInt() to 40, // kjs -> Latn
  0xe12a0000.toInt() to 40, // kjy -> Latn
  0x6b6b0000         to 15, // kk -> Cyrl
  0x6b6b4146         to 1, // kk-AF -> Arab
  0x6b6b434e         to 1, // kk-CN -> Arab
  0x6b6b4952         to 1, // kk-IR -> Arab
  0x6b6b4d4e         to 1, // kk-MN -> Arab
  0x894a0000.toInt() to 40, // kkc -> Latn
  0xa54a0000.toInt() to 40, // kkj -> Latn
  0x6b6c0000         to 40, // kl -> Latn
  0xb56a0000.toInt() to 40, // kln -> Latn
  0xc16a0000.toInt() to 40, // klq -> Latn
  0xcd6a0000.toInt() to 40, // klt -> Latn
  0xdd6a0000.toInt() to 40, // klx -> Latn
  0x6b6d0000         to 35, // km -> Khmr
  0x858a0000.toInt() to 40, // kmb -> Latn
  0x9d8a0000.toInt() to 40, // kmh -> Latn
  0xb98a0000.toInt() to 40, // kmo -> Latn
  0xc98a0000.toInt() to 40, // kms -> Latn
  0xd18a0000.toInt() to 40, // kmu -> Latn
  0xd98a0000.toInt() to 40, // kmw -> Latn
  0x6b6e0000         to 36, // kn -> Knda
  0xbdaa0000.toInt() to 40, // knp -> Latn
  0x6b6f0000         to 37, // ko -> Kore
  0xa1ca0000.toInt() to 15, // koi -> Cyrl
  0xa9ca0000.toInt() to 16, // kok -> Deva
  0xadca0000.toInt() to 40, // kol -> Latn
  0xc9ca0000.toInt() to 40, // kos -> Latn
  0xe5ca0000.toInt() to 40, // koz -> Latn
  0x91ea0000.toInt() to 40, // kpe -> Latn
  0x95ea0000.toInt() to 40, // kpf -> Latn
  0xb9ea0000.toInt() to 40, // kpo -> Latn
  0xc5ea0000.toInt() to 40, // kpr -> Latn
  0xddea0000.toInt() to 40, // kpx -> Latn
  0x860a0000.toInt() to 40, // kqb -> Latn
  0x960a0000.toInt() to 40, // kqf -> Latn
  0xca0a0000.toInt() to 40, // kqs -> Latn
  0xe20a0000.toInt() to 18, // kqy -> Ethi
  0x8a2a0000.toInt() to 15, // krc -> Cyrl
  0xa22a0000.toInt() to 40, // kri -> Latn
  0xa62a0000.toInt() to 40, // krj -> Latn
  0xae2a0000.toInt() to 40, // krl -> Latn
  0xca2a0000.toInt() to 40, // krs -> Latn
  0xd22a0000.toInt() to 16, // kru -> Deva
  0x6b730000         to 1, // ks -> Arab
  0x864a0000.toInt() to 40, // ksb -> Latn
  0x8e4a0000.toInt() to 40, // ksd -> Latn
  0x964a0000.toInt() to 40, // ksf -> Latn
  0x9e4a0000.toInt() to 40, // ksh -> Latn
  0xa64a0000.toInt() to 40, // ksj -> Latn
  0xc64a0000.toInt() to 40, // ksr -> Latn
  0x866a0000.toInt() to 18, // ktb -> Ethi
  0xb26a0000.toInt() to 40, // ktm -> Latn
  0xba6a0000.toInt() to 40, // kto -> Latn
  0x6b750000         to 40, // ku -> Latn
  0x6b754952         to 1, // ku-IR -> Arab
  0x6b754c42         to 1, // ku-LB -> Arab
  0x868a0000.toInt() to 40, // kub -> Latn
  0x8e8a0000.toInt() to 40, // kud -> Latn
  0x928a0000.toInt() to 40, // kue -> Latn
  0xa68a0000.toInt() to 40, // kuj -> Latn
  0xb28a0000.toInt() to 15, // kum -> Cyrl
  0xb68a0000.toInt() to 40, // kun -> Latn
  0xbe8a0000.toInt() to 40, // kup -> Latn
  0xca8a0000.toInt() to 40, // kus -> Latn
  0x6b760000         to 15, // kv -> Cyrl
  0x9aaa0000.toInt() to 40, // kvg -> Latn
  0xc6aa0000.toInt() to 40, // kvr -> Latn
  0xdeaa0000.toInt() to 1, // kvx -> Arab
  0x6b770000         to 40, // kw -> Latn
  0xa6ca0000.toInt() to 40, // kwj -> Latn
  0xbaca0000.toInt() to 40, // kwo -> Latn
  0x82ea0000.toInt() to 40, // kxa -> Latn
  0x8aea0000.toInt() to 18, // kxc -> Ethi
  0xb2ea0000.toInt() to 80, // kxm -> Thai
  0xbeea0000.toInt() to 1, // kxp -> Arab
  0xdaea0000.toInt() to 40, // kxw -> Latn
  0xe6ea0000.toInt() to 40, // kxz -> Latn
  0x6b790000         to 15, // ky -> Cyrl
  0x6b79434e         to 1, // ky-CN -> Arab
  0x6b795452         to 40, // ky-TR -> Latn
  0x930a0000.toInt() to 40, // kye -> Latn
  0xdf0a0000.toInt() to 40, // kyx -> Latn
  0xc72a0000.toInt() to 40, // kzr -> Latn
  0x6c610000         to 40, // la -> Latn
  0x840b0000.toInt() to 42, // lab -> Lina
  0x8c0b0000.toInt() to 27, // lad -> Hebr
  0x980b0000.toInt() to 40, // lag -> Latn
  0x9c0b0000.toInt() to 1, // lah -> Arab
  0xa40b0000.toInt() to 40, // laj -> Latn
  0xc80b0000.toInt() to 40, // las -> Latn
  0x6c620000         to 40, // lb -> Latn
  0x902b0000.toInt() to 15, // lbe -> Cyrl
  0xd02b0000.toInt() to 40, // lbu -> Latn
  0xd82b0000.toInt() to 40, // lbw -> Latn
  0xb04b0000.toInt() to 40, // lcm -> Latn
  0xbc4b0000.toInt() to 80, // lcp -> Thai
  0x846b0000.toInt() to 40, // ldb -> Latn
  0x8c8b0000.toInt() to 40, // led -> Latn
  0x908b0000.toInt() to 40, // lee -> Latn
  0xb08b0000.toInt() to 40, // lem -> Latn
  0xbc8b0000.toInt() to 41, // lep -> Lepc
  0xc08b0000.toInt() to 40, // leq -> Latn
  0xd08b0000.toInt() to 40, // leu -> Latn
  0xe48b0000.toInt() to 15, // lez -> Cyrl
  0x6c670000         to 40, // lg -> Latn
  0x98cb0000.toInt() to 40, // lgg -> Latn
  0x6c690000         to 40, // li -> Latn
  0x810b0000.toInt() to 40, // lia -> Latn
  0x8d0b0000.toInt() to 40, // lid -> Latn
  0x950b0000.toInt() to 16, // lif -> Deva
  0x990b0000.toInt() to 40, // lig -> Latn
  0x9d0b0000.toInt() to 40, // lih -> Latn
  0xa50b0000.toInt() to 40, // lij -> Latn
  0xc90b0000.toInt() to 43, // lis -> Lisu
  0xbd2b0000.toInt() to 40, // ljp -> Latn
  0xa14b0000.toInt() to 1, // lki -> Arab
  0xcd4b0000.toInt() to 40, // lkt -> Latn
  0x916b0000.toInt() to 40, // lle -> Latn
  0xb56b0000.toInt() to 40, // lln -> Latn
  0xb58b0000.toInt() to 77, // lmn -> Telu
  0xb98b0000.toInt() to 40, // lmo -> Latn
  0xbd8b0000.toInt() to 40, // lmp -> Latn
  0x6c6e0000         to 40, // ln -> Latn
  0xc9ab0000.toInt() to 40, // lns -> Latn
  0xd1ab0000.toInt() to 40, // lnu -> Latn
  0x6c6f0000         to 39, // lo -> Laoo
  0xa5cb0000.toInt() to 40, // loj -> Latn
  0xa9cb0000.toInt() to 40, // lok -> Latn
  0xadcb0000.toInt() to 40, // lol -> Latn
  0xc5cb0000.toInt() to 40, // lor -> Latn
  0xc9cb0000.toInt() to 40, // los -> Latn
  0xe5cb0000.toInt() to 40, // loz -> Latn
  0x8a2b0000.toInt() to 1, // lrc -> Arab
  0x6c740000         to 40, // lt -> Latn
  0x9a6b0000.toInt() to 40, // ltg -> Latn
  0x6c750000         to 40, // lu -> Latn
  0x828b0000.toInt() to 40, // lua -> Latn
  0xba8b0000.toInt() to 40, // luo -> Latn
  0xe28b0000.toInt() to 40, // luy -> Latn
  0xe68b0000.toInt() to 1, // luz -> Arab
  0x6c760000         to 40, // lv -> Latn
  0xaecb0000.toInt() to 80, // lwl -> Thai
  0x9f2b0000.toInt() to 24, // lzh -> Hans
  0xe72b0000.toInt() to 40, // lzz -> Latn
  0x8c0c0000.toInt() to 40, // mad -> Latn
  0x940c0000.toInt() to 40, // maf -> Latn
  0x980c0000.toInt() to 16, // mag -> Deva
  0xa00c0000.toInt() to 16, // mai -> Deva
  0xa80c0000.toInt() to 40, // mak -> Latn
  0xb40c0000.toInt() to 40, // man -> Latn
  0xb40c474e.toInt() to 54, // man-GN -> Nkoo
  0xc80c0000.toInt() to 40, // mas -> Latn
  0xd80c0000.toInt() to 40, // maw -> Latn
  0xe40c0000.toInt() to 40, // maz -> Latn
  0x9c2c0000.toInt() to 40, // mbh -> Latn
  0xb82c0000.toInt() to 40, // mbo -> Latn
  0xc02c0000.toInt() to 40, // mbq -> Latn
  0xd02c0000.toInt() to 40, // mbu -> Latn
  0xd82c0000.toInt() to 40, // mbw -> Latn
  0xa04c0000.toInt() to 40, // mci -> Latn
  0xbc4c0000.toInt() to 40, // mcp -> Latn
  0xc04c0000.toInt() to 40, // mcq -> Latn
  0xc44c0000.toInt() to 40, // mcr -> Latn
  0xd04c0000.toInt() to 40, // mcu -> Latn
  0x806c0000.toInt() to 40, // mda -> Latn
  0x906c0000.toInt() to 1, // mde -> Arab
  0x946c0000.toInt() to 15, // mdf -> Cyrl
  0x9c6c0000.toInt() to 40, // mdh -> Latn
  0xa46c0000.toInt() to 40, // mdj -> Latn
  0xc46c0000.toInt() to 40, // mdr -> Latn
  0xdc6c0000.toInt() to 18, // mdx -> Ethi
  0x8c8c0000.toInt() to 40, // med -> Latn
  0x908c0000.toInt() to 40, // mee -> Latn
  0xa88c0000.toInt() to 40, // mek -> Latn
  0xb48c0000.toInt() to 40, // men -> Latn
  0xc48c0000.toInt() to 40, // mer -> Latn
  0xcc8c0000.toInt() to 40, // met -> Latn
  0xd08c0000.toInt() to 40, // meu -> Latn
  0x80ac0000.toInt() to 1, // mfa -> Arab
  0x90ac0000.toInt() to 40, // mfe -> Latn
  0xb4ac0000.toInt() to 40, // mfn -> Latn
  0xb8ac0000.toInt() to 40, // mfo -> Latn
  0xc0ac0000.toInt() to 40, // mfq -> Latn
  0x6d670000         to 40, // mg -> Latn
  0x9ccc0000.toInt() to 40, // mgh -> Latn
  0xaccc0000.toInt() to 40, // mgl -> Latn
  0xb8cc0000.toInt() to 40, // mgo -> Latn
  0xbccc0000.toInt() to 16, // mgp -> Deva
  0xe0cc0000.toInt() to 40, // mgy -> Latn
  0x6d680000         to 40, // mh -> Latn
  0xa0ec0000.toInt() to 40, // mhi -> Latn
  0xacec0000.toInt() to 40, // mhl -> Latn
  0x6d690000         to 40, // mi -> Latn
  0x950c0000.toInt() to 40, // mif -> Latn
  0xb50c0000.toInt() to 40, // min -> Latn
  0xc90c0000.toInt() to 26, // mis -> Hatr
  0xd90c0000.toInt() to 40, // miw -> Latn
  0x6d6b0000         to 15, // mk -> Cyrl
  0xa14c0000.toInt() to 1, // mki -> Arab
  0xad4c0000.toInt() to 40, // mkl -> Latn
  0xbd4c0000.toInt() to 40, // mkp -> Latn
  0xd94c0000.toInt() to 40, // mkw -> Latn
  0x6d6c0000         to 49, // ml -> Mlym
  0x916c0000.toInt() to 40, // mle -> Latn
  0xbd6c0000.toInt() to 40, // mlp -> Latn
  0xc96c0000.toInt() to 40, // mls -> Latn
  0xb98c0000.toInt() to 40, // mmo -> Latn
  0xd18c0000.toInt() to 40, // mmu -> Latn
  0xdd8c0000.toInt() to 40, // mmx -> Latn
  0x6d6e0000         to 15, // mn -> Cyrl
  0x6d6e434e         to 50, // mn-CN -> Mong
  0x81ac0000.toInt() to 40, // mna -> Latn
  0x95ac0000.toInt() to 40, // mnf -> Latn
  0xa1ac0000.toInt() to 7, // mni -> Beng
  0xd9ac0000.toInt() to 52, // mnw -> Mymr
  0x81cc0000.toInt() to 40, // moa -> Latn
  0x91cc0000.toInt() to 40, // moe -> Latn
  0x9dcc0000.toInt() to 40, // moh -> Latn
  0xc9cc0000.toInt() to 40, // mos -> Latn
  0xddcc0000.toInt() to 40, // mox -> Latn
  0xbdec0000.toInt() to 40, // mpp -> Latn
  0xc9ec0000.toInt() to 40, // mps -> Latn
  0xcdec0000.toInt() to 40, // mpt -> Latn
  0xddec0000.toInt() to 40, // mpx -> Latn
  0xae0c0000.toInt() to 40, // mql -> Latn
  0x6d720000         to 16, // mr -> Deva
  0x8e2c0000.toInt() to 16, // mrd -> Deva
  0xa62c0000.toInt() to 15, // mrj -> Cyrl
  0xba2c0000.toInt() to 51, // mro -> Mroo
  0x6d730000         to 40, // ms -> Latn
  0x6d734343         to 1, // ms-CC -> Arab
  0x6d734944         to 1, // ms-ID -> Arab
  0x6d740000         to 40, // mt -> Latn
  0x8a6c0000.toInt() to 40, // mtc -> Latn
  0x966c0000.toInt() to 40, // mtf -> Latn
  0xa26c0000.toInt() to 40, // mti -> Latn
  0xc66c0000.toInt() to 16, // mtr -> Deva
  0x828c0000.toInt() to 40, // mua -> Latn
  0xc68c0000.toInt() to 40, // mur -> Latn
  0xca8c0000.toInt() to 40, // mus -> Latn
  0x82ac0000.toInt() to 40, // mva -> Latn
  0xb6ac0000.toInt() to 40, // mvn -> Latn
  0xe2ac0000.toInt() to 1, // mvy -> Arab
  0xaacc0000.toInt() to 40, // mwk -> Latn
  0xc6cc0000.toInt() to 16, // mwr -> Deva
  0xd6cc0000.toInt() to 40, // mwv -> Latn
  0x8aec0000.toInt() to 40, // mxc -> Latn
  0xb2ec0000.toInt() to 40, // mxm -> Latn
  0x6d790000         to 52, // my -> Mymr
  0xab0c0000.toInt() to 40, // myk -> Latn
  0xb30c0000.toInt() to 18, // mym -> Ethi
  0xd70c0000.toInt() to 15, // myv -> Cyrl
  0xdb0c0000.toInt() to 40, // myw -> Latn
  0xdf0c0000.toInt() to 40, // myx -> Latn
  0xe70c0000.toInt() to 46, // myz -> Mand
  0xab2c0000.toInt() to 40, // mzk -> Latn
  0xb32c0000.toInt() to 40, // mzm -> Latn
  0xb72c0000.toInt() to 1, // mzn -> Arab
  0xbf2c0000.toInt() to 40, // mzp -> Latn
  0xdb2c0000.toInt() to 40, // mzw -> Latn
  0xe72c0000.toInt() to 40, // mzz -> Latn
  0x6e610000         to 40, // na -> Latn
  0x880d0000.toInt() to 40, // nac -> Latn
  0x940d0000.toInt() to 40, // naf -> Latn
  0xa80d0000.toInt() to 40, // nak -> Latn
  0xb40d0000.toInt() to 24, // nan -> Hans
  0xbc0d0000.toInt() to 40, // nap -> Latn
  0xc00d0000.toInt() to 40, // naq -> Latn
  0xc80d0000.toInt() to 40, // nas -> Latn
  0x6e620000         to 40, // nb -> Latn
  0x804d0000.toInt() to 40, // nca -> Latn
  0x904d0000.toInt() to 40, // nce -> Latn
  0x944d0000.toInt() to 40, // ncf -> Latn
  0x9c4d0000.toInt() to 40, // nch -> Latn
  0xb84d0000.toInt() to 40, // nco -> Latn
  0xd04d0000.toInt() to 40, // ncu -> Latn
  0x6e640000         to 40, // nd -> Latn
  0x886d0000.toInt() to 40, // ndc -> Latn
  0xc86d0000.toInt() to 40, // nds -> Latn
  0x6e650000         to 16, // ne -> Deva
  0x848d0000.toInt() to 40, // neb -> Latn
  0xd88d0000.toInt() to 16, // new -> Deva
  0xdc8d0000.toInt() to 40, // nex -> Latn
  0xc4ad0000.toInt() to 40, // nfr -> Latn
  0x6e670000         to 40, // ng -> Latn
  0x80cd0000.toInt() to 40, // nga -> Latn
  0x84cd0000.toInt() to 40, // ngb -> Latn
  0xaccd0000.toInt() to 40, // ngl -> Latn
  0x84ed0000.toInt() to 40, // nhb -> Latn
  0x90ed0000.toInt() to 40, // nhe -> Latn
  0xd8ed0000.toInt() to 40, // nhw -> Latn
  0x950d0000.toInt() to 40, // nif -> Latn
  0xa10d0000.toInt() to 40, // nii -> Latn
  0xa50d0000.toInt() to 40, // nij -> Latn
  0xb50d0000.toInt() to 40, // nin -> Latn
  0xd10d0000.toInt() to 40, // niu -> Latn
  0xe10d0000.toInt() to 40, // niy -> Latn
  0xe50d0000.toInt() to 40, // niz -> Latn
  0xb92d0000.toInt() to 40, // njo -> Latn
  0x994d0000.toInt() to 40, // nkg -> Latn
  0xb94d0000.toInt() to 40, // nko -> Latn
  0x6e6c0000         to 40, // nl -> Latn
  0x998d0000.toInt() to 40, // nmg -> Latn
  0xe58d0000.toInt() to 40, // nmz -> Latn
  0x6e6e0000         to 40, // nn -> Latn
  0x95ad0000.toInt() to 40, // nnf -> Latn
  0x9dad0000.toInt() to 40, // nnh -> Latn
  0xa9ad0000.toInt() to 40, // nnk -> Latn
  0xb1ad0000.toInt() to 40, // nnm -> Latn
  0x6e6f0000         to 40, // no -> Latn
  0x8dcd0000.toInt() to 38, // nod -> Lana
  0x91cd0000.toInt() to 16, // noe -> Deva
  0xb5cd0000.toInt() to 64, // non -> Runr
  0xbdcd0000.toInt() to 40, // nop -> Latn
  0xd1cd0000.toInt() to 40, // nou -> Latn
  0xba0d0000.toInt() to 54, // nqo -> Nkoo
  0x6e720000         to 40, // nr -> Latn
  0x862d0000.toInt() to 40, // nrb -> Latn
  0xaa4d0000.toInt() to 9, // nsk -> Cans
  0xb64d0000.toInt() to 40, // nsn -> Latn
  0xba4d0000.toInt() to 40, // nso -> Latn
  0xca4d0000.toInt() to 40, // nss -> Latn
  0xb26d0000.toInt() to 40, // ntm -> Latn
  0xc66d0000.toInt() to 40, // ntr -> Latn
  0xa28d0000.toInt() to 40, // nui -> Latn
  0xbe8d0000.toInt() to 40, // nup -> Latn
  0xca8d0000.toInt() to 40, // nus -> Latn
  0xd68d0000.toInt() to 40, // nuv -> Latn
  0xde8d0000.toInt() to 40, // nux -> Latn
  0x6e760000         to 40, // nv -> Latn
  0x86cd0000.toInt() to 40, // nwb -> Latn
  0xc2ed0000.toInt() to 40, // nxq -> Latn
  0xc6ed0000.toInt() to 40, // nxr -> Latn
  0x6e790000         to 40, // ny -> Latn
  0xb30d0000.toInt() to 40, // nym -> Latn
  0xb70d0000.toInt() to 40, // nyn -> Latn
  0xa32d0000.toInt() to 40, // nzi -> Latn
  0x6f630000         to 40, // oc -> Latn
  0x88ce0000.toInt() to 40, // ogc -> Latn
  0xc54e0000.toInt() to 40, // okr -> Latn
  0xd54e0000.toInt() to 40, // okv -> Latn
  0x6f6d0000         to 40, // om -> Latn
  0x99ae0000.toInt() to 40, // ong -> Latn
  0xb5ae0000.toInt() to 40, // onn -> Latn
  0xc9ae0000.toInt() to 40, // ons -> Latn
  0xb1ee0000.toInt() to 40, // opm -> Latn
  0x6f720000         to 57, // or -> Orya
  0xba2e0000.toInt() to 40, // oro -> Latn
  0xd22e0000.toInt() to 1, // oru -> Arab
  0x6f730000         to 15, // os -> Cyrl
  0x824e0000.toInt() to 58, // osa -> Osge
  0x826e0000.toInt() to 1, // ota -> Arab
  0xaa6e0000.toInt() to 56, // otk -> Orkh
  0xb32e0000.toInt() to 40, // ozm -> Latn
  0x70610000         to 23, // pa -> Guru
  0x7061504b         to 1, // pa-PK -> Arab
  0x980f0000.toInt() to 40, // pag -> Latn
  0xac0f0000.toInt() to 60, // pal -> Phli
  0xb00f0000.toInt() to 40, // pam -> Latn
  0xbc0f0000.toInt() to 40, // pap -> Latn
  0xd00f0000.toInt() to 40, // pau -> Latn
  0xa02f0000.toInt() to 40, // pbi -> Latn
  0x8c4f0000.toInt() to 40, // pcd -> Latn
  0xb04f0000.toInt() to 40, // pcm -> Latn
  0x886f0000.toInt() to 40, // pdc -> Latn
  0xcc6f0000.toInt() to 40, // pdt -> Latn
  0x8c8f0000.toInt() to 40, // ped -> Latn
  0xb88f0000.toInt() to 84, // peo -> Xpeo
  0xdc8f0000.toInt() to 40, // pex -> Latn
  0xacaf0000.toInt() to 40, // pfl -> Latn
  0xacef0000.toInt() to 1, // phl -> Arab
  0xb4ef0000.toInt() to 61, // phn -> Phnx
  0xad0f0000.toInt() to 40, // pil -> Latn
  0xbd0f0000.toInt() to 40, // pip -> Latn
  0x814f0000.toInt() to 8, // pka -> Brah
  0xb94f0000.toInt() to 40, // pko -> Latn
  0x706c0000         to 40, // pl -> Latn
  0x816f0000.toInt() to 40, // pla -> Latn
  0xc98f0000.toInt() to 40, // pms -> Latn
  0x99af0000.toInt() to 40, // png -> Latn
  0xb5af0000.toInt() to 40, // pnn -> Latn
  0xcdaf0000.toInt() to 21, // pnt -> Grek
  0xb5cf0000.toInt() to 40, // pon -> Latn
  0xb9ef0000.toInt() to 40, // ppo -> Latn
  0x822f0000.toInt() to 34, // pra -> Khar
  0x8e2f0000.toInt() to 1, // prd -> Arab
  0x9a2f0000.toInt() to 40, // prg -> Latn
  0x70730000         to 1, // ps -> Arab
  0xca4f0000.toInt() to 40, // pss -> Latn
  0x70740000         to 40, // pt -> Latn
  0xbe6f0000.toInt() to 40, // ptp -> Latn
  0xd28f0000.toInt() to 40, // puu -> Latn
  0x82cf0000.toInt() to 40, // pwa -> Latn
  0x71750000         to 40, // qu -> Latn
  0x8a900000.toInt() to 40, // quc -> Latn
  0x9a900000.toInt() to 40, // qug -> Latn
  0xa0110000.toInt() to 40, // rai -> Latn
  0xa4110000.toInt() to 16, // raj -> Deva
  0xb8110000.toInt() to 40, // rao -> Latn
  0x94510000.toInt() to 40, // rcf -> Latn
  0xa4910000.toInt() to 40, // rej -> Latn
  0xac910000.toInt() to 40, // rel -> Latn
  0xc8910000.toInt() to 40, // res -> Latn
  0xb4d10000.toInt() to 40, // rgn -> Latn
  0x98f10000.toInt() to 1, // rhg -> Arab
  0x81110000.toInt() to 40, // ria -> Latn
  0x95110000.toInt() to 78, // rif -> Tfng
  0x95114e4c.toInt() to 40, // rif-NL -> Latn
  0xc9310000.toInt() to 16, // rjs -> Deva
  0xcd510000.toInt() to 7, // rkt -> Beng
  0x726d0000         to 40, // rm -> Latn
  0x95910000.toInt() to 40, // rmf -> Latn
  0xb9910000.toInt() to 40, // rmo -> Latn
  0xcd910000.toInt() to 1, // rmt -> Arab
  0xd1910000.toInt() to 40, // rmu -> Latn
  0x726e0000         to 40, // rn -> Latn
  0x81b10000.toInt() to 40, // rna -> Latn
  0x99b10000.toInt() to 40, // rng -> Latn
  0x726f0000         to 40, // ro -> Latn
  0x85d10000.toInt() to 40, // rob -> Latn
  0x95d10000.toInt() to 40, // rof -> Latn
  0xb9d10000.toInt() to 40, // roo -> Latn
  0xba310000.toInt() to 40, // rro -> Latn
  0xb2710000.toInt() to 40, // rtm -> Latn
  0x72750000         to 15, // ru -> Cyrl
  0x92910000.toInt() to 15, // rue -> Cyrl
  0x9a910000.toInt() to 40, // rug -> Latn
  0x72770000         to 40, // rw -> Latn
  0xaad10000.toInt() to 40, // rwk -> Latn
  0xbad10000.toInt() to 40, // rwo -> Latn
  0xd3110000.toInt() to 33, // ryu -> Kana
  0x73610000         to 16, // sa -> Deva
  0x94120000.toInt() to 40, // saf -> Latn
  0x9c120000.toInt() to 15, // sah -> Cyrl
  0xc0120000.toInt() to 40, // saq -> Latn
  0xc8120000.toInt() to 40, // sas -> Latn
  0xcc120000.toInt() to 40, // sat -> Latn
  0xe4120000.toInt() to 67, // saz -> Saur
  0x80320000.toInt() to 40, // sba -> Latn
  0x90320000.toInt() to 40, // sbe -> Latn
  0xbc320000.toInt() to 40, // sbp -> Latn
  0x73630000         to 40, // sc -> Latn
  0xa8520000.toInt() to 16, // sck -> Deva
  0xac520000.toInt() to 1, // scl -> Arab
  0xb4520000.toInt() to 40, // scn -> Latn
  0xb8520000.toInt() to 40, // sco -> Latn
  0xc8520000.toInt() to 40, // scs -> Latn
  0x73640000         to 1, // sd -> Arab
  0x88720000.toInt() to 40, // sdc -> Latn
  0x9c720000.toInt() to 1, // sdh -> Arab
  0x73650000         to 40, // se -> Latn
  0x94920000.toInt() to 40, // sef -> Latn
  0x9c920000.toInt() to 40, // seh -> Latn
  0xa0920000.toInt() to 40, // sei -> Latn
  0xc8920000.toInt() to 40, // ses -> Latn
  0x73670000         to 40, // sg -> Latn
  0x80d20000.toInt() to 55, // sga -> Ogam
  0xc8d20000.toInt() to 40, // sgs -> Latn
  0xd8d20000.toInt() to 18, // sgw -> Ethi
  0xe4d20000.toInt() to 40, // sgz -> Latn
  0x73680000         to 40, // sh -> Latn
  0xa0f20000.toInt() to 78, // shi -> Tfng
  0xa8f20000.toInt() to 40, // shk -> Latn
  0xb4f20000.toInt() to 52, // shn -> Mymr
  0xd0f20000.toInt() to 1, // shu -> Arab
  0x73690000         to 69, // si -> Sinh
  0x8d120000.toInt() to 40, // sid -> Latn
  0x99120000.toInt() to 40, // sig -> Latn
  0xad120000.toInt() to 40, // sil -> Latn
  0xb1120000.toInt() to 40, // sim -> Latn
  0xc5320000.toInt() to 40, // sjr -> Latn
  0x736b0000         to 40, // sk -> Latn
  0x89520000.toInt() to 40, // skc -> Latn
  0xc5520000.toInt() to 1, // skr -> Arab
  0xc9520000.toInt() to 40, // sks -> Latn
  0x736c0000         to 40, // sl -> Latn
  0x8d720000.toInt() to 40, // sld -> Latn
  0xa1720000.toInt() to 40, // sli -> Latn
  0xad720000.toInt() to 40, // sll -> Latn
  0xe1720000.toInt() to 40, // sly -> Latn
  0x736d0000         to 40, // sm -> Latn
  0x81920000.toInt() to 40, // sma -> Latn
  0xa5920000.toInt() to 40, // smj -> Latn
  0xb5920000.toInt() to 40, // smn -> Latn
  0xbd920000.toInt() to 65, // smp -> Samr
  0xc1920000.toInt() to 40, // smq -> Latn
  0xc9920000.toInt() to 40, // sms -> Latn
  0x736e0000         to 40, // sn -> Latn
  0x89b20000.toInt() to 40, // snc -> Latn
  0xa9b20000.toInt() to 40, // snk -> Latn
  0xbdb20000.toInt() to 40, // snp -> Latn
  0xddb20000.toInt() to 40, // snx -> Latn
  0xe1b20000.toInt() to 40, // sny -> Latn
  0x736f0000         to 40, // so -> Latn
  0xa9d20000.toInt() to 40, // sok -> Latn
  0xc1d20000.toInt() to 40, // soq -> Latn
  0xd1d20000.toInt() to 80, // sou -> Thai
  0xe1d20000.toInt() to 40, // soy -> Latn
  0x8df20000.toInt() to 40, // spd -> Latn
  0xadf20000.toInt() to 40, // spl -> Latn
  0xc9f20000.toInt() to 40, // sps -> Latn
  0x73710000         to 40, // sq -> Latn
  0x73720000         to 15, // sr -> Cyrl
  0x73724d45         to 40, // sr-ME -> Latn
  0x7372524f         to 40, // sr-RO -> Latn
  0x73725255         to 40, // sr-RU -> Latn
  0x73725452         to 40, // sr-TR -> Latn
  0x86320000.toInt() to 70, // srb -> Sora
  0xb6320000.toInt() to 40, // srn -> Latn
  0xc6320000.toInt() to 40, // srr -> Latn
  0xde320000.toInt() to 16, // srx -> Deva
  0x73730000         to 40, // ss -> Latn
  0x8e520000.toInt() to 40, // ssd -> Latn
  0x9a520000.toInt() to 40, // ssg -> Latn
  0xe2520000.toInt() to 40, // ssy -> Latn
  0x73740000         to 40, // st -> Latn
  0xaa720000.toInt() to 40, // stk -> Latn
  0xc2720000.toInt() to 40, // stq -> Latn
  0x73750000         to 40, // su -> Latn
  0x82920000.toInt() to 40, // sua -> Latn
  0x92920000.toInt() to 40, // sue -> Latn
  0xaa920000.toInt() to 40, // suk -> Latn
  0xc6920000.toInt() to 40, // sur -> Latn
  0xca920000.toInt() to 40, // sus -> Latn
  0x73760000         to 40, // sv -> Latn
  0x73770000         to 40, // sw -> Latn
  0x86d20000.toInt() to 1, // swb -> Arab
  0x8ad20000.toInt() to 40, // swc -> Latn
  0x9ad20000.toInt() to 40, // swg -> Latn
  0xbed20000.toInt() to 40, // swp -> Latn
  0xd6d20000.toInt() to 16, // swv -> Deva
  0xb6f20000.toInt() to 40, // sxn -> Latn
  0xdaf20000.toInt() to 40, // sxw -> Latn
  0xaf120000.toInt() to 7, // syl -> Beng
  0xc7120000.toInt() to 71, // syr -> Syrc
  0xaf320000.toInt() to 40, // szl -> Latn
  0x74610000         to 74, // ta -> Taml
  0xa4130000.toInt() to 16, // taj -> Deva
  0xac130000.toInt() to 40, // tal -> Latn
  0xb4130000.toInt() to 40, // tan -> Latn
  0xc0130000.toInt() to 40, // taq -> Latn
  0x88330000.toInt() to 40, // tbc -> Latn
  0x8c330000.toInt() to 40, // tbd -> Latn
  0x94330000.toInt() to 40, // tbf -> Latn
  0x98330000.toInt() to 40, // tbg -> Latn
  0xb8330000.toInt() to 40, // tbo -> Latn
  0xd8330000.toInt() to 40, // tbw -> Latn
  0xe4330000.toInt() to 40, // tbz -> Latn
  0xa0530000.toInt() to 40, // tci -> Latn
  0xe0530000.toInt() to 36, // tcy -> Knda
  0x8c730000.toInt() to 72, // tdd -> Tale
  0x98730000.toInt() to 16, // tdg -> Deva
  0x9c730000.toInt() to 16, // tdh -> Deva
  0x74650000         to 77, // te -> Telu
  0x8c930000.toInt() to 40, // ted -> Latn
  0xb0930000.toInt() to 40, // tem -> Latn
  0xb8930000.toInt() to 40, // teo -> Latn
  0xcc930000.toInt() to 40, // tet -> Latn
  0xa0b30000.toInt() to 40, // tfi -> Latn
  0x74670000         to 15, // tg -> Cyrl
  0x7467504b         to 1, // tg-PK -> Arab
  0x88d30000.toInt() to 40, // tgc -> Latn
  0xb8d30000.toInt() to 40, // tgo -> Latn
  0xd0d30000.toInt() to 40, // tgu -> Latn
  0x74680000         to 80, // th -> Thai
  0xacf30000.toInt() to 16, // thl -> Deva
  0xc0f30000.toInt() to 16, // thq -> Deva
  0xc4f30000.toInt() to 16, // thr -> Deva
  0x74690000         to 18, // ti -> Ethi
  0x95130000.toInt() to 40, // tif -> Latn
  0x99130000.toInt() to 18, // tig -> Ethi
  0xa9130000.toInt() to 40, // tik -> Latn
  0xb1130000.toInt() to 40, // tim -> Latn
  0xb9130000.toInt() to 40, // tio -> Latn
  0xd5130000.toInt() to 40, // tiv -> Latn
  0x746b0000         to 40, // tk -> Latn
  0xad530000.toInt() to 40, // tkl -> Latn
  0xc5530000.toInt() to 40, // tkr -> Latn
  0xcd530000.toInt() to 16, // tkt -> Deva
  0x746c0000         to 40, // tl -> Latn
  0x95730000.toInt() to 40, // tlf -> Latn
  0xdd730000.toInt() to 40, // tlx -> Latn
  0xe1730000.toInt() to 40, // tly -> Latn
  0x9d930000.toInt() to 40, // tmh -> Latn
  0xe1930000.toInt() to 40, // tmy -> Latn
  0x746e0000         to 40, // tn -> Latn
  0x9db30000.toInt() to 40, // tnh -> Latn
  0x746f0000         to 40, // to -> Latn
  0x95d30000.toInt() to 40, // tof -> Latn
  0x99d30000.toInt() to 40, // tog -> Latn
  0xc1d30000.toInt() to 40, // toq -> Latn
  0xa1f30000.toInt() to 40, // tpi -> Latn
  0xb1f30000.toInt() to 40, // tpm -> Latn
  0xe5f30000.toInt() to 40, // tpz -> Latn
  0xba130000.toInt() to 40, // tqo -> Latn
  0x74720000         to 40, // tr -> Latn
  0xd2330000.toInt() to 40, // tru -> Latn
  0xd6330000.toInt() to 40, // trv -> Latn
  0xda330000.toInt() to 1, // trw -> Arab
  0x74730000         to 40, // ts -> Latn
  0x8e530000.toInt() to 21, // tsd -> Grek
  0x96530000.toInt() to 16, // tsf -> Deva
  0x9a530000.toInt() to 40, // tsg -> Latn
  0xa6530000.toInt() to 81, // tsj -> Tibt
  0xda530000.toInt() to 40, // tsw -> Latn
  0x74740000         to 15, // tt -> Cyrl
  0x8e730000.toInt() to 40, // ttd -> Latn
  0x92730000.toInt() to 40, // tte -> Latn
  0xa6730000.toInt() to 40, // ttj -> Latn
  0xc6730000.toInt() to 40, // ttr -> Latn
  0xca730000.toInt() to 80, // tts -> Thai
  0xce730000.toInt() to 40, // ttt -> Latn
  0x9e930000.toInt() to 40, // tuh -> Latn
  0xae930000.toInt() to 40, // tul -> Latn
  0xb2930000.toInt() to 40, // tum -> Latn
  0xc2930000.toInt() to 40, // tuq -> Latn
  0x8eb30000.toInt() to 40, // tvd -> Latn
  0xaeb30000.toInt() to 40, // tvl -> Latn
  0xd2b30000.toInt() to 40, // tvu -> Latn
  0x9ed30000.toInt() to 40, // twh -> Latn
  0xc2d30000.toInt() to 40, // twq -> Latn
  0x9af30000.toInt() to 75, // txg -> Tang
  0x74790000         to 40, // ty -> Latn
  0x83130000.toInt() to 40, // tya -> Latn
  0xd7130000.toInt() to 15, // tyv -> Cyrl
  0xb3330000.toInt() to 40, // tzm -> Latn
  0xd0340000.toInt() to 40, // ubu -> Latn
  0xb0740000.toInt() to 15, // udm -> Cyrl
  0x75670000         to 1, // ug -> Arab
  0x75674b5a         to 15, // ug-KZ -> Cyrl
  0x75674d4e         to 15, // ug-MN -> Cyrl
  0x80d40000.toInt() to 82, // uga -> Ugar
  0x756b0000         to 15, // uk -> Cyrl
  0xa1740000.toInt() to 40, // uli -> Latn
  0x85940000.toInt() to 40, // umb -> Latn
  0xc5b40000.toInt() to 7, // unr -> Beng
  0xc5b44e50.toInt() to 16, // unr-NP -> Deva
  0xddb40000.toInt() to 7, // unx -> Beng
  0x75720000         to 1, // ur -> Arab
  0xa2340000.toInt() to 40, // uri -> Latn
  0xce340000.toInt() to 40, // urt -> Latn
  0xda340000.toInt() to 40, // urw -> Latn
  0x82540000.toInt() to 40, // usa -> Latn
  0xc6740000.toInt() to 40, // utr -> Latn
  0x9eb40000.toInt() to 40, // uvh -> Latn
  0xaeb40000.toInt() to 40, // uvl -> Latn
  0x757a0000         to 40, // uz -> Latn
  0x757a4146         to 1, // uz-AF -> Arab
  0x757a434e         to 15, // uz-CN -> Cyrl
  0x98150000.toInt() to 40, // vag -> Latn
  0xa0150000.toInt() to 83, // vai -> Vaii
  0xb4150000.toInt() to 40, // van -> Latn
  0x76650000         to 40, // ve -> Latn
  0x88950000.toInt() to 40, // vec -> Latn
  0xbc950000.toInt() to 40, // vep -> Latn
  0x76690000         to 40, // vi -> Latn
  0x89150000.toInt() to 40, // vic -> Latn
  0xd5150000.toInt() to 40, // viv -> Latn
  0xc9750000.toInt() to 40, // vls -> Latn
  0x95950000.toInt() to 40, // vmf -> Latn
  0xd9950000.toInt() to 40, // vmw -> Latn
  0x766f0000         to 40, // vo -> Latn
  0xcdd50000.toInt() to 40, // vot -> Latn
  0xba350000.toInt() to 40, // vro -> Latn
  0xb6950000.toInt() to 40, // vun -> Latn
  0xce950000.toInt() to 40, // vut -> Latn
  0x77610000         to 40, // wa -> Latn
  0x90160000.toInt() to 40, // wae -> Latn
  0xa4160000.toInt() to 40, // waj -> Latn
  0xac160000.toInt() to 18, // wal -> Ethi
  0xb4160000.toInt() to 40, // wan -> Latn
  0xc4160000.toInt() to 40, // war -> Latn
  0xbc360000.toInt() to 40, // wbp -> Latn
  0xc0360000.toInt() to 77, // wbq -> Telu
  0xc4360000.toInt() to 16, // wbr -> Deva
  0xa0560000.toInt() to 40, // wci -> Latn
  0xc4960000.toInt() to 40, // wer -> Latn
  0xa0d60000.toInt() to 40, // wgi -> Latn
  0x98f60000.toInt() to 40, // whg -> Latn
  0x85160000.toInt() to 40, // wib -> Latn
  0xd1160000.toInt() to 40, // wiu -> Latn
  0xd5160000.toInt() to 40, // wiv -> Latn
  0x81360000.toInt() to 40, // wja -> Latn
  0xa1360000.toInt() to 40, // wji -> Latn
  0xc9760000.toInt() to 40, // wls -> Latn
  0xb9960000.toInt() to 40, // wmo -> Latn
  0x89b60000.toInt() to 40, // wnc -> Latn
  0xa1b60000.toInt() to 1, // wni -> Arab
  0xd1b60000.toInt() to 40, // wnu -> Latn
  0x776f0000         to 40, // wo -> Latn
  0x85d60000.toInt() to 40, // wob -> Latn
  0xc9d60000.toInt() to 40, // wos -> Latn
  0xca360000.toInt() to 40, // wrs -> Latn
  0xaa560000.toInt() to 40, // wsk -> Latn
  0xb2760000.toInt() to 16, // wtm -> Deva
  0xd2960000.toInt() to 24, // wuu -> Hans
  0xd6960000.toInt() to 40, // wuv -> Latn
  0x82d60000.toInt() to 40, // wwa -> Latn
  0xd4170000.toInt() to 40, // xav -> Latn
  0xa0370000.toInt() to 40, // xbi -> Latn
  0xc4570000.toInt() to 10, // xcr -> Cari
  0xc8970000.toInt() to 40, // xes -> Latn
  0x78680000         to 40, // xh -> Latn
  0x81770000.toInt() to 40, // xla -> Latn
  0x89770000.toInt() to 44, // xlc -> Lyci
  0x8d770000.toInt() to 45, // xld -> Lydi
  0x95970000.toInt() to 19, // xmf -> Geor
  0xb5970000.toInt() to 47, // xmn -> Mani
  0xc5970000.toInt() to 48, // xmr -> Merc
  0x81b70000.toInt() to 53, // xna -> Narb
  0xc5b70000.toInt() to 16, // xnr -> Deva
  0x99d70000.toInt() to 40, // xog -> Latn
  0xb5d70000.toInt() to 40, // xon -> Latn
  0xc5f70000.toInt() to 63, // xpr -> Prti
  0x86370000.toInt() to 40, // xrb -> Latn
  0x82570000.toInt() to 66, // xsa -> Sarb
  0xa2570000.toInt() to 40, // xsi -> Latn
  0xb2570000.toInt() to 40, // xsm -> Latn
  0xc6570000.toInt() to 16, // xsr -> Deva
  0x92d70000.toInt() to 40, // xwe -> Latn
  0xb0180000.toInt() to 40, // yam -> Latn
  0xb8180000.toInt() to 40, // yao -> Latn
  0xbc180000.toInt() to 40, // yap -> Latn
  0xc8180000.toInt() to 40, // yas -> Latn
  0xcc180000.toInt() to 40, // yat -> Latn
  0xd4180000.toInt() to 40, // yav -> Latn
  0xe0180000.toInt() to 40, // yay -> Latn
  0xe4180000.toInt() to 40, // yaz -> Latn
  0x80380000.toInt() to 40, // yba -> Latn
  0x84380000.toInt() to 40, // ybb -> Latn
  0xe0380000.toInt() to 40, // yby -> Latn
  0xc4980000.toInt() to 40, // yer -> Latn
  0xc4d80000.toInt() to 40, // ygr -> Latn
  0xd8d80000.toInt() to 40, // ygw -> Latn
  0x79690000         to 27, // yi -> Hebr
  0xb9580000.toInt() to 40, // yko -> Latn
  0x91780000.toInt() to 40, // yle -> Latn
  0x99780000.toInt() to 40, // ylg -> Latn
  0xad780000.toInt() to 40, // yll -> Latn
  0xad980000.toInt() to 40, // yml -> Latn
  0x796f0000         to 40, // yo -> Latn
  0xb5d80000.toInt() to 40, // yon -> Latn
  0x86380000.toInt() to 40, // yrb -> Latn
  0x92380000.toInt() to 40, // yre -> Latn
  0xae380000.toInt() to 40, // yrl -> Latn
  0xca580000.toInt() to 40, // yss -> Latn
  0x82980000.toInt() to 40, // yua -> Latn
  0x92980000.toInt() to 25, // yue -> Hant
  0x9298434e.toInt() to 24, // yue-CN -> Hans
  0xa6980000.toInt() to 40, // yuj -> Latn
  0xce980000.toInt() to 40, // yut -> Latn
  0xda980000.toInt() to 40, // yuw -> Latn
  0x7a610000         to 40, // za -> Latn
  0x98190000.toInt() to 40, // zag -> Latn
  0xa4790000.toInt() to 1, // zdj -> Arab
  0x80990000.toInt() to 40, // zea -> Latn
  0x9cd90000.toInt() to 78, // zgh -> Tfng
  0x7a680000         to 24, // zh -> Hans
  0x7a684155         to 25, // zh-AU -> Hant
  0x7a68424e         to 25, // zh-BN -> Hant
  0x7a684742         to 25, // zh-GB -> Hant
  0x7a684746         to 25, // zh-GF -> Hant
  0x7a68484b         to 25, // zh-HK -> Hant
  0x7a684944         to 25, // zh-ID -> Hant
  0x7a684d4f         to 25, // zh-MO -> Hant
  0x7a684d59         to 25, // zh-MY -> Hant
  0x7a685041         to 25, // zh-PA -> Hant
  0x7a685046         to 25, // zh-PF -> Hant
  0x7a685048         to 25, // zh-PH -> Hant
  0x7a685352         to 25, // zh-SR -> Hant
  0x7a685448         to 25, // zh-TH -> Hant
  0x7a685457         to 25, // zh-TW -> Hant
  0x7a685553         to 25, // zh-US -> Hant
  0x7a68564e         to 25, // zh-VN -> Hant
  0x81190000.toInt() to 40, // zia -> Latn
  0xb1790000.toInt() to 40, // zlm -> Latn
  0xa1990000.toInt() to 40, // zmi -> Latn
  0x91b90000.toInt() to 40, // zne -> Latn
  0x7a750000         to 40, // zu -> Latn
  0x83390000.toInt() to 40) // zza -> Latn

internal val REPRESENTATIVE_LOCALES = setOf(
  0x616145544C61746EL, // aa_Latn_ET
  0x616247454379726CL, // ab_Cyrl_GE
  -0x3bdfb8b7b39e8b92L, // abr_Latn_GH
  -0x6fbfb6bbb39e8b92L, // ace_Latn_ID
  -0x63bfaab8b39e8b92L, // ach_Latn_UG
  -0x7f9fb8b7b39e8b92L, // ada_Latn_GH
  -0x1f9fadaabc868d94L, // ady_Cyrl_RU
  0x6165495241767374L, // ae_Avst_IR
  -0x7b7fabb1be8d9e9eL, // aeb_Arab_TN
  0x61665A414C61746EL, // af_Latn_ZA
  -0x3f3fbcb2b39e8b92L, // agq_Latn_CM
  -0x471fb6b1be979093L, // aho_Ahom_IN
  0x616B47484C61746EL, // ak_Latn_GH
  -0x56bfb6aea78c8a88L, // akk_Xsux_IQ
  -0x4a9fa7b4b39e8b92L, // aln_Latn_XK
  -0x329fadaabc868d94L, // alt_Cyrl_RU
  0x616D455445746869L, // am_Ethi_ET
  -0x467fb1b8b39e8b92L, // amo_Latn_NG
  -0x1a3fb6bbb39e8b92L, // aoz_Latn_ID
  -0x721fabb8be8d9e9eL, // apd_Arab_TG
  0x6172454741726162L, // ar_Arab_EG
  -0x75dfb6adbe8d9297L, // arc_Armi_IR
  -0x75dfb5b0b19d9e8cL, // arc_Nbat_JO
  -0x75dfaca6af9e9393L, // arc_Palm_SY
  -0x49dfbcb3b39e8b92L, // arn_Latn_CL
  -0x45dfbdb0b39e8b92L, // aro_Latn_BO
  -0x3ddfbba5be8d9e9eL, // arq_Arab_DZ
  -0x1ddfb2bebe8d9e9eL, // ary_Arab_MA
  -0x19dfbab8be8d9e9eL, // arz_Arab_EG
  0x6173494E42656E67L, // as_Beng_IN
  -0x7dbfaba5b39e8b92L, // asa_Latn_TZ
  -0x6dbfaaacac989189L, // ase_Sgnw_US
  -0x31bfbaacb39e8b92L, // ast_Latn_ES
  -0x599fbcbeb39e8b92L, // atj_Latn_CA
  0x617652554379726CL, // av_Cyrl_RU
  -0x7d3fb6b1bb9a899fL, // awa_Deva_IN
  0x6179424F4C61746EL, // ay_Latn_BO
  0x617A495241726162L, // az_Arab_IR
  0x617A415A4C61746EL, // az_Latn_AZ
  0x626152554379726CL, // ba_Cyrl_RU
  -0x53feafb4be8d9e9eL, // bal_Arab_PK
  -0x4bfeb6bbb39e8b92L, // ban_Latn_ID
  -0x43feb1afbb9a899fL, // bap_Deva_NP
  -0x3bfebeabb39e8b92L, // bar_Latn_AT
  -0x37febcb2b39e8b92L, // bas_Latn_CM
  -0x23febcb2bd9e928bL, // bax_Bamu_CM
  -0x77deb6bbb39e8b92L, // bbc_Latn_ID
  -0x5bdebcb2b39e8b92L, // bbj_Latn_CM
  -0x5fbebcb6b39e8b92L, // bci_Latn_CI
  0x626542594379726CL, // be_Cyrl_BY
  -0x5b7eacbbbe8d9e9eL, // bej_Arab_SD
  -0x4f7ea5b2b39e8b92L, // bem_Latn_ZM
  -0x277eb6bbb39e8b92L, // bew_Latn_ID
  -0x1b7eaba5b39e8b92L, // bez_Latn_TZ
  -0x735ebcb2b39e8b92L, // bfd_Latn_CM
  -0x3f5eb6b1ab9e9294L, // bfq_Taml_IN
  -0x335eafb4be8d9e9eL, // bft_Arab_PK
  -0x1f5eb6b1bb9a899fL, // bfy_Deva_IN
  0x626742474379726CL, // bg_Cyrl_BG
  -0x773eb6b1bb9a899fL, // bgc_Deva_IN
  -0x4b3eafb4be8d9e9eL, // bgn_Arab_PK
  -0x233eabadb88d9a95L, // bgx_Grek_TR
  -0x7b1eb6b1bb9a899fL, // bhb_Deva_IN
  -0x5f1eb6b1bb9a899fL, // bhi_Deva_IN
  -0x571eafb7b39e8b92L, // bhk_Latn_PH
  -0x471eb6b1bb9a899fL, // bho_Deva_IN
  0x626956554C61746EL, // bi_Latn_VU
  -0x56feafb7b39e8b92L, // bik_Latn_PH
  -0x4afeb1b8b39e8b92L, // bin_Latn_NG
  -0x5adeb6b1bb9a899fL, // bjj_Deva_IN
  -0x4adeb6bbb39e8b92L, // bjn_Latn_ID
  -0x4ebebcb2b39e8b92L, // bkm_Latn_CM
  -0x2ebeafb7b39e8b92L, // bku_Latn_PH
  -0x329ea9b1ab9e898cL, // blt_Tavt_VN
  0x626D4D4C4C61746EL, // bm_Latn_ML
  -0x3e7eb2b3b39e8b92L, // bmq_Latn_ML
  0x626E424442656E67L, // bn_Beng_BD
  0x626F434E54696274L, // bo_Tibt_CN
  -0x1e1eb6b1bd9a9199L, // bpy_Beng_IN
  -0x5dfeb6adbe8d9e9eL, // bqi_Arab_IR
  -0x29febcb6b39e8b92L, // bqv_Latn_CI
  0x627246524C61746EL, // br_Latn_FR
  -0x7ddeb6b1bb9a899fL, // bra_Deva_IN
  -0x61deafb4be8d9e9eL, // brh_Arab_PK
  -0x21deb6b1bb9a899fL, // brx_Deva_IN
  0x627342414C61746EL, // bs_Latn_BA
  -0x3dbeb3adbd9e8c8dL, // bsq_Bass_LR
  -0x35bebcb2b39e8b92L, // bss_Latn_CM
  -0x459eafb7b39e8b92L, // bto_Latn_PH
  -0x299eafb4bb9a899fL, // btv_Deva_PK
  -0x7d7eadaabc868d94L, // bua_Cyrl_RU
  -0x757ea6abb39e8b92L, // buc_Latn_YT
  -0x657eb6bbb39e8b92L, // bug_Latn_ID
  -0x4d7ebcb2b39e8b92L, // bum_Latn_CM
  -0x795eb8aeb39e8b92L, // bvb_Latn_GQ
  -0x48febaadba8b9797L, // byn_Ethi_ER
  -0x28febcb2b39e8b92L, // byv_Latn_CM
  -0x6cdeb2b3b39e8b92L, // bze_Latn_ML
  0x636145534C61746EL, // ca_Latn_ES
  -0x63bdb1b8b39e8b92L, // cch_Latn_NG
  -0x43bdb6b1bd9a9199L, // ccp_Beng_IN
  -0x43bdbdbbbc9e9493L, // ccp_Cakm_BD
  0x636552554379726CL, // ce_Cyrl_RU
  -0x7b7dafb7b39e8b92L, // ceb_Latn_PH
  -0x673daab8b39e8b92L, // cgg_Latn_UG
  0x636847554C61746EL, // ch_Latn_GU
  -0x571db9b2b39e8b92L, // chk_Latn_FM
  -0x4f1dadaabc868d94L, // chm_Cyrl_RU
  -0x471daaacb39e8b92L, // cho_Latn_US
  -0x431dbcbeb39e8b92L, // chp_Latn_CA
  -0x3b1daaacbc979a8eL, // chr_Cher_US
  -0x7eddb4b7be8d9e9eL, // cja_Arab_KH
  -0x4edda9b1bc979e93L, // cjm_Cham_VN
  -0x7abdb6aebe8d9e9eL, // ckb_Arab_IQ
  0x636F46524C61746EL, // co_Latn_FR
  -0x423dbab8bc908f8cL, // cop_Copt_EG
  -0x361dafb7b39e8b92L, // cps_Latn_PH
  0x6372434143616E73L, // cr_Cans_CA
  -0x59ddbcbebc9e918dL, // crj_Cans_CA
  -0x55ddbcbebc9e918dL, // crk_Cans_CA
  -0x51ddbcbebc9e918dL, // crl_Cans_CA
  -0x4dddbcbebc9e918dL, // crm_Cans_CA
  -0x35ddacbcb39e8b92L, // crs_Latn_SC
  0x6373435A4C61746EL, // cs_Latn_CZ
  -0x79bdafb3b39e8b92L, // csb_Latn_PL
  -0x25bdbcbebc9e918dL, // csw_Cans_CA
  -0x719db2b2af9e8a9dL, // ctd_Pauc_MM
  0x637552554379726CL, // cu_Cyrl_RU
  0x63754247476C6167L, // cu_Glag_BG
  0x637652554379726CL, // cv_Cyrl_RU
  0x637947424C61746EL, // cy_Latn_GB
  0x6461444B4C61746EL, // da_Latn_DK
  -0x57fcaaacb39e8b92L, // dak_Latn_US
  -0x3bfcadaabc868d94L, // dar_Cyrl_RU
  -0x2bfcb4bab39e8b92L, // dav_Latn_KE
  -0x77bcb6b1be8d9e9eL, // dcc_Arab_IN
  0x646544454C61746EL, // de_Latn_DE
  -0x4b7cbcbeb39e8b92L, // den_Latn_CA
  -0x3b3cbcbeb39e8b92L, // dgr_Latn_CA
  -0x6edcb1bab39e8b92L, // dje_Latn_NE
  -0x5a5cbcb6b39e8b92L, // dnj_Latn_CI
  -0x5e3cb6b1be8d9e9eL, // doi_Arab_IN
  -0x79bcbbbab39e8b92L, // dsb_Latn_DE
  -0x4d9cb2b3b39e8b92L, // dtm_Latn_ML
  -0x419cb2a6b39e8b92L, // dtp_Latn_MY
  -0x1d9cb1afbb9a899fL, // dty_Deva_NP
  -0x7d7cbcb2b39e8b92L, // dua_Latn_CM
  0x64764D5654686161L, // dv_Thaa_MV
  -0x44fcacb1b39e8b92L, // dyo_Latn_SN
  -0x2cfcbdb9b39e8b92L, // dyu_Latn_BF
  0x647A425454696274L, // dz_Tibt_BT
  -0x2fdbb4bab39e8b92L, // ebu_Latn_KE
  0x656547484C61746EL, // ee_Latn_GH
  -0x5f5bb1b8b39e8b92L, // efi_Latn_NG
  -0x533bb6abb39e8b92L, // egl_Latn_IT
  -0x1f3bbab8ba988690L, // egy_Egyp_EG
  -0x1ebbb2b2b49e9397L, // eky_Kali_MM
  0x656C47524772656BL, // el_Grek_GR
  0x656E47424C61746EL, // en_Latn_GB
  0x656E55534C61746EL, // en_Latn_US
  0x656E474253686177L, // en_Shaw_GB
  0x657345534C61746EL, // es_Latn_ES
  0x65734D584C61746EL, // es_Latn_MX
  0x657355534C61746EL, // es_Latn_US
  -0x2dbbaaacb39e8b92L, // esu_Latn_US
  0x657445454C61746EL, // et_Latn_EE
  -0x319bb6abb68b9e94L, // ett_Ital_IT
  0x657545534C61746EL, // eu_Latn_ES
  -0x453bbcb2b39e8b92L, // ewo_Latn_CM
  -0x311bbaacb39e8b92L, // ext_Latn_ES
  0x6661495241726162L, // fa_Arab_IR
  -0x4bfab8aeb39e8b92L, // fan_Latn_GQ
  0x6666474E41646C6DL, // ff_Adlm_GN
  0x6666534E4C61746EL, // ff_Latn_SN
  -0x4f5ab2b3b39e8b92L, // ffm_Latn_ML
  0x666946494C61746EL, // fi_Latn_FI
  -0x7efaacbbbe8d9e9eL, // fia_Arab_SD
  -0x52faafb7b39e8b92L, // fil_Latn_PH
  -0x32faacbab39e8b92L, // fit_Latn_SE
  0x666A464A4C61746EL, // fj_Latn_FJ
  0x666F464F4C61746EL, // fo_Latn_FO
  -0x4a3abdb5b39e8b92L, // fon_Latn_BJ
  0x667246524C61746EL, // fr_Latn_FR
  -0x75daaaacb39e8b92L, // frc_Latn_US
  -0x41dab9adb39e8b92L, // frp_Latn_FR
  -0x39dabbbab39e8b92L, // frr_Latn_DE
  -0x35dabbbab39e8b92L, // frs_Latn_DE
  -0x797abcb2be8d9e9eL, // fub_Arab_CM
  -0x717aa8b9b39e8b92L, // fud_Latn_WF
  -0x697ab8b1b39e8b92L, // fuf_Latn_GN
  -0x3d7ab1bab39e8b92L, // fuq_Latn_NE
  -0x397ab6abb39e8b92L, // fur_Latn_IT
  -0x297ab1b8b39e8b92L, // fuv_Latn_NG
  -0x395aacbbb39e8b92L, // fvr_Latn_SD
  0x66794E4C4C61746EL, // fy_Latn_NL
  0x676149454C61746EL, // ga_Latn_IE
  -0x7ff9b8b7b39e8b92L, // gaa_Latn_GH
  -0x67f9b2bbb39e8b92L, // gag_Latn_MD
  -0x4bf9bcb1b79e918dL, // gan_Hans_CN
  -0x1ff9b6bbb39e8b92L, // gay_Latn_ID
  -0x4fd9b6b1bb9a899fL, // gbm_Deva_IN
  -0x1bd9b6adbe8d9e9eL, // gbz_Arab_IR
  -0x3bb9b8b9b39e8b92L, // gcr_Latn_GF
  0x676447424C61746EL, // gd_Latn_GB
  -0x1b79baabba8b9797L, // gez_Ethi_ET
  -0x4b39b1afbb9a899fL, // ggn_Deva_NP
  -0x52f9b4b6b39e8b92L, // gil_Latn_KI
  -0x56d9afb4be8d9e9eL, // gjk_Arab_PK
  -0x2ed9afb4be8d9e9eL, // gju_Arab_PK
  0x676C45534C61746EL, // gl_Latn_ES
  -0x5699b6adbe8d9e9eL, // glk_Arab_IR
  0x676E50594C61746EL, // gn_Latn_PY
  -0x4e39b6b1bb9a899fL, // gom_Deva_IN
  -0x4a39b6b1ab9a938bL, // gon_Telu_IN
  -0x3a39b6bbb39e8b92L, // gor_Latn_ID
  -0x3639b1b3b39e8b92L, // gos_Latn_NL
  -0x3239aabeb8908b98L, // got_Goth_UA
  -0x75d9bca6bc8f8d8cL, // grc_Cprt_CY
  -0x75d9b8adb396919eL, // grc_Linb_GR
  -0x31d9b6b1bd9a9199L, // grt_Beng_IN
  -0x25b9bcb7b39e8b92L, // gsw_Latn_CH
  0x6775494E47756A72L, // gu_Gujr_IN
  -0x7979bdadb39e8b92L, // gub_Latn_BR
  -0x7579bcb0b39e8b92L, // guc_Latn_CO
  -0x3979b8b7b39e8b92L, // gur_Latn_GH
  -0x1979b4bab39e8b92L, // guz_Latn_KE
  0x6776494D4C61746EL, // gv_Latn_IM
  -0x3959b1afbb9a899fL, // gvr_Deva_NP
  -0x5d39bcbeb39e8b92L, // gwi_Latn_CA
  0x68614E474C61746EL, // ha_Latn_NG
  -0x57f8bcb1b79e918dL, // hak_Hans_CN
  -0x27f8aaacb39e8b92L, // haw_Latn_US
  -0x1bf8beb9be8d9e9eL, // haz_Arab_AF
  0x6865494C48656272L, // he_Hebr_IL
  0x6869494E44657661L, // hi_Deva_IN
  -0x6af8b9b5b39e8b92L, // hif_Latn_FJ
  -0x52f8afb7b39e8b92L, // hil_Latn_PH
  -0x2e98abadb7938a89L, // hlu_Hluw_TR
  -0x7278bcb1af938d9cL, // hmd_Plrd_CN
  -0x7258afb4be8d9e9eL, // hnd_Arab_PK
  -0x6e58b6b1bb9a899fL, // hne_Deva_IN
  -0x5a58b3beb7929199L, // hnj_Hmng_LA
  -0x4a58afb7b39e8b92L, // hnn_Latn_PH
  -0x4658afb4be8d9e9eL, // hno_Arab_PK
  0x686F50474C61746EL, // ho_Latn_PG
  -0x7638b6b1bb9a899fL, // hoc_Deva_IN
  -0x5a38b6b1bb9a899fL, // hoj_Deva_IN
  0x687248524C61746EL, // hr_Latn_HR
  -0x79b8bbbab39e8b92L, // hsb_Latn_DE
  -0x49b8bcb1b79e918dL, // hsn_Hans_CN
  0x687448544C61746EL, // ht_Latn_HT
  0x687548554C61746EL, // hu_Latn_HU
  0x6879414D41726D6EL, // hy_Armn_AM
  0x687A4E414C61746EL, // hz_Latn_NA
  0x696146524C61746EL, // ia_Latn_FR
  -0x7fd7b2a6b39e8b92L, // iba_Latn_MY
  -0x7bd7b1b8b39e8b92L, // ibb_Latn_NG
  0x696449444C61746EL, // id_Latn_ID
  0x69674E474C61746EL, // ig_Latn_NG
  0x6969434E59696969L, // ii_Yiii_CN
  0x696B55534C61746EL, // ik_Latn_US
  -0x32b7bcbeb39e8b92L, // ikt_Latn_CA
  -0x4697afb7b39e8b92L, // ilo_Latn_PH
  0x696E49444C61746EL, // in_Latn_ID
  -0x6257adaabc868d94L, // inh_Cyrl_RU
  0x697349534C61746EL, // is_Latn_IS
  0x697449544C61746EL, // it_Latn_IT
  0x6975434143616E73L, // iu_Cans_CA
  0x6977494C48656272L, // iw_Hebr_IL
  -0x60d7adaab39e8b92L, // izh_Latn_RU
  0x6A614A504A70616EL, // ja_Jpan_JP
  -0x4ff6b5b2b39e8b92L, // jam_Latn_JM
  -0x4736bcb2b39e8b92L, // jgo_Latn_CM
  -0x7676aba5b39e8b92L, // jmc_Latn_TZ
  -0x5276b1afbb9a899fL, // jml_Deva_NP
  -0x3176bbb4b39e8b92L, // jut_Latn_DK
  0x6A7649444C61746EL, // jv_Latn_ID
  0x6A7749444C61746EL, // jw_Latn_ID
  0x6B61474547656F72L, // ka_Geor_GE
  -0x7ff5aaa5bc868d94L, // kaa_Cyrl_UZ
  -0x7bf5bba5b39e8b92L, // kab_Latn_DZ
  -0x77f5b2b2b39e8b92L, // kac_Latn_MM
  -0x5bf5b1b8b39e8b92L, // kaj_Latn_NG
  -0x4ff5b4bab39e8b92L, // kam_Latn_KE
  -0x47f5b2b3b39e8b92L, // kao_Latn_ML
  -0x73d5adaabc868d94L, // kbd_Cyrl_RU
  -0x1fd5b1babe8d9e9eL, // kby_Arab_NE
  -0x67b5b1b8b39e8b92L, // kcg_Latn_NG
  -0x57b5a5a8b39e8b92L, // kck_Latn_ZW
  -0x6f95aba5b39e8b92L, // kde_Latn_TZ
  -0x6395abb8be8d9e9eL, // kdh_Arab_TG
  -0x3395abb7ab979e97L, // kdt_Thai_TH
  -0x7f75bca9b39e8b92L, // kea_Latn_CV
  -0x4b75bcb2b39e8b92L, // ken_Latn_CM
  -0x4755bcb6b39e8b92L, // kfo_Latn_CI
  -0x3b55b6b1bb9a899fL, // kfr_Deva_IN
  -0x1f55b6b1bb9a899fL, // kfy_Deva_IN
  0x6B6743444C61746EL, // kg_Latn_CD
  -0x6f35b6bbb39e8b92L, // kge_Latn_ID
  -0x4335bdadb39e8b92L, // kgp_Latn_BR
  -0x7f15b6b1b39e8b92L, // kha_Latn_IN
  -0x7b15bcb1ab9e938bL, // khb_Talu_CN
  -0x4b15b6b1bb9a899fL, // khn_Deva_IN
  -0x3f15b2b3b39e8b92L, // khq_Latn_ML
  -0x3315b6b1b286928eL, // kht_Mymr_IN
  -0x2715afb4be8d9e9eL, // khw_Arab_PK
  0x6B694B454C61746EL, // ki_Latn_KE
  -0x2ef5abadb39e8b92L, // kiu_Latn_TR
  0x6B6A4E414C61746EL, // kj_Latn_NA
  -0x66d5b3beb39e9091L, // kjg_Laoo_LA
  0x6B6B434E41726162L, // kk_Arab_CN
  0x6B6B4B5A4379726CL, // kk_Cyrl_KZ
  -0x5ab5bcb2b39e8b92L, // kkj_Latn_CM
  0x6B6C474C4C61746EL, // kl_Latn_GL
  -0x4a95b4bab39e8b92L, // kln_Latn_KE
  0x6B6D4B484B686D72L, // km_Khmr_KH
  -0x7a75beb0b39e8b92L, // kmb_Latn_AO
  0x6B6E494E4B6E6461L, // kn_Knda_IN
  0x6B6F4B524B6F7265L, // ko_Kore_KR
  -0x5e35adaabc868d94L, // koi_Cyrl_RU
  -0x5635b6b1bb9a899fL, // kok_Deva_IN
  -0x3635b9b2b39e8b92L, // kos_Latn_FM
  -0x6e15b3adb39e8b92L, // kpe_Latn_LR
  -0x75d5adaabc868d94L, // krc_Cyrl_RU
  -0x5dd5acb3b39e8b92L, // kri_Latn_SL
  -0x59d5afb7b39e8b92L, // krj_Latn_PH
  -0x51d5adaab39e8b92L, // krl_Latn_RU
  -0x2dd5b6b1bb9a899fL, // kru_Deva_IN
  0x6B73494E41726162L, // ks_Arab_IN
  -0x79b5aba5b39e8b92L, // ksb_Latn_TZ
  -0x69b5bcb2b39e8b92L, // ksf_Latn_CM
  -0x61b5bbbab39e8b92L, // ksh_Latn_DE
  0x6B75495141726162L, // ku_Arab_IQ
  0x6B7554524C61746EL, // ku_Latn_TR
  -0x4d75adaabc868d94L, // kum_Cyrl_RU
  0x6B7652554379726CL, // kv_Cyrl_RU
  -0x3955b6bbb39e8b92L, // kvr_Latn_ID
  -0x2155afb4be8d9e9eL, // kvx_Arab_PK
  0x6B7747424C61746EL, // kw_Latn_GB
  -0x4d15abb7ab979e97L, // kxm_Thai_TH
  -0x4115afb4be8d9e9eL, // kxp_Arab_PK
  0x6B79434E41726162L, // ky_Arab_CN
  0x6B794B474379726CL, // ky_Cyrl_KG
  0x6B7954524C61746EL, // ky_Latn_TR
  0x6C6156414C61746EL, // la_Latn_VA
  -0x7bf4b8adb396919fL, // lab_Lina_GR
  -0x73f4b6b3b79a9d8eL, // lad_Hebr_IL
  -0x67f4aba5b39e8b92L, // lag_Latn_TZ
  -0x63f4afb4be8d9e9eL, // lah_Arab_PK
  -0x5bf4aab8b39e8b92L, // laj_Latn_UG
  0x6C624C554C61746EL, // lb_Latn_LU
  -0x6fd4adaabc868d94L, // lbe_Cyrl_RU
  -0x27d4b6bbb39e8b92L, // lbw_Latn_ID
  -0x43b4bcb1ab979e97L, // lcp_Thai_CN
  -0x4374b6b1b39a8f9dL, // lep_Lepc_IN
  -0x1b74adaabc868d94L, // lez_Cyrl_RU
  0x6C6755474C61746EL, // lg_Latn_UG
  0x6C694E4C4C61746EL, // li_Latn_NL
  -0x6af4b1afbb9a899fL, // lif_Deva_NP
  -0x6af4b6b1b396929eL, // lif_Limb_IN
  -0x5af4b6abb39e8b92L, // lij_Latn_IT
  -0x36f4bcb1b3968c8bL, // lis_Lisu_CN
  -0x42d4b6bbb39e8b92L, // ljp_Latn_ID
  -0x5eb4b6adbe8d9e9eL, // lki_Arab_IR
  -0x32b4aaacb39e8b92L, // lkt_Latn_US
  -0x4a74b6b1ab9a938bL, // lmn_Telu_IN
  -0x4674b6abb39e8b92L, // lmo_Latn_IT
  0x6C6E43444C61746EL, // ln_Latn_CD
  0x6C6F4C414C616F6FL, // lo_Laoo_LA
  -0x5234bcbbb39e8b92L, // lol_Latn_CD
  -0x1a34a5b2b39e8b92L, // loz_Latn_ZM
  -0x75d4b6adbe8d9e9eL, // lrc_Arab_IR
  0x6C744C544C61746EL, // lt_Latn_LT
  -0x6594b3a9b39e8b92L, // ltg_Latn_LV
  0x6C7543444C61746EL, // lu_Latn_CD
  -0x7d74bcbbb39e8b92L, // lua_Latn_CD
  -0x4574b4bab39e8b92L, // luo_Latn_KE
  -0x1d74b4bab39e8b92L, // luy_Latn_KE
  -0x1974b6adbe8d9e9eL, // luz_Arab_IR
  0x6C764C564C61746EL, // lv_Latn_LV
  -0x5134abb7ab979e97L, // lwl_Thai_TH
  -0x60d4bcb1b79e918dL, // lzh_Hans_CN
  -0x18d4abadb39e8b92L, // lzz_Latn_TR
  -0x73f3b6bbb39e8b92L, // mad_Latn_ID
  -0x6bf3bcb2b39e8b92L, // maf_Latn_CM
  -0x67f3b6b1bb9a899fL, // mag_Deva_IN
  -0x5ff3b6b1bb9a899fL, // mai_Deva_IN
  -0x57f3b6bbb39e8b92L, // mak_Latn_ID
  -0x4bf3b8b2b39e8b92L, // man_Latn_GM
  -0x4bf3b8b1b1949091L, // man_Nkoo_GN
  -0x37f3b4bab39e8b92L, // mas_Latn_KE
  -0x1bf3b2a7b39e8b92L, // maz_Latn_MX
  -0x6b93adaabc868d94L, // mdf_Cyrl_RU
  -0x6393afb7b39e8b92L, // mdh_Latn_PH
  -0x3b93b6bbb39e8b92L, // mdr_Latn_ID
  -0x4b73acb3b39e8b92L, // men_Latn_SL
  -0x3b73b4bab39e8b92L, // mer_Latn_KE
  -0x7f53abb7be8d9e9eL, // mfa_Arab_TH
  -0x6f53b2aab39e8b92L, // mfe_Latn_MU
  0x6D674D474C61746EL, // mg_Latn_MG
  -0x6333b2a5b39e8b92L, // mgh_Latn_MZ
  -0x4733bcb2b39e8b92L, // mgo_Latn_CM
  -0x4333b1afbb9a899fL, // mgp_Deva_NP
  -0x1f33aba5b39e8b92L, // mgy_Latn_TZ
  0x6D684D484C61746EL, // mh_Latn_MH
  0x6D694E5A4C61746EL, // mi_Latn_NZ
  -0x4af3b6bbb39e8b92L, // min_Latn_ID
  -0x36f3b6aeb79e8b8eL, // mis_Hatr_IQ
  0x6D6B4D4B4379726CL, // mk_Cyrl_MK
  0x6D6C494E4D6C796DL, // ml_Mlym_IN
  -0x3693acbbb39e8b92L, // mls_Latn_SD
  0x6D6E4D4E4379726CL, // mn_Cyrl_MN
  0x6D6E434E4D6F6E67L, // mn_Mong_CN
  -0x5e53b6b1bd9a9199L, // mni_Beng_IN
  -0x2653b2b2b286928eL, // mnw_Mymr_MM
  -0x6e33bcbeb39e8b92L, // moe_Latn_CA
  -0x6233bcbeb39e8b92L, // moh_Latn_CA
  -0x3633bdb9b39e8b92L, // mos_Latn_BF
  0x6D72494E44657661L, // mr_Deva_IN
  -0x71d3b1afbb9a899fL, // mrd_Deva_NP
  -0x59d3adaabc868d94L, // mrj_Cyrl_RU
  -0x45d3bdbbb28d9091L, // mro_Mroo_BD
  0x6D734D594C61746EL, // ms_Latn_MY
  0x6D744D544C61746EL, // mt_Latn_MT
  -0x3993b6b1bb9a899fL, // mtr_Deva_IN
  -0x7d73bcb2b39e8b92L, // mua_Latn_CM
  -0x3573aaacb39e8b92L, // mus_Latn_US
  -0x1d53afb4be8d9e9eL, // mvy_Arab_PK
  -0x5533b2b3b39e8b92L, // mwk_Latn_ML
  -0x3933b6b1bb9a899fL, // mwr_Deva_IN
  -0x2933b6bbb39e8b92L, // mwv_Latn_ID
  -0x7513a5a8b39e8b92L, // mxc_Latn_ZW
  0x6D794D4D4D796D72L, // my_Mymr_MM
  -0x28f3adaabc868d94L, // myv_Cyrl_RU
  -0x20f3aab8b39e8b92L, // myx_Latn_UG
  -0x18f3b6adb29e919cL, // myz_Mand_IR
  -0x48d3b6adbe8d9e9eL, // mzn_Arab_IR
  0x6E614E524C61746EL, // na_Latn_NR
  -0x4bf2bcb1b79e918dL, // nan_Hans_CN
  -0x43f2b6abb39e8b92L, // nap_Latn_IT
  -0x3ff2b1beb39e8b92L, // naq_Latn_NA
  0x6E624E4F4C61746EL, // nb_Latn_NO
  -0x63b2b2a7b39e8b92L, // nch_Latn_MX
  0x6E645A574C61746EL, // nd_Latn_ZW
  -0x7792b2a5b39e8b92L, // ndc_Latn_MZ
  -0x3792bbbab39e8b92L, // nds_Latn_DE
  0x6E654E5044657661L, // ne_Deva_NP
  -0x2772b1afbb9a899fL, // new_Deva_NP
  0x6E674E414C61746EL, // ng_Latn_NA
  -0x5332b2a5b39e8b92L, // ngl_Latn_MZ
  -0x6f12b2a7b39e8b92L, // nhe_Latn_MX
  -0x2712b2a7b39e8b92L, // nhw_Latn_MX
  -0x5af2b6bbb39e8b92L, // nij_Latn_ID
  -0x2ef2b1aab39e8b92L, // niu_Latn_NU
  -0x46d2b6b1b39e8b92L, // njo_Latn_IN
  0x6E6C4E4C4C61746EL, // nl_Latn_NL
  -0x6672bcb2b39e8b92L, // nmg_Latn_CM
  0x6E6E4E4F4C61746EL, // nn_Latn_NO
  -0x6252bcb2b39e8b92L, // nnh_Latn_CM
  0x6E6F4E4F4C61746EL, // no_Latn_NO
  -0x7232abb7b39e919fL, // nod_Lana_TH
  -0x6e32b6b1bb9a899fL, // noe_Deva_IN
  -0x4a32acbaad8a918eL, // non_Runr_SE
  -0x45f2b8b1b1949091L, // nqo_Nkoo_GN
  0x6E725A414C61746EL, // nr_Latn_ZA
  -0x55b2bcbebc9e918dL, // nsk_Cans_CA
  -0x45b2a5beb39e8b92L, // nso_Latn_ZA
  -0x3572acacb39e8b92L, // nus_Latn_SS
  0x6E7655534C61746EL, // nv_Latn_US
  -0x3d12bcb1b39e8b92L, // nxq_Latn_CN
  0x6E794D574C61746EL, // ny_Latn_MW
  -0x4cf2aba5b39e8b92L, // nym_Latn_TZ
  -0x48f2aab8b39e8b92L, // nyn_Latn_UG
  -0x5cd2b8b7b39e8b92L, // nzi_Latn_GH
  0x6F6346524C61746EL, // oc_Latn_FR
  0x6F6D45544C61746EL, // om_Latn_ET
  0x6F72494E4F727961L, // or_Orya_IN
  0x6F7347454379726CL, // os_Cyrl_GE
  -0x7db1aaacb08c989bL, // osa_Osge_US
  -0x5591b2b1b08d9498L, // otk_Orkh_MN
  0x7061504B41726162L, // pa_Arab_PK
  0x7061494E47757275L, // pa_Guru_IN
  -0x67f0afb7b39e8b92L, // pag_Latn_PH
  -0x53f0b6adaf979397L, // pal_Phli_IR
  -0x53f0bcb1af979390L, // pal_Phlp_CN
  -0x4ff0afb7b39e8b92L, // pam_Latn_PH
  -0x43f0bea8b39e8b92L, // pap_Latn_AW
  -0x2ff0afa8b39e8b92L, // pau_Latn_PW
  -0x73b0b9adb39e8b92L, // pcd_Latn_FR
  -0x4fb0b1b8b39e8b92L, // pcm_Latn_NG
  -0x7790aaacb39e8b92L, // pdc_Latn_US
  -0x3390bcbeb39e8b92L, // pdt_Latn_CA
  -0x4770b6ada78f9a91L, // peo_Xpeo_IR
  -0x5350bbbab39e8b92L, // pfl_Latn_DE
  -0x4b10b3bdaf979188L, // phn_Phnx_LB
  -0x7eb0b6b1bd8d9e98L, // pka_Brah_IN
  -0x46b0b4bab39e8b92L, // pko_Latn_KE
  0x706C504C4C61746EL, // pl_Latn_PL
  -0x3670b6abb39e8b92L, // pms_Latn_IT
  -0x3250b8adb88d9a95L, // pnt_Grek_GR
  -0x4a30b9b2b39e8b92L, // pon_Latn_FM
  -0x7dd0afb4b4979e8eL, // pra_Khar_PK
  -0x71d0b6adbe8d9e9eL, // prd_Arab_IR
  0x7073414641726162L, // ps_Arab_AF
  0x707442524C61746EL, // pt_Latn_BR
  -0x2d70b8beb39e8b92L, // puu_Latn_GA
  0x717550454C61746EL, // qu_Latn_PE
  -0x756fb8abb39e8b92L, // quc_Latn_GT
  -0x656fbabcb39e8b92L, // qug_Latn_EC
  -0x5beeb6b1bb9a899fL, // raj_Deva_IN
  -0x6baeadbab39e8b92L, // rcf_Latn_RE
  -0x5b6eb6bbb39e8b92L, // rej_Latn_ID
  -0x4b2eb6abb39e8b92L, // rgn_Latn_IT
  -0x7eeeb6b1b39e8b92L, // ria_Latn_IN
  -0x6aeeb2beab999199L, // rif_Tfng_MA
  -0x36ceb1afbb9a899fL, // rjs_Deva_NP
  -0x32aebdbbbd9a9199L, // rkt_Beng_BD
  0x726D43484C61746EL, // rm_Latn_CH
  -0x6a6eb9b6b39e8b92L, // rmf_Latn_FI
  -0x466ebcb7b39e8b92L, // rmo_Latn_CH
  -0x326eb6adbe8d9e9eL, // rmt_Arab_IR
  -0x2e6eacbab39e8b92L, // rmu_Latn_SE
  0x726E42494C61746EL, // rn_Latn_BI
  -0x664eb2a5b39e8b92L, // rng_Latn_MZ
  0x726F524F4C61746EL, // ro_Latn_RO
  -0x7a2eb6bbb39e8b92L, // rob_Latn_ID
  -0x6a2eaba5b39e8b92L, // rof_Latn_TZ
  -0x4d8eb9b5b39e8b92L, // rtm_Latn_FJ
  0x727552554379726CL, // ru_Cyrl_RU
  -0x6d6eaabebc868d94L, // rue_Cyrl_UA
  -0x656eacbdb39e8b92L, // rug_Latn_SB
  0x727752574C61746EL, // rw_Latn_RW
  -0x552eaba5b39e8b92L, // rwk_Latn_TZ
  -0x2ceeb5afb49e919fL, // ryu_Kana_JP
  0x7361494E44657661L, // sa_Deva_IN
  -0x6bedb8b7b39e8b92L, // saf_Latn_GH
  -0x63edadaabc868d94L, // sah_Cyrl_RU
  -0x3fedb4bab39e8b92L, // saq_Latn_KE
  -0x37edb6bbb39e8b92L, // sas_Latn_ID
  -0x33edb6b1b39e8b92L, // sat_Latn_IN
  -0x1bedb6b1ac9e8a8eL, // saz_Saur_IN
  -0x43cdaba5b39e8b92L, // sbp_Latn_TZ
  0x736349544C61746EL, // sc_Latn_IT
  -0x57adb6b1bb9a899fL, // sck_Deva_IN
  -0x4badb6abb39e8b92L, // scn_Latn_IT
  -0x47adb8bdb39e8b92L, // sco_Latn_GB
  -0x37adbcbeb39e8b92L, // scs_Latn_CA
  0x7364504B41726162L, // sd_Arab_PK
  0x7364494E44657661L, // sd_Deva_IN
  0x7364494E4B686F6AL, // sd_Khoj_IN
  0x7364494E53696E64L, // sd_Sind_IN
  -0x778db6abb39e8b92L, // sdc_Latn_IT
  -0x638db6adbe8d9e9eL, // sdh_Arab_IR
  0x73654E4F4C61746EL, // se_Latn_NO
  -0x6b6dbcb6b39e8b92L, // sef_Latn_CI
  -0x636db2a5b39e8b92L, // seh_Latn_MZ
  -0x5f6db2a7b39e8b92L, // sei_Latn_MX
  -0x376db2b3b39e8b92L, // ses_Latn_ML
  0x736743464C61746EL, // sg_Latn_CF
  -0x7f2db6bab0989e93L, // sga_Ogam_IE
  -0x372db3abb39e8b92L, // sgs_Latn_LT
  -0x5f0db2beab999199L, // shi_Tfng_MA
  -0x4b0db2b2b286928eL, // shn_Mymr_MM
  0x73694C4B53696E68L, // si_Sinh_LK
  -0x72edbaabb39e8b92L, // sid_Latn_ET
  0x736B534B4C61746EL, // sk_Latn_SK
  -0x3aadafb4be8d9e9eL, // skr_Arab_PK
  0x736C53494C61746EL, // sl_Latn_SI
  -0x5e8dafb3b39e8b92L, // sli_Latn_PL
  -0x1e8db6bbb39e8b92L, // sly_Latn_ID
  0x736D57534C61746EL, // sm_Latn_WS
  -0x7e6dacbab39e8b92L, // sma_Latn_SE
  -0x5a6dacbab39e8b92L, // smj_Latn_SE
  -0x4a6db9b6b39e8b92L, // smn_Latn_FI
  -0x426db6b3ac9e928eL, // smp_Samr_IL
  -0x366db9b6b39e8b92L, // sms_Latn_FI
  0x736E5A574C61746EL, // sn_Latn_ZW
  -0x564db2b3b39e8b92L, // snk_Latn_ML
  0x736F534F4C61746EL, // so_Latn_SO
  -0x2e2dabb7ab979e97L, // sou_Thai_TH
  0x7371414C4C61746EL, // sq_Latn_AL
  0x737252534379726CL, // sr_Cyrl_RS
  0x737252534C61746EL, // sr_Latn_RS
  -0x79cdb6b1ac908d9fL, // srb_Sora_IN
  -0x49cdacadb39e8b92L, // srn_Latn_SR
  -0x39cdacb1b39e8b92L, // srr_Latn_SN
  -0x21cdb6b1bb9a899fL, // srx_Deva_IN
  0x73735A414C61746EL, // ss_Latn_ZA
  -0x1dadbaadb39e8b92L, // ssy_Latn_ER
  0x73745A414C61746EL, // st_Latn_ZA
  -0x3d8dbbbab39e8b92L, // stq_Latn_DE
  0x737549444C61746EL, // su_Latn_ID
  -0x556daba5b39e8b92L, // suk_Latn_TZ
  -0x356db8b1b39e8b92L, // sus_Latn_GN
  0x737653454C61746EL, // sv_Latn_SE
  0x7377545A4C61746EL, // sw_Latn_TZ
  -0x792da6abbe8d9e9eL, // swb_Arab_YT
  -0x752dbcbbb39e8b92L, // swc_Latn_CD
  -0x652dbbbab39e8b92L, // swg_Latn_DE
  -0x292db6b1bb9a899fL, // swv_Deva_IN
  -0x490db6bbb39e8b92L, // sxn_Latn_ID
  -0x50edbdbbbd9a9199L, // syl_Beng_BD
  -0x38edb6aeac868d9dL, // syr_Syrc_IQ
  -0x50cdafb3b39e8b92L, // szl_Latn_PL
  0x7461494E54616D6CL, // ta_Taml_IN
  -0x5becb1afbb9a899fL, // taj_Deva_NP
  -0x27ccafb7b39e8b92L, // tbw_Latn_PH
  -0x1facb6b1b4919b9fL, // tcy_Knda_IN
  -0x738cbcb1ab9e939bL, // tdd_Tale_CN
  -0x678cb1afbb9a899fL, // tdg_Deva_NP
  -0x638cb1afbb9a899fL, // tdh_Deva_NP
  0x7465494E54656C75L, // te_Telu_IN
  -0x4f6cacb3b39e8b92L, // tem_Latn_SL
  -0x476caab8b39e8b92L, // teo_Latn_UG
  -0x336cabb3b39e8b92L, // tet_Latn_TL
  0x7467504B41726162L, // tg_Arab_PK
  0x7467544A4379726CL, // tg_Cyrl_TJ
  0x7468544854686169L, // th_Thai_TH
  -0x530cb1afbb9a899fL, // thl_Deva_NP
  -0x3f0cb1afbb9a899fL, // thq_Deva_NP
  -0x3b0cb1afbb9a899fL, // thr_Deva_NP
  0x7469455445746869L, // ti_Ethi_ET
  -0x66ecbaadba8b9797L, // tig_Ethi_ER
  -0x2aecb1b8b39e8b92L, // tiv_Latn_NG
  0x746B544D4C61746EL, // tk_Latn_TM
  -0x52acabb4b39e8b92L, // tkl_Latn_TK
  -0x3aacbea5b39e8b92L, // tkr_Latn_AZ
  -0x32acb1afbb9a899fL, // tkt_Deva_NP
  0x746C50484C61746EL, // tl_Latn_PH
  -0x1e8cbea5b39e8b92L, // tly_Latn_AZ
  -0x626cb1bab39e8b92L, // tmh_Latn_NE
  0x746E5A414C61746EL, // tn_Latn_ZA
  0x746F544F4C61746EL, // to_Latn_TO
  -0x662cb2a8b39e8b92L, // tog_Latn_MW
  -0x5e0cafb8b39e8b92L, // tpi_Latn_PG
  0x747254524C61746EL, // tr_Latn_TR
  -0x2dccabadb39e8b92L, // tru_Latn_TR
  -0x29ccaba8b39e8b92L, // trv_Latn_TW
  0x74735A414C61746EL, // ts_Latn_ZA
  -0x71acb8adb88d9a95L, // tsd_Grek_GR
  -0x69acb1afbb9a899fL, // tsf_Deva_NP
  -0x65acafb7b39e8b92L, // tsg_Latn_PH
  -0x59acbdabab969d8cL, // tsj_Tibt_BT
  0x747452554379726CL, // tt_Cyrl_RU
  -0x598caab8b39e8b92L, // ttj_Latn_UG
  -0x358cabb7ab979e97L, // tts_Thai_TH
  -0x318cbea5b39e8b92L, // ttt_Latn_AZ
  -0x4d6cb2a8b39e8b92L, // tum_Latn_MW
  -0x514caba9b39e8b92L, // tvl_Latn_TV
  -0x3d2cb1bab39e8b92L, // twq_Latn_NE
  -0x650cbcb1ab9e9199L, // txg_Tang_CN
  0x747950464C61746EL, // ty_Latn_PF
  -0x28ecadaabc868d94L, // tyv_Cyrl_RU
  -0x4cccb2beb39e8b92L, // tzm_Latn_MA
  -0x4f8badaabc868d94L, // udm_Cyrl_RU
  0x7567434E41726162L, // ug_Arab_CN
  0x75674B5A4379726CL, // ug_Cyrl_KZ
  -0x7f2baca6aa989e8eL, // uga_Ugar_SY
  0x756B55414379726CL, // uk_Cyrl_UA
  -0x5e8bb9b2b39e8b92L, // uli_Latn_FM
  -0x7a6bbeb0b39e8b92L, // umb_Latn_AO
  -0x3a4bb6b1bd9a9199L, // unr_Beng_IN
  -0x3a4bb1afbb9a899fL, // unr_Deva_NP
  -0x224bb6b1bd9a9199L, // unx_Beng_IN
  0x7572504B41726162L, // ur_Arab_PK
  0x757A414641726162L, // uz_Arab_AF
  0x757A555A4C61746EL, // uz_Latn_UZ
  -0x5feab3ada99e9697L, // vai_Vaii_LR
  0x76655A414C61746EL, // ve_Latn_ZA
  -0x776ab6abb39e8b92L, // vec_Latn_IT
  -0x436aadaab39e8b92L, // vep_Latn_RU
  0x7669564E4C61746EL, // vi_Latn_VN
  -0x76eaaca7b39e8b92L, // vic_Latn_SX
  -0x368abdbab39e8b92L, // vls_Latn_BE
  -0x6a6abbbab39e8b92L, // vmf_Latn_DE
  -0x266ab2a5b39e8b92L, // vmw_Latn_MZ
  -0x322aadaab39e8b92L, // vot_Latn_RU
  -0x45cababab39e8b92L, // vro_Latn_EE
  -0x496aaba5b39e8b92L, // vun_Latn_TZ
  0x776142454C61746EL, // wa_Latn_BE
  -0x6fe9bcb7b39e8b92L, // wae_Latn_CH
  -0x53e9baabba8b9797L, // wal_Ethi_ET
  -0x3be9afb7b39e8b92L, // war_Latn_PH
  -0x43c9beaab39e8b92L, // wbp_Latn_AU
  -0x3fc9b6b1ab9a938bL, // wbq_Telu_IN
  -0x3bc9b6b1bb9a899fL, // wbr_Deva_IN
  -0x3689a8b9b39e8b92L, // wls_Latn_WF
  -0x5e49b4b2be8d9e9eL, // wni_Arab_KM
  0x776F534E4C61746EL, // wo_Latn_SN
  -0x4d89b6b1bb9a899fL, // wtm_Deva_IN
  -0x2d69bcb1b79e918dL, // wuu_Hans_CN
  -0x2be8bdadb39e8b92L, // xav_Latn_BR
  -0x3ba8abadbc9e8d97L, // xcr_Cari_TR
  0x78685A414C61746EL, // xh_Latn_ZA
  -0x7688abadb3869c97L, // xlc_Lyci_TR
  -0x7288abadb3869b97L, // xld_Lydi_TR
  -0x6a68b8bab89a908eL, // xmf_Geor_GE
  -0x4a68bcb1b29e9197L, // xmn_Mani_CN
  -0x3a68acbbb29a8d9dL, // xmr_Merc_SD
  -0x7e48acbeb19e8d9eL, // xna_Narb_SA
  -0x3a48b6b1bb9a899fL, // xnr_Deva_IN
  -0x6628aab8b39e8b92L, // xog_Latn_UG
  -0x3a08b6adaf8d8b97L, // xpr_Prti_IR
  -0x7da8a6baac9e8d9eL, // xsa_Sarb_YE
  -0x39a8b1afbb9a899fL, // xsr_Deva_NP
  -0x47e7b2a5b39e8b92L, // yao_Latn_MZ
  -0x43e7b9b2b39e8b92L, // yap_Latn_FM
  -0x2be7bcb2b39e8b92L, // yav_Latn_CM
  -0x7bc7bcb2b39e8b92L, // ybb_Latn_CM
  0x796F4E474C61746EL, // yo_Latn_NG
  -0x51c7bdadb39e8b92L, // yrl_Latn_BR
  -0x7d67b2a7b39e8b92L, // yua_Latn_MX
  -0x6d67bcb1b79e918dL, // yue_Hans_CN
  -0x6d67b7b4b79e918cL, // yue_Hant_HK
  0x7A61434E4C61746EL, // za_Latn_CN
  -0x67e6acbbb39e8b92L, // zag_Latn_SD
  -0x5b86b4b2be8d9e9eL, // zdj_Arab_KM
  -0x7f66b1b3b39e8b92L, // zea_Latn_NL
  -0x6326b2beab999199L, // zgh_Tfng_MA
  0x7A685457426F706FL, // zh_Bopo_TW
  0x7A68545748616E62L, // zh_Hanb_TW
  0x7A68434E48616E73L, // zh_Hans_CN
  0x7A68545748616E74L, // zh_Hant_TW
  -0x4e86abb8b39e8b92L, // zlm_Latn_TG
  -0x5e66b2a6b39e8b92L, // zmi_Latn_MY
  0x7A755A414C61746EL, // zu_Latn_ZA
  -0x7cc6abadb39e8b92L) // zza_Latn_TR

internal val ARAB_PARENTS = mapOf(
  0x6172445A to 0x61729420, // ar-DZ -> ar-015
  0x61724548 to 0x61729420, // ar-EH -> ar-015
  0x61724C59 to 0x61729420, // ar-LY -> ar-015
  0x61724D41 to 0x61729420, // ar-MA -> ar-015
  0x6172544E to 0x61729420) // ar-TN -> ar-015

internal val HANT_PARENTS = mapOf(
  0x7A684D4F to 0x7A68484B) // zh-Hant-MO -> zh-Hant-HK

internal val LATN_PARENTS = mapOf(
  0x656e80a1 to 0x656e8400, // en-150 -> en-001
  0x656e4147 to 0x656e8400, // en-AG -> en-001
  0x656e4149 to 0x656e8400, // en-AI -> en-001
  0x656e4154 to 0x656e80a1, // en-AT -> en-150
  0x656e4155 to 0x656e8400, // en-AU -> en-001
  0x656e4242 to 0x656e8400, // en-BB -> en-001
  0x656e4245 to 0x656e8400, // en-BE -> en-001
  0x656e424d to 0x656e8400, // en-BM -> en-001
  0x656e4253 to 0x656e8400, // en-BS -> en-001
  0x656e4257 to 0x656e8400, // en-BW -> en-001
  0x656e425a to 0x656e8400, // en-BZ -> en-001
  0x656e4341 to 0x656e8400, // en-CA -> en-001
  0x656e4343 to 0x656e8400, // en-CC -> en-001
  0x656e4348 to 0x656e80a1, // en-CH -> en-150
  0x656e434b to 0x656e8400, // en-CK -> en-001
  0x656e434d to 0x656e8400, // en-CM -> en-001
  0x656e4358 to 0x656e8400, // en-CX -> en-001
  0x656e4359 to 0x656e8400, // en-CY -> en-001
  0x656e4445 to 0x656e80a1, // en-DE -> en-150
  0x656e4447 to 0x656e8400, // en-DG -> en-001
  0x656e444b to 0x656e80a1, // en-DK -> en-150
  0x656e444d to 0x656e8400, // en-DM -> en-001
  0x656e4552 to 0x656e8400, // en-ER -> en-001
  0x656e4649 to 0x656e80a1, // en-FI -> en-150
  0x656e464a to 0x656e8400, // en-FJ -> en-001
  0x656e464b to 0x656e8400, // en-FK -> en-001
  0x656e464d to 0x656e8400, // en-FM -> en-001
  0x656e4742 to 0x656e8400, // en-GB -> en-001
  0x656e4744 to 0x656e8400, // en-GD -> en-001
  0x656e4747 to 0x656e8400, // en-GG -> en-001
  0x656e4748 to 0x656e8400, // en-GH -> en-001
  0x656e4749 to 0x656e8400, // en-GI -> en-001
  0x656e474d to 0x656e8400, // en-GM -> en-001
  0x656e4759 to 0x656e8400, // en-GY -> en-001
  0x656e484b to 0x656e8400, // en-HK -> en-001
  0x656e4945 to 0x656e8400, // en-IE -> en-001
  0x656e494c to 0x656e8400, // en-IL -> en-001
  0x656e494d to 0x656e8400, // en-IM -> en-001
  0x656e494e to 0x656e8400, // en-IN -> en-001
  0x656e494f to 0x656e8400, // en-IO -> en-001
  0x656e4a45 to 0x656e8400, // en-JE -> en-001
  0x656e4a4d to 0x656e8400, // en-JM -> en-001
  0x656e4b45 to 0x656e8400, // en-KE -> en-001
  0x656e4b49 to 0x656e8400, // en-KI -> en-001
  0x656e4b4e to 0x656e8400, // en-KN -> en-001
  0x656e4b59 to 0x656e8400, // en-KY -> en-001
  0x656e4c43 to 0x656e8400, // en-LC -> en-001
  0x656e4c52 to 0x656e8400, // en-LR -> en-001
  0x656e4c53 to 0x656e8400, // en-LS -> en-001
  0x656e4d47 to 0x656e8400, // en-MG -> en-001
  0x656e4d4f to 0x656e8400, // en-MO -> en-001
  0x656e4d53 to 0x656e8400, // en-MS -> en-001
  0x656e4d54 to 0x656e8400, // en-MT -> en-001
  0x656e4d55 to 0x656e8400, // en-MU -> en-001
  0x656e4d57 to 0x656e8400, // en-MW -> en-001
  0x656e4d59 to 0x656e8400, // en-MY -> en-001
  0x656e4e41 to 0x656e8400, // en-NA -> en-001
  0x656e4e46 to 0x656e8400, // en-NF -> en-001
  0x656e4e47 to 0x656e8400, // en-NG -> en-001
  0x656e4e4c to 0x656e80a1, // en-NL -> en-150
  0x656e4e52 to 0x656e8400, // en-NR -> en-001
  0x656e4e55 to 0x656e8400, // en-NU -> en-001
  0x656e4e5a to 0x656e8400, // en-NZ -> en-001
  0x656e5047 to 0x656e8400, // en-PG -> en-001
  0x656e5048 to 0x656e8400, // en-PH -> en-001
  0x656e504b to 0x656e8400, // en-PK -> en-001
  0x656e504e to 0x656e8400, // en-PN -> en-001
  0x656e5057 to 0x656e8400, // en-PW -> en-001
  0x656e5257 to 0x656e8400, // en-RW -> en-001
  0x656e5342 to 0x656e8400, // en-SB -> en-001
  0x656e5343 to 0x656e8400, // en-SC -> en-001
  0x656e5344 to 0x656e8400, // en-SD -> en-001
  0x656e5345 to 0x656e80a1, // en-SE -> en-150
  0x656e5347 to 0x656e8400, // en-SG -> en-001
  0x656e5348 to 0x656e8400, // en-SH -> en-001
  0x656e5349 to 0x656e80a1, // en-SI -> en-150
  0x656e534c to 0x656e8400, // en-SL -> en-001
  0x656e5353 to 0x656e8400, // en-SS -> en-001
  0x656e5358 to 0x656e8400, // en-SX -> en-001
  0x656e535a to 0x656e8400, // en-SZ -> en-001
  0x656e5443 to 0x656e8400, // en-TC -> en-001
  0x656e544b to 0x656e8400, // en-TK -> en-001
  0x656e544f to 0x656e8400, // en-TO -> en-001
  0x656e5454 to 0x656e8400, // en-TT -> en-001
  0x656e5456 to 0x656e8400, // en-TV -> en-001
  0x656e545a to 0x656e8400, // en-TZ -> en-001
  0x656e5547 to 0x656e8400, // en-UG -> en-001
  0x656e5643 to 0x656e8400, // en-VC -> en-001
  0x656e5647 to 0x656e8400, // en-VG -> en-001
  0x656e5655 to 0x656e8400, // en-VU -> en-001
  0x656e5753 to 0x656e8400, // en-WS -> en-001
  0x656e5a41 to 0x656e8400, // en-ZA -> en-001
  0x656e5a4d to 0x656e8400, // en-ZM -> en-001
  0x656e5a57 to 0x656e8400, // en-ZW -> en-001
  0x65734152 to 0x6573a424, // es-AR -> es-419
  0x6573424f to 0x6573a424, // es-BO -> es-419
  0x65734252 to 0x6573a424, // es-BR -> es-419
  0x6573434c to 0x6573a424, // es-CL -> es-419
  0x6573434f to 0x6573a424, // es-CO -> es-419
  0x65734352 to 0x6573a424, // es-CR -> es-419
  0x65734355 to 0x6573a424, // es-CU -> es-419
  0x6573444f to 0x6573a424, // es-DO -> es-419
  0x65734543 to 0x6573a424, // es-EC -> es-419
  0x65734754 to 0x6573a424, // es-GT -> es-419
  0x6573484e to 0x6573a424, // es-HN -> es-419
  0x65734d58 to 0x6573a424, // es-MX -> es-419
  0x65734e49 to 0x6573a424, // es-NI -> es-419
  0x65735041 to 0x6573a424, // es-PA -> es-419
  0x65735045 to 0x6573a424, // es-PE -> es-419
  0x65735052 to 0x6573a424, // es-PR -> es-419
  0x65735059 to 0x6573a424, // es-PY -> es-419
  0x65735356 to 0x6573a424, // es-SV -> es-419
  0x65735553 to 0x6573a424, // es-US -> es-419
  0x65735559 to 0x6573a424, // es-UY -> es-419
  0x65735645 to 0x6573a424, // es-VE -> es-419
  0x7074414f to 0x70745054, // pt-AO -> pt-PT
  0x70744348 to 0x70745054, // pt-CH -> pt-PT
  0x70744356 to 0x70745054, // pt-CV -> pt-PT
  0x70744751 to 0x70745054, // pt-GQ -> pt-PT
  0x70744757 to 0x70745054, // pt-GW -> pt-PT
  0x70744c55 to 0x70745054, // pt-LU -> pt-PT
  0x70744d4f to 0x70745054, // pt-MO -> pt-PT
  0x70744d5a to 0x70745054, // pt-MZ -> pt-PT
  0x70745354 to 0x70745054, // pt-ST -> pt-PT
  0x7074544c to 0x70745054) // pt-TL -> pt-PT

internal val SCRIPT_PARENTS = mapOf(
  "Arab" to ARAB_PARENTS,
  "Hant" to HANT_PARENTS,
  "Latn" to LATN_PARENTS)

internal const val MAX_SCRIPT_PARENT_DEPTH = 3
