// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:file_selector_platform_interface/file_selector_platform_interface.dart';
import 'package:flutter/foundation.dart' show visibleForTesting;
import 'package:flutter/services.dart';

import 'src/messages.g.dart';

const MethodChannel _channel =
    MethodChannel('plugins.flutter.io/file_selector_ios');

/// An implementation of [FileSelectorPlatform] for iOS.
class FileSelectorIOS extends FileSelectorPlatform {
  /// The MethodChannel that is being used by this implementation of the plugin.
  @visibleForTesting
  MethodChannel get channel => _channel;

  final FileSelectorApi _hostApi = FileSelectorApi();

  /// Registers the iOS implementation.
  static void registerWith() {
    FileSelectorPlatform.instance = FileSelectorIOS();
  }

  @override
  Future<XFile?> openFile({
    List<XTypeGroup>? acceptedTypeGroups,
    String? initialDirectory,
    String? confirmButtonText,
  }) async {
    final List<String>? path = (await _hostApi.openFile(FileSelectorConfig(
            utis: _allowedUtiListFromTypeGroups(acceptedTypeGroups),
            allowMultiSelection: false)))
        ?.cast<String>();
    return path == null ? null : XFile(path.first);
  }

  @override
  Future<List<XFile>> openFiles({
    List<XTypeGroup>? acceptedTypeGroups,
    String? initialDirectory,
    String? confirmButtonText,
  }) async {
    final List<String>? pathList = (await _hostApi.openFile(FileSelectorConfig(
            utis: _allowedUtiListFromTypeGroups(acceptedTypeGroups),
            allowMultiSelection: true)))
        ?.cast<String>();
    return pathList?.map((String path) => XFile(path)).toList() ?? <XFile>[];
  }

  // Converts the type group list into a list of all allowed UTIs, since
  // iOS doesn't support filter groups.
  List<String>? _allowedUtiListFromTypeGroups(List<XTypeGroup>? typeGroups) {
    if (typeGroups == null || typeGroups.isEmpty) {
      return null;
    }
    final List<String> allowedUTIs = <String>[];
    for (final XTypeGroup typeGroup in typeGroups) {
      // If any group allows everything, no filtering should be done.
      if (typeGroup.macUTIs?.isEmpty ?? true) {
        return null;
      }
      allowedUTIs.addAll(typeGroup.macUTIs!);
    }
    return allowedUTIs;
  }
}
