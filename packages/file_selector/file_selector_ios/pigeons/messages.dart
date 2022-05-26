// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  dartTestOut: 'test/test_api.dart',
  objcHeaderOut: 'ios/Classes/messages.g.h',
  objcSourceOut: 'ios/Classes/messages.g.m',
  objcOptions: ObjcOptions(
    prefix: 'FLT',
  ),
  copyrightHeader: 'pigeons/copyright.txt',
))
class FileSelectorConfig {
  FileSelectorConfig(this.utis, this.allowMultiSelection);
  List<String?>? utis;
  bool? allowMultiSelection;
}

@HostApi()
abstract class FileSelectorApi {
  @async
  @ObjCSelector('openFileSelectorWithConfig:')
  List<String>? openFile(FileSelectorConfig config);
}
