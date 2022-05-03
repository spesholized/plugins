import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:file_selector_android/file_selector_android.dart';

void main() {
  const MethodChannel channel = MethodChannel('file_selector_android');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FileSelectorAndroid.platformVersion, '42');
  });
}
