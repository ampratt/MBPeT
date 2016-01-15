package com.aaron.mbpet.services;

import com.aaron.mbpet.ui.RampFlotWindow;

public final class FlotUtils {

	
	/**
	 * Convert the proper Ramp format of 	-> [(0,0), (1,1), ..]
	 * to bracketed format for flotcharts 	-> [[0,0], [1,1], ..]
	 */
	public static String formatRampToFlot(String ramp) {
		String formatted = "";
		if (ramp !=null){
			formatted = ramp.replace("(", "[");
			formatted = formatted.replace(")", "]");
			System.out.println("formatted ->" + formatted);
			
		}

		return formatted;
	}

	/**
	 * Convert the Flot chart format of -> [[0,0], [1,1], ..]
	 * to the correct Ramp format 		-> [(0,0), (1,1), ..]
	 */
	public static String formatFlotToRamp(String flot) {
		String formatted = "";
		if (flot !=null){
			formatted = flot.substring(1, flot.length()-1);
				System.out.println("Substring ->" + formatted);
			formatted = formatted.replace("[", "(");
			formatted = formatted.replace("]", ")");
			formatted = "[" + formatted + "]";
				System.out.println("formatted ->" + formatted);
		}
		
		return formatted;
	}
	
	/**
	 * whenever a point is drag-n-drop'ed
	 * this method is triggered to automatically update
	 * the data variable to be sent elsewhere.
	 */
	public static void triggerDataUpdate() {
		RampFlotWindow.updateDataInField();
	}
	
}
