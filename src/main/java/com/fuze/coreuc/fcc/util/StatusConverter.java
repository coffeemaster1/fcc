package com.fuze.coreuc.fcc.util;

import java.util.HashMap;
import java.util.Map;

public class StatusConverter {

	public static final int AST_DEVICE_UNKNOWN = 0;
	public static final int AST_DEVICE_NOT_INUSE = 1;
	public static final int AST_DEVICE_INUSE = 2;
	public static final int AST_DEVICE_BUSY = 3;
	public static final int AST_DEVICE_INVALID = 4;
	public static final int AST_DEVICE_UNAVAILABLE = 5;
	public static final int AST_DEVICE_RINGING = 6;
	public static final int AST_DEVICE_RINGINUSE = 7;
	public static final int AST_DEVICE_ONHOLD = 8;

	public static final int AST_STATE_DOWN = 0;
	public static final int AST_STATE_RESERVED = 1;		/*!< Channel is down, but reserved */
	public static final int AST_STATE_OFFHOOK = 2;		/*!< Channel is off hook */
	public static final int AST_STATE_DIALING = 3;		/*!< Digits (or equivalent) have been dialed */
	public static final int AST_STATE_RING = 4;			/*!< Line is ringing */
	public static final int AST_STATE_RINGING = 5;		/*!< Remote end is ringing */
	public static final int AST_STATE_UP = 6;			/*!< Line is up */
	public static final int AST_STATE_BUSY = 7;			/*!< Line is busy */
	public static final int AST_STATE_DIALING_OFFHOOK = 8;	/*!< Digits (or equivalent) have been dialed while offhook */
	public static final int AST_STATE_PRERING = 9;		/*!< Channel has detected an incoming call and is waiting for ring */

	private static final Map<Integer, String> queueStatusCodes = new HashMap<>();
	private static final Map<Integer, Integer> extensionStatusMap = new HashMap<>();
	static {
		queueStatusCodes.put(AST_DEVICE_UNKNOWN, "Unknown");
		queueStatusCodes.put(AST_DEVICE_NOT_INUSE, "Not In Use");
		queueStatusCodes.put(AST_DEVICE_INUSE, "In Use");
		queueStatusCodes.put(AST_DEVICE_BUSY, "Busy");
		queueStatusCodes.put(AST_DEVICE_INVALID, "Invalid");
		queueStatusCodes.put(AST_DEVICE_UNAVAILABLE, "Unavailable");
		queueStatusCodes.put(AST_DEVICE_RINGING, "Ringing");
		queueStatusCodes.put(AST_DEVICE_RINGINUSE, "Ring In Use");
		queueStatusCodes.put(AST_DEVICE_ONHOLD, "On Hold");

		extensionStatusMap.put(AST_STATE_DOWN, AST_DEVICE_NOT_INUSE);
		extensionStatusMap.put(AST_STATE_RESERVED, AST_DEVICE_NOT_INUSE);
		extensionStatusMap.put(AST_STATE_OFFHOOK, AST_DEVICE_INUSE);
		extensionStatusMap.put(AST_STATE_DIALING, AST_DEVICE_INUSE);
		extensionStatusMap.put(AST_STATE_RING, AST_DEVICE_RINGING);
		extensionStatusMap.put(AST_STATE_RINGING, AST_DEVICE_RINGING);
		extensionStatusMap.put(AST_STATE_UP, AST_DEVICE_INUSE);
		extensionStatusMap.put(AST_STATE_BUSY, AST_DEVICE_BUSY);
		extensionStatusMap.put(AST_STATE_DIALING_OFFHOOK, AST_DEVICE_INUSE);
		extensionStatusMap.put(AST_STATE_PRERING, AST_DEVICE_RINGING);
	}

	public String getQueueStatusString (Integer statusCode) {
		return queueStatusCodes.get(statusCode);
	}

	public String getExtensionStatusString (Integer statusCode) {
		return queueStatusCodes.get(extensionStatusMap.get(statusCode));
	}

}
