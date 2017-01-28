# Interactable
> react-native-interactable

This is an experimental implementation of a declarative API for handling fluid user interactions with views at 60 FPS in React Native. Here are some example use-cases for views that users can interact with:

* **Swipeable card** (a la Google Now) springing into place unless swiped away with enough force
* **Drawer** snapping between closed and open with buttons appearing gradually as it's being dragged
* **Collapsible header** that snaps to a smaller size as the content below is being scrolled
* **Chat heads** (a la Facebook Messenger) that can be dragged around but snap to corners of the screen

All of these use-cases have views that continuously interact with the user's gestures. These interactions are normally physical in nature, having properties like springiness, friction, elasticity and damping. In order to feel natural on a touch device they need to run at 60 FPS.

### Why is this challenging?

The async nature of the React Native bridge incurs an inherent performance penalty. This traditionally prevents JavaScript code from running at high framerates. One of the most noticeable challenges is animations in JavaScript, which aren't guaranteed to run at 60 FPS.

Modern animation libraries for React Native, like [Animated](https://facebook.github.io/react-native/docs/animated.html), tackle this challenge with a declarative approach. In order to minimize passes over the bridge, animations are only declared in JavaScript but executed by a native driver on the other side - in 60 FPS.

Fluid user interactions take this a step further than animations. Interactions require UI to continuously react to the user's gestures. This library is designed to support complex physical interactions with ease, using a full-fledged physics engine to drive the interaction on the native side.

## Installation

* Install the library from npm

```
npm install react-native-interactable --save
```

* [Connect](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking) the native library to your Xcode project

```
ios/Interactable.xcodeproj
```

## Example

To see the library in action, run the [example](example) project - it has 4 use-cases implemented: *swipeable card*, *drawer*, *collapsible header* and *chat heads*. It's recommended to experience it on a [real device](http://facebook.github.io/react-native/docs/running-on-device.html) and not on a simulator. To run the example, first clone the repo and go to the `example/` folder and run:

```
npm install
react-native run-ios
```

## Usage

The core of this library is the `Interactable.View` component, used to wrap views you want to interact with:

```jsx
import Interactable from 'react-native-interactable';

<Interactable.View
  horizontalOnly={true}
  snapTo={[{x: 0}, {x: -200}]}
  onSnap={this.onDrawerSnap}>
  
  // the view that you wrap here will now support interactions

</Interactable.View>
```

### `Interactable.View` Props

#### `snapTo` (array of points)

```jsx
snapTo={[{x: 0}, {x: -200}]}
```

A list of points the view will snap to after being dragged by the user. To implement a a drawer for example, provide 2 points - one for the open state and one for the closed state. Points are relative to the view's center (after initial layout). Every point in the array is an object with the following properties:

* `x` - The horizontal position (relative to the center). Optional if a single dimension is needed.
* `y` - The vertical position (relative to the center). Optional if a single dimension is needed.
* `damping` - Amount of damping on the spring connected to this point. Default is `0.5`.
* `id` - An optional string name for the point to identify it in the `onSnap` event.

Examples for [chat heads](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/ChatHeads.js#L10), [drawer](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/IconDrawer.js#L62) and [swipeable card](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/SwipeableCard.js#L25).

#### `horizontalOnly` (boolean)

```jsx
horizontalOnly={true}
```

Optional, whether the view should be locked to horizontal movement only.

#### `verticalOnly` (boolean)

```jsx
verticalOnly={true}
```

Optional, whether the view should be locked to vertical movement only.

#### `onSnap` (function)

```jsx
onSnap={this.onDrawerSnap}
```

Optional, a function called whenever the view snaps to a `snapTo` point (after being dragged). Example for [drawer](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/IconDrawer.js#L63). When the function is called, an event object is passed as argument, containing the following properties:

* `index` - The zero-based index of the point in the `snapTo` array.
* `id` - The string `id` of the point in the `snapTo` array (assuming it was provided).

#### `animatedValueX` (Animated.Value)

```jsx
animatedValueX={this._deltaX}
```

[`Animated.Value`](https://facebook.github.io/react-native/docs/animated.html#animatedvalue) that will contain the delta from the center as the view moves (x axis). See [this](README.md#animating-other-views-according-to-interactableview-position) for more details on how to animate other views according to the movement of this view.

#### `animatedValueY` (Animated.Value)

```jsx
animatedValueY={this._deltaY}
```

[`Animated.Value`](https://facebook.github.io/react-native/docs/animated.html#animatedvalue) that will contain the delta from the center as the view moves (y axis). See [this](README.md#animating-other-views-according-to-interactableview-position) for more details on how to animate other views according to the movement of this view.

#### `resistance` (number)

```jsx
resistance={3000}
```

Optional, a physical attribute that will cause the view to resist movement (move slower). A value of at least `1000` is required for a significant change.

#### `initialPosition` (point)

```jsx
initialPosition={{x: -140, y: -280}}
```

Optional, used to initialize the view's position to a position different than it's original center. This is mostly useful when you want to provide `snapTo` points relative to the center calculated by React Native layout, but want the view itself to start from a different position. See [chat heads](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/ChatHeads.js#L22) example.

#### `allowRotation` (boolean)

```jsx
allowRotation={true}
```

Optional, whether the view can rotate on its center axis when tossed around.

## Animating other views according to `Interactable.View` position

This library is integrated with the [Animated](https://facebook.github.io/react-native/docs/animated.html) library in order to support performant animations of other views according to the movement of the `Interactable.View`.

Consider the following use-cases:

* Buttons that appear using a fade & scale animation under a drawer as it's being dragged ([example](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/IconDrawer.js#L44))
* Image in a collapsible header that scales as it's snapped between states ([example](https://github.com/wix/react-native-interactable/blob/b72eff0649b48dd50548593e5ecfe4c42b026a02/example/src/CollapsingHeader.js#L15))

In these use-cases, we have views different from the one the user is interacting with, that animate according to the interactive view's position. Since [Animated](https://facebook.github.io/react-native/docs/animated.html) library uses [`Animated.Value`](https://facebook.github.io/react-native/docs/animated.html#animatedvalue) to animate view properties, we support setting the value of an `Animated.Value` instance according to position of the interactable view. The `Animated.Value` will contain the delta between the `Interactable.View` original center and new center. This can be done separately on the X axis and Y axis.

After setting this up, use [Animated](https://facebook.github.io/react-native/docs/animated.html) to declaratively define [interpolations](https://facebook.github.io/react-native/docs/animated.html#interpolate) of the `Animated.Value` to various animatable view properties like opacity, scale, rotation, translateX and translateY:

```jsx
this._deltaY = new Animated.Value(0);

<Animated.View style={{
  transform: [{
    scale: this._deltaY.interpolate({
      inputRange: [-150, -150, 0, 0],
      outputRange: [0.3, 0.3, 1, 1]
    })
  }]
}}> 
  ...
</Animated.View>

<Interactable.View
  verticalOnly={true}
  snapTo={[{y: 0}, {y: -150}]}
  animatedValueY={this._deltaY}
>
  ...
</Interactable.View>
```

## Implementation Details

The iOS implementation relies on [UIKit Dynamics](https://developer.apple.com/reference/uikit/uidynamicanimator) - a powerful native animation engine for physical interactions. A physics engine is required in order to make the interaction life-like. Consider the action of tossing a view connected via a spring to a snap point. A simple native [spring animation](https://developer.apple.com/reference/uikit/uiview/1622594-animatewithduration) will not be enough to take the initial velocity vector into account.

## Roadmap

* Enable `useNativeDriver` in `Animated.event` to make `Animated.Value` updates to be fully native
* Add support for automatic installation with `react-native link`
* Android support (probably using [Box2D](http://box2d.org/) to drive the physics)

## Contributing

If you are interested in the project, have feedback or want to contribute don't hesitate to contact me. I'm particularly interested in ideas on how to expand the declarative API for more use-cases and suggestions on how to improve performance. PRs are always welcome.

## License

MIT
