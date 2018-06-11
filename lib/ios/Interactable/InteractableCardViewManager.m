//
//  InteractableCardViewManager.m
//  Interactable
//
//  Created by Farid Dahiri on 5/17/18.
//  Copyright © 2018 Wix. All rights reserved.
//

#import "InteractableCardViewManager.h"
#import "InteractableCardView.h"
#import "InteractablePoint.h"
#import "InteractableArea.h"
#import "InteractableSpring.h"
#import "RCTConvert+Interactable.h"
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

@implementation InteractableCardViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return [[InteractableCardView alloc] initWithBridge:self.bridge];
}

RCT_EXPORT_VIEW_PROPERTY(verticalOnly, BOOL)
RCT_EXPORT_VIEW_PROPERTY(horizontalOnly, BOOL)
RCT_EXPORT_VIEW_PROPERTY(dragEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(snapPoints, NSArray<InteractablePoint *>)
RCT_EXPORT_VIEW_PROPERTY(springPoints, NSArray<InteractablePoint *>)
RCT_EXPORT_VIEW_PROPERTY(gravityPoints, NSArray<InteractablePoint *>)
RCT_EXPORT_VIEW_PROPERTY(frictionAreas, NSArray<InteractablePoint *>)
RCT_EXPORT_VIEW_PROPERTY(alertAreas, NSArray<InteractablePoint *>)
RCT_EXPORT_VIEW_PROPERTY(boundaries, InteractableArea)
RCT_EXPORT_VIEW_PROPERTY(dragWithSpring, InteractableSpring)
RCT_EXPORT_VIEW_PROPERTY(dragToss, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(onSnap, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStop, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAlert, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onDrag, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(initialPosition, CGPoint)
RCT_EXPORT_VIEW_PROPERTY(onAnimatedEvent, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(reportOnAnimatedEvents, BOOL)

RCT_EXPORT_METHOD(setVelocity:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
     {
         UIView *view = viewRegistry[reactTag];
         if ([view isKindOfClass:[InteractableCardView class]])
         {
             [(InteractableCardView*)view setVelocity:params];
         }
         else
         {
             RCTLogError(@"tried to setVelocity: on non-InteractableCardView view %@ "
                         "with tag #%@", view, reactTag);
         }
     }];
}

RCT_EXPORT_METHOD(bringToFront:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    
}

RCT_EXPORT_METHOD(attachScrollViewIfNeeded:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
     {
         UIView *view = viewRegistry[reactTag];
         if ([view isKindOfClass:[InteractableCardView class]])
         {
             [(InteractableCardView*)view attachScrollViewIfNeeded:params];
         }
         else
         {
             RCTLogError(@"tried to attachScrollViewIfNeeded: on non-InteractableCardView view %@ "
                         "with tag #%@", view, reactTag);
         }
     }];
}

RCT_EXPORT_METHOD(scrollHostedViewToTop:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
     {
         UIView *view = viewRegistry[reactTag];
         if ([view isKindOfClass:[InteractableCardView class]])
         {
             [(InteractableCardView*)view scrollHostedViewToTop:params];
         }
         else
         {
             RCTLogError(@"tried to scrollHostedViewToTop: on non-InteractableCardView view %@ "
                         "with tag #%@", view, reactTag);
         }
     }];
}

RCT_EXPORT_METHOD(snapTo:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
     {
         UIView *view = viewRegistry[reactTag];
         if ([view isKindOfClass:[InteractableCardView class]])
         {
             [(InteractableCardView*)view snapTo:params];
         }
         else
         {
             RCTLogError(@"tried to snapTo: on non-InteractableCardView view %@ "
                         "with tag #%@", view, reactTag);
         }
     }];
}

RCT_EXPORT_METHOD(changePosition:(nonnull NSNumber *)reactTag
                  params:(NSDictionary*)params)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry)
     {
         UIView *view = viewRegistry[reactTag];
         if ([view isKindOfClass:[InteractableCardView class]])
         {
             [(InteractableCardView*)view changePosition:params];
         }
         else
         {
             RCTLogError(@"tried to changePosition: on non-InteractableCardView view %@ "
                         "with tag #%@", view, reactTag);
         }
     }];
}

@end
