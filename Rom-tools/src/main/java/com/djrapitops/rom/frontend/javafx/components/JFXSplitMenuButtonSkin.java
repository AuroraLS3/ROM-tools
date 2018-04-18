package com.djrapitops.rom.frontend.javafx.components;

import com.jfoenix.controls.JFXRippler;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.CachedTransition;
import com.jfoenix.utils.JFXNodeUtils;
import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.SplitMenuButtonSkin;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * //TODO Class Javadoc Comment
 *
 * @author Rsl1122
 */
public class JFXSplitMenuButtonSkin extends SplitMenuButtonSkin {

    private Transition clickedAnimation;
    private JFXRippler buttonRippler;
    private Runnable releaseManualRippler = null;
    private boolean invalid = true;
    private boolean mousePressed = false;

    public JFXSplitMenuButtonSkin(SplitMenuButton button) {
        super(button);

        buttonRippler = new JFXRippler(getSkinnable()) {
            @Override
            protected Node getMask() {
                StackPane mask = new StackPane();
                mask.shapeProperty().bind(getSkinnable().shapeProperty());
                JFXNodeUtils.updateBackground(getSkinnable().getBackground(), mask);
                mask.resize(getWidth() - snappedRightInset() - snappedLeftInset(),
                        getHeight() - snappedBottomInset() - snappedTopInset());
                return mask;
            }

            @Override
            protected void positionControl(Node control) {
                // do nothing as the controls is not inside the ripple
            }
        };

        // add listeners to the button and bind properties
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> playClickAnimation(1));
//        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> playClickAnimation(-1));
        button.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> mousePressed = true);
        button.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> mousePressed = false);
        button.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> mousePressed = false);

        button.armedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                if (!mousePressed) {
                    releaseManualRippler = buttonRippler.createManualRipple();
                    playClickAnimation(1);
                }
            } else {
                if (releaseManualRippler != null) {
                    releaseManualRippler.run();
                    releaseManualRippler = null;
                }
                playClickAnimation(-1);
            }
        });

        /*
         * disable action when clicking on the button shadow
         */
        button.setPickOnBounds(false);

        updateChildren();
    }

    protected void updateChildren() {
        if (buttonRippler != null)
            getChildren().add(0, buttonRippler);
        for (int i = 1; i < getChildren().size(); i++) {
            final Node child = getChildren().get(i);
            if (child instanceof Text)
                child.setMouseTransparent(true);
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        if (invalid) {
            // change rippler fill according to the last LabeledText/Label child
            for (int i = getChildren().size() - 1; i >= 1; i--) {
                if (getChildren().get(i) instanceof LabeledText) {
                    buttonRippler.setRipplerFill(((LabeledText) getChildren().get(i)).getFill());
                    ((LabeledText) getChildren().get(i)).fillProperty()
                            .addListener((o, oldVal, newVal) -> buttonRippler.setRipplerFill(
                                    newVal));
                    break;
                } else if (getChildren().get(i) instanceof Label) {
                    buttonRippler.setRipplerFill(((Label) getChildren().get(i)).getTextFill());
                    ((Label) getChildren().get(i)).textFillProperty()
                            .addListener((o, oldVal, newVal) -> buttonRippler.setRipplerFill(
                                    newVal));
                    break;
                }
            }
            invalid = false;
        }
        buttonRippler.resizeRelocate(
                getSkinnable().getLayoutBounds().getMinX(),
                getSkinnable().getLayoutBounds().getMinY(),
                getSkinnable().getWidth(), getSkinnable().getHeight());
//        layoutLabelInArea(x, y, w, h);
    }

    private void playClickAnimation(double rate) {
        if (clickedAnimation != null) {
            if (!clickedAnimation.getCurrentTime().equals(clickedAnimation.getCycleDuration()) || rate != 1) {
                clickedAnimation.setRate(rate);
                clickedAnimation.play();
            }
        }
    }

    private class ButtonClickTransition extends CachedTransition {
        ButtonClickTransition(DropShadow shadowEffect) {
            super(getSkinnable(), new Timeline(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(shadowEffect.radiusProperty(),
                                            JFXDepthManager.getShadowAt(2).radiusProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.spreadProperty(),
                                            JFXDepthManager.getShadowAt(2).spreadProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.offsetXProperty(),
                                            JFXDepthManager.getShadowAt(2).offsetXProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.offsetYProperty(),
                                            JFXDepthManager.getShadowAt(2).offsetYProperty().get(),
                                            Interpolator.EASE_BOTH)
                            ),
                            new KeyFrame(Duration.millis(1000),
                                    new KeyValue(shadowEffect.radiusProperty(),
                                            JFXDepthManager.getShadowAt(5).radiusProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.spreadProperty(),
                                            JFXDepthManager.getShadowAt(5).spreadProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.offsetXProperty(),
                                            JFXDepthManager.getShadowAt(5).offsetXProperty().get(),
                                            Interpolator.EASE_BOTH),
                                    new KeyValue(shadowEffect.offsetYProperty(),
                                            JFXDepthManager.getShadowAt(5).offsetYProperty().get(),
                                            Interpolator.EASE_BOTH)
                            )
                    )
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.2));
            setDelay(Duration.seconds(0));
        }
    }
}