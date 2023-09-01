#import "ReactWis.h"

@import iOS_wis;

@implementation ReactWis
@synthesize didInvokeCalled;

Webinstats *webinstats = NULL;


RCT_EXPORT_MODULE();

RCT_REMAP_METHOD(execute, map:(NSDictionary*) map executeWithResolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){

    @try {

        NSString *s = @"";
        NSString *_cburl = @"";
        s = [map valueForKey:@"s"];
        _cburl = [map valueForKey:@"_cburl"];
        if(s == @""){
            return;
        }
        if (_cburl == @"") {
            _cburl = @"//wis.webinstats.com";
        }
        [map setValue:@"React-Native" forKey:@"_wis_plt"];
        [map setValue:@"1" forKey:@"_enable_push"];
        //resolve([[[HelloWorld alloc]init]testReactWithMap:map]);
     //   [[[Webinstats alloc]init:_cburl :s :@"0" ]sendPageviewWithLocalmap:map];
        //resolve([[[Webinstats alloc]init:@"//wis.webinstats.com" :s :@"0" ]testReactWithMap:map]);
        __weak typeof(self) weakSelf = self;

        dispatch_async(dispatch_get_main_queue(), ^{
            UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
            UIViewController *viewConnection = [[UIViewController alloc]init];
            [viewConnection.view setFrame:[UIScreen mainScreen].bounds];
            [rootViewController addChildViewController:viewConnection];
            if(webinstats == NULL){
                webinstats =  [[Webinstats alloc] init:_cburl :s :@"0"];
            }
            [webinstats executeWithView:viewConnection localmap:map];
        });

    }
    @catch (NSException *exception) {
        reject(@"090033", @"Hata", exception);
    }

}

- (NSArray<NSString *> *)supportedEvents {
  return @[@"pushClicked"];
}

RCT_EXPORT_METHOD(setPushClickCallback:(NSString*)companyId callback:(RCTResponseSenderBlock)callback)
{
    @try{
        didInvokeCalled = NO;
        NSLog(@"webinstats_log : setpushclickcallback called ");
        if(webinstats == NULL){
            webinstats =  [[Webinstats alloc] init:@"//wis.webinstats.com/" :companyId :@"0"];
        }
        [webinstats pushClickCallbackWithCallback:^(NSDictionary* dictionary){
            NSLog(@"webinstats_log : native callback result called dict size : %lu",(unsigned long)dictionary.count);
            @try{
                if(didInvokeCalled == NO){
                    NSLog(@"webinstats_log : first invoke ");
                    double delayInSeconds = 1.0;
                    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
                    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
                        NSLog(@"webinstats_log : timer run");
                        callback(@[dictionary]);
                       // [self sendEventWithName:@"pushClicked" body:dictionary];
                    });
                    didInvokeCalled = YES;
                }else{
                    NSLog(@"webinstats_log : another times invoke");
                    [self sendEventWithName:@"pushClicked" body:dictionary];
                }
            }@catch(NSException *excep){
                NSLog(@"webinstats_log : invoke error %@",excep.reason);
                //[self sendEventWithName:@"pushClicked" body:dictionary];
            }
            //[self sendEventWithName:@"pushClicked" body:@[dictionary]];
            NSLog(@"webinstats_log : spc React Callback Triggered");
        }];
    }@catch(NSException *exception){
        NSLog(@"webinstats_log : invoke error %@",exception.reason);
    }
}

RCT_EXPORT_METHOD(addItem:(NSString *)companyId
                  domain:(NSString *)domain
                  productId:(NSString *)productId
                  quantity:(NSString *)quantity
                  price:(NSString *)price
                  category:(NSString *)category
                  title:(NSString *)title){
    if(companyId == @""){
        return;
    }
    if (domain == @"") {
        domain = @"//wis.webinstats.com";
    }
    if(webinstats == NULL){
        webinstats =  [[Webinstats alloc] init:domain :companyId :@"0"];
    }
    [webinstats addItemWithProductId:productId quantity:quantity price:price category:category title:title];
 
}
RCT_EXPORT_METHOD(saveTestParameters:(NSString *)companyId
                  url:(NSString *)url){
    if(companyId == @""){
        return;
    }
    NSString *domain = @"";
    if (domain == @"") {
        domain = @"//wis.webinstats.com";
    }
    if(webinstats == NULL){
        webinstats =  [[Webinstats alloc] init:domain :companyId :@"0"];
    }
    NSURL *baseurl = [[NSURL alloc] initWithString:url];
    [webinstats saveTestParametersWithUrl:baseurl];

}

@end
