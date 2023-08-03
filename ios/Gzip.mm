#import "Gzip.h"
#import "Base64/MF_Base64Additions.h"
#import "GZIP/NSData+GZIP.h"

@implementation Gzip
RCT_EXPORT_MODULE()

// Decompresses base64 encoded string
RCT_REMAP_METHOD(inflate,
                 base64: (NSString *)base64
                 withResolver: (RCTPromiseResolveBlock)resolve
                 withRejecter: (RCTPromiseRejectBlock)reject)
{
  NSData * _data = [NSData dataWithBase64String: base64];
  resolve([[NSString alloc] initWithData: [_data gunzippedData] encoding: NSUTF8StringEncoding]);
}

// Compresses string to base64 encoded string
RCT_REMAP_METHOD(deflate,
                 data: (NSString *)data
                 withResolver: (RCTPromiseResolveBlock)resolve
                 withRejecter: (RCTPromiseRejectBlock)reject)
{
  NSData * _data = [data dataUsingEncoding: NSUTF8StringEncoding];
  resolve([[_data gzippedData] base64String]);
}

// Decompresses base64 encoded base64 string
RCT_REMAP_METHOD(inflateBase64,
                 base64: (NSString *)base64
                 withResolver: (RCTPromiseResolveBlock)resolve
                 withRejecter: (RCTPromiseRejectBlock)reject)
{
  NSData * _data = [NSData dataWithBase64String: base64];
  resolve([[_data gunzippedData] base64String]);
}

// Compresses base64 string to base64 encoded string
RCT_REMAP_METHOD(deflateBase64,
                 base64: (NSString *)base64
                 withResolver: (RCTPromiseResolveBlock)resolve
                 withRejecter: (RCTPromiseRejectBlock)reject)
{
  NSData * _data = [NSData dataWithBase64String: base64];
  resolve([[NSString alloc] initWithData: [_data gzippedData] encoding: NSUTF8StringEncoding]);
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeGzipSpecJSI>(params);
}
#endif

@end
