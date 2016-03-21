/**
 * Client-side Connector
 */

window.com_aaron_mbpet_components_flot_FlotChart = function() {

	var element = $(this.getElement());
	var rpcProxy = this.getRpcProxy();
	var self = this;
	
	var state = this.getState();
	var options = state.options;
	var data = state.data;
	var p = $.plot(element, data, options);

	this.onStateChange = function() {
//		var state = this.getState();
//		var options = state.options;
//		var data = state.data;
//		$.plot(element, data, options); //options {}
//		p = $.plot(element, data, options);
		p;
	}
	
	
	/*
	 * edit points
	 */
	var txt;
	var newPositionX = null;
	var newPositionY = null; // datapoint1, datapoint2;
	
	element.bind("datadrop", function(event,pos,item) {
		newPositionX = pos.x1.toFixed(2);	//Math.round()
		newPositionY = pos.y1.toFixed(2);	
//		datapoint1 = item.datapoint[0].toFixed(2);
//		datapoint2 = item.datapoint[1].toFixed(2);
		
//	    txt = "Datapoint(" + item.seriesIndex + "," + item.dataIndex + ") dragged" +
//	    "\nfrom " + item.datapoint[0].toFixed(2) + " , " + item.datapoint[1].toFixed(2) +
//	    "\nto " + newPositionX + " , " + newPositionY;
//		alert(txt);
	        	    
	    // commit changes to the chart and redraw
        data[item.seriesIndex].data[item.dataIndex] = [newPositionX, newPositionY]; //[pos.x1.toFixed(2),pos.y1.toFixed(2)];
        p = $.plot(element, data, options);
		
        if (item) {
			rpcProxy.onDataDrop(item.seriesIndex, item.dataIndex, item.datapoint, newPositionX, newPositionY, data);
//			self.onDataDrop(item.seriesIndex, item.dataIndex, item.datapoint, newPositionX, newPositionY, data);
		}
//        alert(txt); 
	});
	
//	this.getPosAfterDrop = function() {
//		var positions = [newPositionX, newPositionY];
//		alert("positions are: " + positions);
//		return (positions);
//	}
	
	this.getCurrentData = function(item) {
		if (item) {
			rpcProxy.onDataUpdate(data);
		}
//		alert(data);
		
		var currData = p.getData();
		return currData;		
	}
	
	
	
	/*
	 * real-time updates
	 */
//	var prevY = 0,
//		prevX = 0,
//		updateInterval = 500;
//
//	function getNewData() {
//		var x,y;
//		if (prevY <= 150) {
//			y = prevY + 10; 
//			x = prevX + 1; 	
//		} else {
//			y = prevY - 75; 
//			x = prevX + 1; 	
//		};	
//		data.push([x, y]);
//		
//		prevY = y;
//		prevX = x;
//
//		return data;
//	}
		
	this.reset = function(newdata){
		alert("resetting chart js connector with data:" + newdata);
//		state = this.getState();
//		options = state.options;
//		data = state.data;
//		$.plot(element, data, options);
//		data.push([x, y]);
		data = newdata;
		p.setData([data]);
//		p.setData([getNewData()]);
		p.setupGrid();			// Since the axes do change, we need to call plot.setupGrid()
		p.draw();
//		p;
	}
	this.update = function(x, y) {
		data.push([x, y]);
		p.setData([data]);
//		p.setData([getNewData()]);
		p.setupGrid();			// Since the axes do change, we need to call plot.setupGrid()
		p.draw();
//		setTimeout(update, updateInterval);
/*
	var i = 0;
		do {
			p.setData([getNewData()]);
			
			p.setupGrid();			// Since the axes do change, we need to call plot.setupGrid()
			p.draw();
			i += 1;
			setTimeout(this, 500);
		} while (i<10);
 */
	}
//	this.update = function() {
//		data.push([10,15], [20,20], [30,30]);
//		p.setData([data]);
//		p.setData([getNewData()]);
//		p.setupGrid();			// Since the axes do change, we need to call plot.setupGrid()
//		p.draw();
//		setTimeout(update, updateInterval);
//	}
	
	
//	this.highlight = function(seriesIndex, dataIndex, data) {
//		if (p) {
//			p.highlight(seriesIndex, dataIndex);
//		}
//	};
	
	
//	element.bind("update", function(event,pos,item) {
//      if (item) {
//			rpcProxy.onDataUpdate(item.seriesIndex, item.dataIndex);
//			self.onDataUpdate(item.seriesIndex, item.dataIndex);
//		}
//		p.setData([getNewData()]);
//		p.setupGrid();
//		p.draw();
//		setTimeout(update, updateInterval);
//	});
	

	
	/*
	 * click events
	 */
	element.bind('plotclick', function(event, pos, item) {
		if (item) {
//			rpcProxy.onPlotClick(item.seriesIndex, item.dataIndex);
			rpcProxy.onPlotClick(item.seriesIndex, item.dataIndex, item.datapoint);
//			self.onPlotClick(item.seriesIndex, item.dataIndex);
//			highlight(item.series, item.datapoint);
//			alert("you clicked at: " + pos.x + ", " + pos.y + "- " + pos.x1 + ", " + pos.y1);
		}
	});
	
	this.registerRpc({
		highlight: function(seriesIndex, dataIndex) {
			if (p) {
				p.highlight(seriesIndex, dataIndex);
			}
		}
	});
	
	
	element.on("plothover", function(event,pos,item) {
		$.plot.JUMlib.library.showHover(event,pos,item,false);
//		if (item) {
//			rpcProxy.onPlotClick(item.seriesIndex, item.dataIndex);
//			self.onPlotClick(item.seriesIndex, item.dataIndex);
//		}
	});
	
	
	/*
	 * Resize event
	 * The plugin includes a jQuery plugin for adding resize events to any
	 * element.  Add a callback so we can display the placeholder size.
	 */
//	element.resize(function() {
//		$(".message").text("Placeholder is now "
//			+ $(this).width() + "x" + $(this).height()
//			+ " pixels");
//	});
//
//	$(".demo-container").resizable({
//		maxWidth: 900,
//		maxHeight: 500,
//		minWidth: 450,
//		minHeight: 250
//	});
	
	
	/*
	 * Highlight event
	 */
//	this.highlight = function(seriesIndex, dataIndex) {
//		if (p) {
//			p.highlight(seriesIndex, dataIndex);
//		}
//	};
	
	
}