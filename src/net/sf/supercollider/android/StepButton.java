package net.sf.supercollider.android;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.graphics.Rect;
import android.view.View.OnTouchListener;
import android.graphics.Typeface;
import java.lang.Math;
import android.util.Log;
import android.app.Activity;

public class StepButton extends Button {
    private boolean isCurrentStep = false;
    private Float previousY = null;
    //    private Integer previousMotionEvent = null;
    private boolean moveInProgress = false;

    public StepButton(Context context, int id) {
        super(context, id);

        this.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d("Kosmische", event.toString());
                    Rect hitRect = new Rect();
                    StepButton.this.getHitRect(hitRect);
                    
                    if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_POINTER_UP) 
                        && hitRect.contains((int) event.getX(), (int) event.getY())) {
                        Log.d("Kosmische", event.toString());
                        if(!moveInProgress) {
                            isSelected = !isSelected;
                        }
                        moveInProgress = false;
                        invalidate();
                    }

                    if (event.getAction() == MotionEvent.ACTION_MOVE && previousY != null) {
                        moveInProgress = true;
                        KosmischeActivity parentActivity = ((KosmischeActivity) StepButton.this.getContext());
                        int step = StepButton.this.getId();
                        Note note = parentActivity.getSequence().get(step);
                        float dy = event.getY() - previousY;

                        //                        if(Math.abs(dy) >= 5) {
                        if(dy < 0) {
                            note.increment();
                        }
                        else {
                            note.decrement();
                        }
                        StepButton.this.setLabelText(((Integer) note.getMidiNumber()).toString());
                        StepButton.this.invalidate();
                    }

                    previousY = event.getY();
                    //                    previousMotionEvent = event.getAction();
                    return true;
                }
            });
    }

    public void setIsCurrentStep(boolean isCurrentStep) {
        this.isCurrentStep = isCurrentStep;
    }

    public boolean isCurrentStep() {
        return isCurrentStep;
    }

    protected void drawFill(Canvas canvas) {
        if(isSelected || isCurrentStep) {
            canvas.drawRect(1, 1, width - 1, height - 1, fill);
        }
    }
}
