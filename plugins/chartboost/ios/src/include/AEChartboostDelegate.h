#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#include <Chartboost/Chartboost.h>
#include "ChartboostCallback.h"

@interface AEChartboostDelegate : NSObject<ChartboostDelegate>

/** */
@property (nonatomic) ae::chartboost::ChartboostCallback *chartboostCallback;

@end