/*
 * SlipGestureDetector.java
 * Android-Charts
 *
 * Created by limc on 2014.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.limc.androidcharts.event;

import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * <p>
 * en
 * </p>
 * <p>
 * jp
 * </p>
 * <p>
 * cn
 * </p>
 * 
 * @author limc
 * @version v1.0 2014/06/23 16:48:07
 * 
 */
public class SlipGestureDetector<T extends ISlipable> extends ZoomGestureDetector<IZoomable> {
	protected PointF startPointA;
	protected PointF startPointB;

	protected boolean isLongPressed = false;
	protected boolean isMoved = false;
	protected long pressStartTime;
	
	private OnSlipGestureListener onSlipGestureListener;
	private GestureHandler gestureHandler;
	private int messageId = 1;

	public SlipGestureDetector(ISlipable slipable){
		super(slipable);
		if (slipable != null) {
			this.onSlipGestureListener = slipable.getOnSlipGestureListener();
		}
		gestureHandler = new GestureHandler(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param event
	 * 
	 * @return
	 * 
	 * @see
	 * cn.limc.androidcharts.event.IGestureDetector#onTouchEvent(android.view
	 * .MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模式
		case MotionEvent.ACTION_DOWN:
			if (event.getPointerCount() == 1) {
				touchMode = TOUCH_MODE_SINGLE;
				startPointA = new PointF(event.getX(0), event.getY(0));
				pressStartTime = System.currentTimeMillis();
			}
			Message message = gestureHandler.obtainMessage();
			message.what = messageId;
			message.getData().putParcelable("event", event);
			gestureHandler.sendMessageDelayed(message, 200);
			break;
		case MotionEvent.ACTION_UP:
			startPointA = null;
			startPointB = null;

			pressStartTime = 0;
			isLongPressed = false;
			isMoved = false;
			gestureHandler.removeMessages(messageId);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			startPointA = null;
			startPointB = null;

			pressStartTime = 0;
			isLongPressed = false;
			isMoved = false;
			gestureHandler.removeMessages(messageId);
		case MotionEvent.ACTION_POINTER_DOWN:
			olddistance = calcDistance(event);
			if (olddistance > MIN_DISTANCE) {
				touchMode = TOUCH_MODE_MULTI;
				startPointA = new PointF(event.getX(0), event.getY(0));
				startPointB = new PointF(event.getX(1), event.getY(1));
			}
			gestureHandler.removeMessages(messageId);
			return true;
			//break;
		case MotionEvent.ACTION_MOVE:
			if (touchMode == TOUCH_MODE_MULTI) {
				newdistance = calcDistance(event);
				if (newdistance > MIN_DISTANCE) {
					if (startPointA.x >= event.getX(0)
							&& startPointB.x >= event.getX(1)) {
						if (onSlipGestureListener != null) {
							onSlipGestureListener.onMoveRight((ISlipable)instance,event, newdistance);
						}
					} else if (startPointA.x <= event.getX(0)
							&& startPointB.x <= event.getX(1)) {
						if (onSlipGestureListener != null) {
							onSlipGestureListener.onMoveLeft((ISlipable)instance,event, newdistance);
						}
					} else {
						if (Math.abs(newdistance - olddistance) > MIN_DISTANCE) {
							if (onZoomGestureListener != null) {
								if (newdistance > olddistance) {
									onZoomGestureListener.onZoomIn((IZoomable)instance,event);
								} else {
									onZoomGestureListener.onZoomOut((IZoomable)instance,event);
								}
							}
							// reset distance
							olddistance = newdistance;
						}
					}
					startPointA = new PointF(event.getX(0), event.getY(0));
					startPointB = new PointF(event.getX(1), event.getY(1));

					return true;
				}
				gestureHandler.removeMessages(messageId);
			} else if(touchMode == TOUCH_MODE_SINGLE) {//扩展:单个手指移动事件处理
				if (startPointA != null) {
					PointF currentPoint = new PointF(event.getX(0), event.getY(0));
					if(!isLongPressed && !isMoved){
						float offsetX = Math.abs(currentPoint.x - startPointA.x);
						float offsetY = Math.abs(currentPoint.y - startPointA.y);
						if (offsetX <= 80 && offsetY <= 80) {
							if (pressStartTime - System.currentTimeMillis()  < -500) {
								isLongPressed = true;
								if (onTouchGestureListener != null) {
									onTouchGestureListener.onLongPressDown(instance, event);
								}
							}
						}else{
							isMoved = true;
						}
					}
					if(isLongPressed){
						touchPoint = new PointF(event.getX(),event.getY());
						// call back to listener
						if (onTouchGestureListener != null) {
							onTouchGestureListener.onLongPressMoved(instance,event);
						}
					}else if(isMoved){
						if (currentPoint.x > startPointA.x){
							if (onSlipGestureListener != null) {
								onSlipGestureListener.onMoveLeft((ISlipable) instance,event, currentPoint.x - startPointA.x);
							}
						}else if (currentPoint.x < startPointA.x){
							if (onSlipGestureListener != null) {
								onSlipGestureListener.onMoveRight((ISlipable)instance,event, startPointA.x - currentPoint.x);
							}
						}
						startPointA.x = currentPoint.x;
						startPointA.y = currentPoint.y;
						gestureHandler.removeMessages(messageId);
					}
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}
	/**
	 * Package: cn.limc.androidcharts.event
	 * Class: SlipGestureDetector.GestureHandler
	 * Description: 长按未移动时，将事件识别为长按操作
	 * Author: zhaoyangzhou
	 * Email: zhaoyangzhou@126.com
	 * Created on: 2017/12/22 12:54
	 */
	static class GestureHandler extends Handler {
		private final WeakReference<SlipGestureDetector> mContext;

		public GestureHandler(SlipGestureDetector t) {
			mContext = new WeakReference<SlipGestureDetector>(t);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = (Bundle)msg.getData();
			MotionEvent event = (MotionEvent)bundle.getParcelable("event");
			if (msg.what == mContext.get().messageId) {//将手势识别为长按操作
				mContext.get().isLongPressed = true;
				if (mContext.get().onTouchGestureListener != null) {
					mContext.get().onTouchGestureListener.onLongPressDown(mContext.get().instance, event);
				}
			}
		}
	}
}
