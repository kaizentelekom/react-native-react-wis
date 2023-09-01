#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import "RCTEventEmitter.h"

@interface ReactWis : RCTEventEmitter <RCTBridgeModule>
@property (nonatomic) BOOL didInvokeCalled;

@end
