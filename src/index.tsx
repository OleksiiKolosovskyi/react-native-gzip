import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-gzip' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Gzip = NativeModules.Gzip
  ? NativeModules.Gzip
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * Decompress data encoded as base64 string
 */
export function inflate(base64: string): Promise<string> {
  return Gzip.inflate(base64);
}

/**
 * Compress data to base64 encoded string
 */
export function deflate(data: string): Promise<string> {
  return Gzip.deflate(data);
}

/**
 * Decompress data encoded as base64 string to base64 string
 */
export function inflateBase64(base64: string): Promise<string> {
  return Gzip.inflateBase64(base64);
}

/**
 * Compress data to base64 encoded string from base64 string
 */
export function deflateBase64(base64: string): Promise<string> {
  return Gzip.deflateBase64(base64);
}
