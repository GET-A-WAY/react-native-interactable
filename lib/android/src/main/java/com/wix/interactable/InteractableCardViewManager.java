package com.wix.interactable;

import android.support.annotation.Nullable;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.wix.interactable.RNConvert.RNConvert;

import java.util.Map;

public class InteractableCardViewManager extends ViewGroupManager<InteractableCardView> {

    public static final String REACT_CLASS = "InteractableCardView";
    public static final int COMMAND_SET_VELOCITY = 1;
    public static final int COMMAND_SNAP_TO = 2;
    public static final int COMMAND_CHANGE_POSITION = 3;
    public static final int COMMAND_BRING_TO_FRONT = 4;


    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected InteractableCardView createViewInstance(ThemedReactContext reactContext) {
        return new InteractableCardView(reactContext);
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        return MapBuilder.of(
                "setVelocity", COMMAND_SET_VELOCITY,
                "snapTo", COMMAND_SNAP_TO,
                "changePosition", COMMAND_CHANGE_POSITION,
                "bringToFront", COMMAND_BRING_TO_FRONT
        );
    }

    @Override
    public void receiveCommand(
            InteractableCardView view,
            int commandType,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case COMMAND_SET_VELOCITY: {
                view.setVelocity(RNConvert.pointF(args.getMap(0)));
                return;
            }
            case COMMAND_SNAP_TO: {
                String snapPoint = args.getMap(0).getString("id");
                view.snapTo(snapPoint);
                return;
            }
            case COMMAND_CHANGE_POSITION: {
                view.changePosition(RNConvert.pointF(args.getMap(0)));
                return;
            }
            case COMMAND_BRING_TO_FRONT: {
                view.bringToFront();
                return;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }

    @ReactProp(name = "verticalOnly")
    public void setVerticalOnly(InteractableCardView view, @Nullable boolean verticalOnly) {
        view.setVerticalOnly(verticalOnly);
    }

    @ReactProp(name = "startOnFront")
    public void setStartOnFront(InteractableCardView view, @Nullable boolean startOnFront) {
        view.bringToFront();
    }

    @ReactProp(name = "horizontalOnly")
    public void setHorizontalOnly(InteractableCardView view, @Nullable boolean horizontalOnly) {
        view.setHorizontalOnly(horizontalOnly);
    }

    @ReactProp(name = "dragEnabled")
    public void setDragEnabled(InteractableCardView view, @Nullable boolean dragEnabled) {
        view.setDragEnabled(dragEnabled);
    }

    @ReactProp(name = "snapPoints")
    public void setSnapTo(InteractableCardView view, @Nullable ReadableArray snapPoints) {
        view.setSnapPoints(RNConvert.interactablePoints(snapPoints));
    }

    @ReactProp(name = "springPoints")
    public void setSprings(InteractableCardView view, @Nullable ReadableArray springs) {
        view.setSpringsPoints(RNConvert.interactablePoints(springs));
    }

    @ReactProp(name = "gravityPoints")
    public void setGravity(InteractableCardView view, @Nullable ReadableArray gravityPoints) {
        view.setGravityPoints(RNConvert.interactablePoints(gravityPoints));
    }

    @ReactProp(name = "frictionAreas")
    public void setFriction(InteractableCardView view, @Nullable ReadableArray frictionAreas) {
        view.setFrictionAreas(RNConvert.interactablePoints(frictionAreas));
    }

    @ReactProp(name = "alertAreas")
    public void setAlertAreas(InteractableCardView view, @Nullable ReadableArray alertAreas) {
        view.setAlertAreas(RNConvert.interactablePoints(alertAreas));
    }

    @ReactProp(name = "dragWithSprings")
    public void setDrag(InteractableCardView view, @Nullable ReadableMap dragWithSprings) {
        view.setDragWithSprings(RNConvert.interactableDrag(dragWithSprings));
    }

    @ReactProp(name = "dragToss")
    public void setDragToss(InteractableCardView view, @Nullable float dragToss) {
        view.setDragToss(dragToss);
    }

    @ReactProp(name = "reportOnAnimatedEvents")
    public void setReportOnAnimatedEvents(InteractableCardView view, @Nullable boolean reportOnAnimatedEvents) {
        view.setReportOnAnimatedEvents(reportOnAnimatedEvents);
    }

    @ReactProp(name = "boundaries")
    public void setBoundaries(InteractableCardView view, @Nullable ReadableMap boundaries) {
        view.setBoundaries(RNConvert.interactableLimit(boundaries));
    }

    @ReactProp(name = "initialPosition")
    public void setInitialPosition(InteractableCardView view, @Nullable ReadableMap setInitialPosition) {
        view.setInitialPosition(RNConvert.pointF(setInitialPosition));

    }

    @Override
    protected void addEventEmitters(ThemedReactContext reactContext, InteractableCardView view) {
        view.setEventListener(new InteractionEventEmitter(view, reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher()));
    }

    @javax.annotation.Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onSnap", MapBuilder.of("registrationName", "onSnap"))
                .put("onWillSnap", MapBuilder.of("registrationName", "onWillSnap"))
                .put("onAlert", MapBuilder.of("registrationName", "onAlert"))
                .put("onAnimatedEvent", MapBuilder.of("registrationName", "onAnimatedEvent"))
                .put("onDrag", MapBuilder.of("registrationName", "onDrag"))
                .put("onStop", MapBuilder.of("registrationName", "onStop"))
                .build();
    }

    private static class InteractionEventEmitter implements InteractableCardView.InteractionListener {
        private final InteractableCardView InteractableCardView;
        private final EventDispatcher eventDispatcher;

        public InteractionEventEmitter(InteractableCardView view, EventDispatcher eventDispatcher) {
            this.InteractableCardView = view;
            this.eventDispatcher = eventDispatcher;
        }

        @Override
        public void onWillSnap(int indexOfSnapPoint, String snapPointId) {
            eventDispatcher.dispatchEvent(new Events.OnWillSnapEvent(InteractableCardView.getId(), indexOfSnapPoint, snapPointId));
        }

        @Override
        public void onSnap(int indexOfSnapPoint, String snapPointId) {
            eventDispatcher.dispatchEvent(new Events.OnSnapEvent(InteractableCardView.getId(), indexOfSnapPoint, snapPointId));
        }

        @Override
        public void onAlert(String alertAreaId, String alertType) {
            eventDispatcher.dispatchEvent(new Events.OnAlertEvent(InteractableCardView.getId(), alertAreaId, alertType));
        }

        @Override
        public void onAnimatedEvent(float x, float y) {
            eventDispatcher.dispatchEvent(new Events.OnAnimatedEvent(InteractableCardView.getId(), x, y));
        }

        @Override
        public void onDrag(String state, float x, float y, String targetSnapPointId) {
            eventDispatcher.dispatchEvent(new Events.onDrag(InteractableCardView.getId(),state, x, y, targetSnapPointId));
        }

        @Override
        public void onStop(float x, float y) {
            eventDispatcher.dispatchEvent(new Events.onStop(InteractableCardView.getId(), x, y));
        }
    }
}

