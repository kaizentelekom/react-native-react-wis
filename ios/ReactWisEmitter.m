//
//  ReactWisEmitter.m
//  ReactWis
//
//  Created by Fatma Selin Hangişi on 17.08.2021.
//  Copyright © 2021 Facebook. All rights reserved.
//
#import <ReactWisEmitter.h>
@import iOS_wis;

@implementation ReactWisEmitter

- (NSArray<NSString *> *)supportedEvents {
  return @[@"pushClicked"];
}
@end
